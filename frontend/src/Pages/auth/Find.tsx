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
        <div className="min-h-screen bg-gray-50 flex items-center justify-center">
            <div className="w-screen h-screen bg-white shadow-lg rounded-lg p-6 md:p-8 lg:max-w-lg md:rounded-md md:shadow-md">
                {/* Header */}
                <div className="flex items-center mb-6">
                    <button
                        onClick={backLogin}
                        className="text-gray-600 hover:text-gray-800 focus:outline-none"
                    >
                        <FaArrowLeft size={20} />
                    </button>
                    <h2 className="ml-3 text-xl font-semibold text-gray-700 md:text-2xl">
                        {isFindingID ? '아이디 찾기' : '비밀번호 찾기'}
                    </h2>
                </div>

                {/* Tabs */}
                <div className="flex border-b mb-6">
                    <button
                        className={`w-1/2 py-2 text-sm font-medium ${
                            isFindingID
                                ? 'border-b-2 border-blue-500 text-blue-500'
                                : 'text-gray-500 hover:text-gray-700'
                        }`}
                        onClick={() => setIsFindingID(true)}
                    >
                        아이디 찾기
                    </button>
                    <button
                        className={`w-1/2 py-2 text-sm font-medium ${
                            !isFindingID
                                ? 'border-b-2 border-blue-500 text-blue-500'
                                : 'text-gray-500 hover:text-gray-700'
                        }`}
                        onClick={() => setIsFindingID(false)}
                    >
                        비밀번호 찾기
                    </button>
                </div>

                {/* Form */}
                <div>{isFindingID ? <IDForm /> : <PasswordForm />}</div>
            </div>
        </div>
    );
};

export default Find;
