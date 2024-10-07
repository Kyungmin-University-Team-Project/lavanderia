import os
import yaml
import torch
import torch.nn as nn
import numpy as np
import cv2
from PIL import Image, ImageEnhance, ImageFilter
from flask import Flask, request, jsonify
from flask_cors import CORS
from timm import create_model
from torchvision.transforms import transforms

# 아나콘다 환경 설정
conda_env_path = os.path.join(os.path.dirname(__file__), 'imagerun.yaml')
with open(conda_env_path, 'r') as f:
    conda_env = yaml.safe_load(f)

# 환경 변수 설정
for key, value in conda_env.get('dependencies', []):
    if isinstance(value, str) and '=' in value:
        name, version = value.split('=')
        os.environ[name.upper()] = version

app = Flask(__name__)
CORS(app)

# ResNetV2 모델 정의
class ModifiedResNetV2(nn.Module):
    def __init__(self, num_classes):
        super(ModifiedResNetV2, self).__init__()
        self.resnet = create_model('resnetv2_50x1_bit.goog_in21k', pretrained=True)
        self.resnet.head = nn.Identity()  # 기존의 fully connected layer 제거
        self.pool = nn.AdaptiveAvgPool2d((1, 1))
        self.flatten = nn.Flatten()
        self.fc = nn.Linear(self.resnet.num_features, num_classes)  # 새로운 출력층 추가

    def forward(self, x):
        x = self.resnet.forward_features(x)
        x = self.pool(x)  # 풀링을 추가해 입력 크기를 줄임
        x = self.flatten(x)  # Flatten으로 2D -> 1D 변환
        x = self.fc(x)  # 최종 레이어에 입력
        return x


# 이미지 전처리 및 예측 함수
def preprocess_and_predict(image_file, model, transform, class_labels):
    image = Image.open(image_file).convert('RGB')

    # 이미지 전처리
    old_size = image.size
    ratio = float(512) / max(old_size)
    new_size = tuple([int(x * ratio) for x in old_size])
    image = image.resize(new_size, Image.Resampling.LANCZOS)

    new_image = Image.new("RGB", (512, 512), image.getpixel((0, 0)))
    new_image.paste(image, ((512 - new_size[0]) // 2, (512 - new_size[1]) // 2))
    image = new_image.filter(ImageFilter.MedianFilter(size=3))
    image = image.filter(ImageFilter.UnsharpMask(radius=2, percent=150, threshold=3))

    image_np = np.array(image)
    image_gray = cv2.cvtColor(image_np, cv2.COLOR_RGB2GRAY)
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
    image_clahe = clahe.apply(image_gray)
    image = Image.fromarray(cv2.cvtColor(image_clahe, cv2.COLOR_GRAY2RGB))

    # 모델 예측
    image = transform(image).unsqueeze(0).to(device)
    model.eval()
    with torch.no_grad():
        outputs = model(image)
        _, predicted = torch.max(outputs, 1)
        predicted_class = class_labels[predicted.item()]

    return predicted_class


# 클래스 목록 및 가격 정의
class_labels = ['tshirt', 'dress', 'hoodie', 'pants', 'shirt', 'shoes', 'shorts', 'skirt']
class_prices = {
    'tshirt': 11000,
    'dress': 23000,
    'hoodie': 13000,
    'pants': 10000,
    'shirt': 9000,
    'shoes': 21000,
    'shorts': 8000,
    'skirt': 12000
}

# 장치 설정
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# ResNetV2 모델 불러오기
num_classes = len(class_labels)
model = ModifiedResNetV2(num_classes=num_classes)
model = model.to(device)

# 사전 훈련된 모델 가중치 불러오기
pretrained_state_dict = torch.load(
    'src/main/python/best_resnetv2.pth', map_location=device)
model.load_state_dict(pretrained_state_dict)

# 이미지 전처리 설정
transform = transforms.Compose([
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image file provided'}), 400

    image_file = request.files['image']

    if not image_file.filename.lower().endswith(('.jpg', '.jpeg', '.png')):
        return jsonify({'error': 'Unsupported file format. Please upload a .jpg, .jpeg, or .png file.'}), 400

    predicted_class = preprocess_and_predict(image_file, model, transform, class_labels)
    predicted_price = class_prices[predicted_class]

    return jsonify({'predicted_class': predicted_class, 'price': predicted_price})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)