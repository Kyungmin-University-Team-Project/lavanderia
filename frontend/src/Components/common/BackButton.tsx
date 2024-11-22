import React from 'react';
import {FiArrowLeft} from "react-icons/fi";
import {useNavigate} from "react-router-dom";

const BackButton = () => {
    const navigate = useNavigate(); // navigate 함수 생성

    const handleBackClick = () => {
        navigate(-1); // 뒤로 가기 기능
    };

    return (
        <button onClick={handleBackClick} className="text-black text-2xl cursor-pointer">
            <FiArrowLeft/>
        </button>
    );
};

export default BackButton;