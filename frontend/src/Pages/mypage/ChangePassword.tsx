import React, { useState } from 'react';

const ChangePassword = () => {
    const [password, setPassword] = useState('');
    const [passwordConfirm, setPasswordConfirm] = useState('');
    const [isPasswordValid, setIsPasswordValid] = useState(false);
    const [isConfirmValid, setIsConfirmValid] = useState(false);
    const [passwordConfirmMessage, setPasswordConfirmMessage] = useState('');

    // 비밀번호 유효성 검사 함수
    const handleChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,30}$/;
        const inputPassword = e.target.value;
        setPassword(inputPassword);

        if (!passwordRegex.test(inputPassword)) {
            setIsPasswordValid(false);
        } else {
            setIsPasswordValid(true);
        }
    };

    // 비밀번호 확인란 체크 함수
    const handleChangePasswordConfirm = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputPasswordConfirm = e.target.value;
        setPasswordConfirm(inputPasswordConfirm);

        if (password === inputPasswordConfirm) {
            setPasswordConfirmMessage('비밀번호를 똑같이 입력했어요.');
            setIsConfirmValid(true);
        } else {
            setPasswordConfirmMessage('비밀번호가 일치하지 않아요.');
            setIsConfirmValid(false);
        }
    };

    return (
        <div className="flex flex-col min-h-full justify-between">
            <div className="flex-grow p-2">
                <div className="mb-2">
                    <input
                        type="password"
                        placeholder="비밀번호 입력"
                        className="w-full p-2 border border-red-500 rounded"
                        required
                    />
                </div>

                <div className="mb-2">
                    <input
                        type="password"
                        placeholder="새 비밀번호 입력"
                        value={password}
                        onChange={handleChangePassword}
                        className="w-full p-2 border border-gray-300 rounded"
                    />
                    <p className="text-gray-500 text-sm mt-1">8~30자 이내로 입력해주세요</p>
                </div>

                <div className="mb-2">
                    <input
                        type="password"
                        placeholder="새 비밀번호 확인"
                        value={passwordConfirm}
                        onChange={handleChangePasswordConfirm}
                        className="w-full p-2 border border-gray-300 rounded"
                    />
                    <p className="text-red-500 text-sm mt-1">{passwordConfirmMessage}</p>
                </div>
            </div>

            {/* 완료 버튼 */}
            <div className="p-2">
                <button
                    type="submit"
                    disabled={!isPasswordValid || !isConfirmValid}
                    className={`w-full py-2 rounded text-white transition-colors ${
                        isPasswordValid && isConfirmValid
                            ? 'bg-yellow-400 hover:bg-yellow-500'
                            : 'bg-gray-300 cursor-not-allowed'
                    }`}
                >
                    완료
                </button>
            </div>
        </div>
    );
};

export default ChangePassword;
