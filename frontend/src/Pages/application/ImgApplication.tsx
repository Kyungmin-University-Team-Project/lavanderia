import React, { useState } from 'react';
import uploadImg from '../../Assets/Img/application/uploadImg.png';
import axiosInstance from "../../Utils/axios/axiosInstance";

const ImgApplication: React.FC = () => {
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [amount, setAmount] = useState<number | null>(null);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(event.target.files || []);
        setSelectedFiles(files);
    };

    const uploadFilesAndCalculateAmount = async () => {
        if (selectedFiles.length === 0) {
            alert('파일을 선택해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFiles[0]); // 서버에서 기대하는 필드 이름에 맞춰 파일 추가

        try {
            const response = await axiosInstance.post(`/laundry/price`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            setAmount(response.data.amount); // 서버 응답으로 받은 금액 설정
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('금액 계산 중 오류가 발생했습니다.');
        }
    };

    const handleAddToCart = () => {
        alert(`세탁물이 추가되었습니다: 총 금액 ${amount} KRW`);
    };

    return (
        <div className="max-w-lg mx-auto mt-5 bg-white">
            <h1 className="text-2xl font-bold mb-5 text-gray-800">세탁 신청 상세</h1>

            <div className="mb-5">
                <label className="block font-medium mb-2 text-gray-600">
                    클릭이나 드래그로 사진을 업로드 해주세요!
                </label>
                <div
                    className="relative w-full h-56 border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center bg-gray-50 hover:bg-gray-100 transition">
                    <div
                        className="absolute inset-0 flex items-center justify-center"
                        style={{
                            backgroundImage: `url(${uploadImg})`,
                            width: '50%',
                            transform: "scale(0.5)",
                            backgroundSize: 'cover',
                            opacity: '0.3'
                        }}
                    ></div>
                    <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                        className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                    />
                    <span className="z-10 text-sm font-medium text-gray-500">
                        {selectedFiles.length > 0 ? `${selectedFiles.length}개 파일 선택됨` : '파일을 선택하세요'}
                    </span>
                </div>
                {selectedFiles.length > 0 && (
                    <div className="mt-3 grid grid-cols-3 gap-2">
                        {selectedFiles.map((file, index) => (
                            <img
                                key={index}
                                src={URL.createObjectURL(file)}
                                alt="preview"
                                className="w-full h-20 object-cover rounded-md border"
                            />
                        ))}
                    </div>
                )}
            </div>

            <div className="mb-5">
                <button
                    onClick={uploadFilesAndCalculateAmount}
                    className="w-full px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-md transition"
                >
                    금액 계산
                </button>
                {amount !== null && (
                    <div className="mt-3 text-center">
                        <p className="text-lg font-semibold text-gray-700">계산된 금액: {amount} 원</p>
                    </div>
                )}
            </div>

            <button
                onClick={handleAddToCart}
                className="w-full px-4 py-2 bg-gray-700 hover:bg-gray-800 text-white font-semibold rounded-lg shadow-md transition"
            >
                세탁물 추가하기
            </button>
        </div>
    );
};

export default ImgApplication;
