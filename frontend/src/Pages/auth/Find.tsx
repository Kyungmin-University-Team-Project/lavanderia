import React, { useState } from 'react';
import { FaArrowLeft } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import IDForm from './IDForm';
import PasswordForm from './PasswordForm';

const Find = () => {
    const [isFindingID, setIsFindingID] = useState(true);
    const navigate = useNavigate();

    const backLogin = () => {
        navigate('/auth/login');
    };

    return (
        <div className="max-w-xl mx-auto h-screen bg-gray-100 flex flex-col justify-center items-center">
            <div className="w-full h-screen bg-white shadow-md rounded p-6">
                <div className="flex items-center mb-6">
                    <FaArrowLeft
                        className="text-gray-700 cursor-pointer"
                        onClick={backLogin}
                    />
                    <h2 className="ml-2 text-lg font-bold text-gray-700">
                        {isFindingID ? '아이디 찾기' : '비밀번호 찾기'}
                    </h2>
                </div>
                <div className="flex justify-center mb-6">
                    <button
                        className={`px-4 py-2 rounded ${isFindingID ? 'bg-gray-500 text-white' : 'bg-gray-200 text-gray-700'}`}
                        onClick={() => setIsFindingID(true)}
                    >
                        아이디 찾기
                    </button>
                    <button
                        className={`px-4 py-2 rounded ml-1 ${!isFindingID ? 'bg-gray-500 text-white' : 'bg-gray-200 text-gray-700'}`}
                        onClick={() => setIsFindingID(false)}
                    >
                        비밀번호 찾기
                    </button>
                </div>
                {isFindingID ? <IDForm /> : <PasswordForm />}
            </div>
        </div>
    );
};

export default Find;
