from setuptools import setup, find_packages

setup(
    name="TRACER",
    version="0.1",
    packages=find_packages(include=['TRACER', 'TRACER.*']),
    install_requires=[
        "torch",
        "torchvision",
        "opencv-python",
        "numpy",
        "albumentations",
        "tqdm",
        "scikit-learn",
        "fastapi",
        "python-multipart",
        "uvicorn",
        "httpx"
    ],
)