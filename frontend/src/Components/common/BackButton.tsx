import React from 'react';
import {useNavigate} from "react-router-dom";
import {IoChevronBack} from "react-icons/io5";

const BackButton = () => {
    const navigate = useNavigate(); // navigate 함수 생성

    const handleBackClick = () => {
        navigate(-1); // 뒤로 가기 기능
    };

    return (
        <button onClick={handleBackClick} className="text-black text-2xl cursor-pointer">
            <IoChevronBack />
        </button>
    );
};

export default BackButton;