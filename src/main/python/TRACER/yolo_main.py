import os
os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

from flask import Flask, request, send_file, jsonify
from PIL import Image
import io
import cv2
import numpy as np
from pathlib import Path
import json
import os
import sys
import torch
from ultralytics import YOLO, __version__ as ultralytics_version
from python.TRACER import BackgroundRemover
import yaml
from datetime import datetime


class YoloProcessor:
    """
    의류 이미지 처리를 위한 메인 프로세서 클래스
    배경 제거와 의류 분류를 통합적으로 처리
    
    주요 기능:
    1. 배경 제거 (TRACER 모델 사용)
    2. 의류 분류 (YOLOv8 모델 사용)
    3. REST API 엔드포인트 제공
    """
    
    def __init__(self):
        """
        초기화 함수
        1. 경로 설정
        2. 모델 및 설정 파일 로드
        3. CUDA/CPU 디바이스 설정
        """
        self.setup_paths()
        self.load_models()
        self.setup_device()

    def setup_paths(self):
        """
        프로젝트 경로 설정 및 환경변수 설정
        - TRACER 모델 관련 경로 (model, util, config)
        - 환경변수 'TRACER_PATH' 설정
        """
        current_dir = Path(__file__).parent
        project_root = current_dir.parent
        
        self.BASE_PATH = str(project_root)
        self.TRACER_PATH = str(current_dir)

        # TRACER 경로 추가
        sys.path.extend([
            self.TRACER_PATH,
            str(Path(self.TRACER_PATH) / "model"),
            str(Path(self.TRACER_PATH) / "util"),
            str(Path(self.TRACER_PATH) / "config")
        ])

        os.environ['TRACER_PATH'] = self.TRACER_PATH

    def load_models(self):
        """
        필요한 모든 모델과 설정 파일 로드
        - YOLO 모델 (best.pt)
        - 가격 정보 (price_info.json)
        - 클래스 정보 (data.yaml)
        - 모델 설정 (args.yaml)
        """
        # 모델 및 설정 파일 로드
        self.MODEL_PATH = str(Path(self.TRACER_PATH) / "best.pt")
        self.PRICE_INFO_PATH = str(Path(self.TRACER_PATH) / "price_info.json")
        self.DATA_YAML_PATH = str(Path(self.TRACER_PATH) / "data.yaml")
        self.ARGS_YAML_PATH = str(Path(self.TRACER_PATH) / "args.yaml")
        
        # 파일 존재 확인
        assert os.path.exists(self.MODEL_PATH), f"Model file not found at {self.MODEL_PATH}"
        assert os.path.exists(self.PRICE_INFO_PATH), f"Price info file not found at {self.PRICE_INFO_PATH}"
        assert os.path.exists(self.DATA_YAML_PATH), f"Data yaml file not found at {self.DATA_YAML_PATH}"
        assert os.path.exists(self.ARGS_YAML_PATH), f"Args yaml file not found at {self.ARGS_YAML_PATH}"
        
        # 설정 파일들 로드
        with open(self.PRICE_INFO_PATH, "r", encoding="utf-8") as f:
            self.PRICE_INFO = json.load(f)
        
        with open(self.DATA_YAML_PATH, "r", encoding="utf-8") as f:
            self.CLASS_NAMES = yaml.safe_load(f)["names"]
        
        with open(self.ARGS_YAML_PATH, "r", encoding="utf-8") as f:
            self.ARGS = yaml.safe_load(f)

    def setup_device(self):
        """
        CUDA/CPU 설정 및 모델 초기화
        1. CUDA 메모리 관리
        2. YOLO 모델 설정 (confidence, IOU, max detection)
        3. BackgroundRemover 초기화
        """
        if torch.cuda.is_available():
            torch.cuda.empty_cache()
            torch.cuda.synchronize()
        
        self.DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
        
        # YOLO 모델 초기화
        self.YOLO_MODEL = YOLO(self.MODEL_PATH)
        self.YOLO_MODEL.to(self.DEVICE)
        
        # args.yaml에서 설정 가져오기
        self.YOLO_MODEL.conf = self.ARGS.get('conf', 0.25)
        self.YOLO_MODEL.iou = self.ARGS.get('iou', 0.7)
        self.YOLO_MODEL.max_det = self.ARGS.get('max_det', 300)
        
        self.YOLO_MODEL.model.eval()
        torch.set_grad_enabled(False)
        
        if torch.cuda.is_available():
            torch.cuda.synchronize()
        
        # device를 전달하여 BackgroundRemover 초기화
        self.bg_remover = BackgroundRemover(device=self.DEVICE)

    def get_best_prediction(self, results):
        if not results or len(results) == 0:
            return None

        result = results[0]
        if len(result.boxes) == 0:
            return None

        confidences = result.boxes.conf.cpu().numpy()
        classes = result.boxes.cls.cpu().numpy()

        # 가장 높은 신뢰도의 인덱스 찾기
        max_conf_idx = confidences.argmax()
        best_class = int(classes[max_conf_idx])
        best_conf = confidences[max_conf_idx]

        return {
            "class_name": self.CLASS_NAMES[best_class],
            "confidence": float(best_conf)
        }

    def remove_background(self, file_bytes):
        nparr = np.frombuffer(file_bytes, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        result = self.bg_remover.remove_background(image)
        _, buffer = cv2.imencode('.png', result)
        return io.BytesIO(buffer.tobytes())

    def process_image(self, file_bytes):
        """
        이미지 처리 메인 파이프라인
        
        Args:
            file_bytes: 바이트 형태의 이미지 데이터
            
        Returns:
            dict: 분류 결과 (클래스명, 신뢰도, 가격)
            
        처리 과정:
        1. 이미지 로드
        2. RGBA/RGB 변환
        3. 배경 제거 (필요한 경우)
        4. YOLO 모델로 의류 분류
        """
        # 이미지 읽기
        nparr = np.frombuffer(file_bytes, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_UNCHANGED)

        # 이미 RGBA 이미지인 경우 (배경이 제거된 이미지)
        if len(image.shape) == 3 and image.shape[2] == 4:
            # RGBA를 RGB로 변환 (검은색 배경)
            rgb_image = np.zeros((image.shape[0], image.shape[1], 3), dtype=np.uint8)
            alpha_mask = image[:, :, 3] == 255
            rgb_image[alpha_mask] = image[alpha_mask, :3]
            processed_image = rgb_image
        else:
            # 배경 제거 수행 후 RGB로 변환
            rgba_image = self.bg_remover.remove_background(image)
            rgb_image = np.zeros((rgba_image.shape[0], rgba_image.shape[1], 3), dtype=np.uint8)
            alpha_mask = rgba_image[:, :, 3] == 255
            rgb_image[alpha_mask] = rgba_image[alpha_mask, :3]
            processed_image = rgb_image

        # YOLO로 의류 분류 (RGB 이미지 직접 사용)
        results = self.YOLO_MODEL.predict(
            source=processed_image,
            save=False,
            conf=self.ARGS.get('conf', 0.25),
            device=self.DEVICE
        )

        best_result = self.get_best_prediction(results)

        if best_result is None:
            return {
                "error": "No clothing detected",
                "debug_info": {
                    "results_length": len(results) if results else 0,
                    "boxes_length": len(results[0].boxes) if results and len(results) > 0 else 0,
                    "device": self.DEVICE
                }
            }

        class_name = best_result["class_name"]
        confidence = best_result["confidence"]
        price = self.PRICE_INFO.get(class_name, 0)

        return {
            "class_name": class_name,
            "confidence": confidence,
            "suggested_price": price
        }


# Flask 앱 초기화
app = Flask(__name__)
yolo_processor = YoloProcessor()


@app.route('/remove-background', methods=['POST'])
def remove_background_endpoint():
    if 'file' not in request.files:
        return jsonify({'error': 'No file provided'}), 400
    file = request.files['file']
    result_buffer = yolo_processor.remove_background(file.read())
    return send_file(
        result_buffer,
        mimetype='image/png',
        as_attachment=True,
        download_name='removed_background.png'
    )


@app.route('/process-image', methods=['POST'])
def process_image_endpoint():
    if 'file' not in request.files:
        return jsonify({'error': 'No file provided'}), 400
    file = request.files['file']
    return jsonify(yolo_processor.process_image(file.read()))


def print_version_info():
    print(f"PyTorch version: {torch.__version__}")
    print(f"Ultralytics version: {ultralytics_version}")
    print(f"CUDA available: {torch.cuda.is_available()}")
    if torch.cuda.is_available():
        print(f"CUDA version: {torch.version.cuda}")


if __name__ == "__main__":
    print_version_info()
    app.run(host='0.0.0.0', port=8000, debug=True)