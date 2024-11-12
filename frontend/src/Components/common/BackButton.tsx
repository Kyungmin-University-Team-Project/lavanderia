import React from 'react';
import {FaArrowLeft} from "react-icons/fa";
import {useNavigate} from "react-router-dom";

const BackButton = () => {
    const navigate = useNavigate(); // navigate 함수 생성

    const handleBackClick = () => {
        navigate(-1); // 뒤로 가기 기능
    };

    return (
        <div className="flex justify-between items-center mb-4">
            <button onClick={handleBackClick} className="text-gray-600 text-xl cursor-pointer">
                <FaArrowLeft/>
            </button>
        </div>
    );
};

export default BackButton;