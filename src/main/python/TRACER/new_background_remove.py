import os
os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

import cv2
import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision.transforms import transforms
from pathlib import Path
import sys

current_dir = Path(__file__).parent
if str(current_dir) not in sys.path:
    sys.path.append(str(current_dir))

from model.TRACER import TRACER
from dataloader import get_test_augmentation
from config import getConfig

class BackgroundRemover:
    """
    TRACER 모델을 사용한 이미지 배경 제거 클래스
    
    주요 기능:
    1. 이미지 전처리
    2. 배경 제거 추론
    3. 후처리 및 RGBA 이미지 생성
    """

    def __init__(self, device=None):
        """
        초기화 함수
        1. 경로 설정
        2. 모델 및 설정 파일 로드
        3. CUDA/CPU 디바이스 설정
        """
        try:
            # 원본 args 저장
            import sys
            original_args = sys.argv[:]
            sys.argv = [sys.argv[0], 'inference']
            
            self.args = getConfig()
            sys.argv = original_args
            
            # TE7 모델에 맞는 설정
            self.args.action = 'inference'
            self.args.arch = '7'
            self.args.img_size = 640
            self.args.dataset = 'DUTS'
            
            self.device = device if device is not None else torch.device('cuda' if torch.cuda.is_available() else 'cpu')
            self.test_transform = get_test_augmentation(img_size=self.args.img_size)
            
            # 모델 초기화
            self.model = TRACER(self.args).to(self.device)
            if self.args.multi_gpu:
                self.model = nn.DataParallel(self.model).to(self.device)
            
            model_path = Path(__file__).parent / "results/DUTS/TE7_0/best_model.pth"
            checkpoint = torch.load(str(model_path), map_location=self.device)
            self.model.load_state_dict(checkpoint)
            self.model.eval()
            
        except Exception as e:
            print(f"Error initializing BackgroundRemover: {str(e)}")
            if torch.cuda.is_available():
                torch.cuda.empty_cache()
            raise e

    def remove_background(self, image_np):
        """
        이미지 배경 제거 메인 함수
        
        Args:
            image_np: numpy 배열 형태의 이미지
            
        Returns:
            numpy.ndarray: RGBA 형식의 배경 제거된 이미지
            
        처리 과정:
        1. 이미지 크기 조정 (640x640)
        2. 텐서 변환 및 정규화
        3. TRACER 모델 추론
        4. 원본 크기로 복원
        """
        try:
            # 전처리
            h, w = image_np.shape[:2]
            image = cv2.resize(image_np, (self.args.img_size, self.args.img_size))
            org_image = self.test_transform(image=image)["image"]
            image_tensor = org_image[None, ...].to(self.device)

            # 추론
            with torch.no_grad():
                output, _, _ = self.model(image_tensor)

            # 후처리
            output = F.interpolate(output, size=(h, w), mode='bilinear')
            output = (output.squeeze().cpu().numpy() * 255.0).astype(np.uint8)

            # 마스크 적용
            result = self.post_processing(org_image, output, h, w)
            return result
            
        except Exception as e:
            if torch.cuda.is_available():
                torch.cuda.empty_cache()
            raise e

    def post_processing(self, original_image, output_image, height, width, threshold=200):
        """
        배경 제거 후 후처리 함수
        
        Args:
            original_image: 원본 이미지 텐서
            output_image: 모델 출력 마스크
            height, width: 원본 이미지 크기
            threshold: 마스크 임계값 (기본값: 200)
            
        Returns:
            numpy.ndarray: 최종 RGBA 이미지
            
        처리 과정:
        1. 정규화 해제
        2. 크기 복원
        3. 마스크 생성 및 적용
        4. RGBA 변환
        """
        try:
            invTrans = transforms.Compose([
                transforms.Normalize(mean=[0., 0., 0.], std=[1 / 0.229, 1 / 0.224, 1 / 0.225]),
                transforms.Normalize(mean=[-0.485, -0.456, -0.406], std=[1., 1., 1.]),
            ])

            original_image = invTrans(original_image)
            original_image = F.interpolate(original_image.unsqueeze(0), size=(height, width), mode='bilinear')
            original_image = original_image.squeeze().cpu().numpy()
            original_image = np.transpose(original_image, (1, 2, 0))
            original_image = (original_image * 255).astype(np.uint8)

            # 마스크 생성
            mask = (output_image > threshold).astype(np.uint8) * 255
            
            # RGBA 이미지 생성
            rgba = cv2.cvtColor(original_image, cv2.COLOR_BGR2BGRA)
            rgba[:, :, 3] = mask

            return rgba
            
        except Exception as e:
            if torch.cuda.is_available():
                torch.cuda.empty_cache()
            raise e