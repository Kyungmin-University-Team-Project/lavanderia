import React, { useState } from 'react';
import uploadImg from '../../Assets/Img/application/uploadImg.png';
import {API_URL} from "../../Api/api";
import BackButton from "../../Components/common/BackButton";

const ImgApplication: React.FC = () => {
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [amount, setAmount] = useState<number | null>(null);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(event.target.files || []);
        setSelectedFiles(files);
    };

    const uploadFilesAndCalculateAmount = async () => {
        const formData = new FormData();
        selectedFiles.forEach(file => {
            formData.append('files', file);
        });

        console.log("이미지 보냄");

        try {
            const response = await fetch(`${API_URL}/calculate-amount`, {
                method: 'POST',
                body: formData,
            });
            const data = await response.json();
            setAmount(data.amount);
        } catch (error) {
            console.error('Error uploading files:', error);
        }
    };

    const handleAddToCart = () => {
        alert(`세탁물이 추가되었습니다: 총 금액 ${amount} KRW`);
    };

    return (
        <div className="mx-auto mt-5 w-full px-5">
            <BackButton/>
            <h1 className="text-2xl font-bold mb-5">세탁 신청 상세</h1>

            <div className="mb-5">
                <label className="block font-semibold mb-2 text-gray-500">클릭이나 드래그로 사진을 업로드 해주세요!</label>
                <div
                    className="relative w-full h-64 mb-2 border rounded bg-gray-50 flex items-center justify-center overflow-hidden">
                    <div
                        className="absolute inset-0 z-0"
                        style={{
                            backgroundImage: `url(${uploadImg})`,
                            width: '50%',
                            transform: "scale(0.5)",
                            backgroundSize: 'cover',
                            margin: 'auto',
                            opacity: '0.4'
                        }}
                    ></div>
                    <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                        className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
                    />
                </div>
                <div className="relative z-20 text-center text-black font-bold">
                    {selectedFiles.length > 0 ? `${selectedFiles.length}개 파일 선택됨` : '선택된 파일 없음'}
                </div>
                <div className="mt-3 flex justify-center flex-wrap">
                    {selectedFiles.map((file, index) => (
                        <img
                            key={index}
                            src={URL.createObjectURL(file)}
                            alt="laundry"
                            className="w-32 h-32 object-cover mr-2 mb-2"
                        />
                    ))}
                </div>
            </div>

            <div className="mb-5">
                <button onClick={uploadFilesAndCalculateAmount}
                        className="w-full px-4 py-2 bg-blue-500 font-bold text-white rounded">
                금액 계산
                </button>
                {amount !== null && (
                    <div className="mt-3">
                        <p className="text-lg font-semibold">계산된 금액: {amount} 원</p>
                    </div>
                )}
            </div>

            <button onClick={handleAddToCart} className="w-full px-4 py-2 bg-gray-600 font-bold text-white rounded mb-10">
                세탁물 추가하기
            </button>
        </div>
    );
};

export default ImgApplication;
