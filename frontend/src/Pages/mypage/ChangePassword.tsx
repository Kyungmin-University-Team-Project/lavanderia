import React, { useState } from 'react';
import axiosInstance from "../../Utils/axios/axiosInstance";

interface MemberPwd {
    oldPassword: string;
    newPassword: string;
}

const ChangePassword = () => {
    const [password, setPassword] = useState<MemberPwd>({
        oldPassword: "",
        newPassword: "",
    });
    const [passwordConfirm, setPasswordConfirm] = useState('');
    const [isPasswordValid, setIsPasswordValid] = useState(false);
    const [isConfirmValid, setIsConfirmValid] = useState(false);
    const [passwordConfirmMessage, setPasswordConfirmMessage] = useState('');

    // 비밀번호 유효성 검사 함수
    const handleChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,30}$/;
        const { name, value } = e.target;

        setPassword((prev) => ({
            ...prev,
            [name]: value,
        }));

        // Check if the password is valid based on the regex
        if (name === "oldPassword" || name === "newPassword") {
            if (!passwordRegex.test(value)) {
                setIsPasswordValid(false);
            } else {
                setIsPasswordValid(true);
            }
        }
    };

    // 비밀번호 확인란 체크 함수
    const handleChangePasswordConfirm = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputPasswordConfirm = e.target.value;
        setPasswordConfirm(inputPasswordConfirm);

        // Check if newPassword and passwordConfirm match
        if (password.newPassword === inputPasswordConfirm) {
            setPasswordConfirmMessage('비밀번호를 똑같이 입력했어요.');
            setIsConfirmValid(true);
        } else {
            setPasswordConfirmMessage('비밀번호가 일치하지 않아요.');
            setIsConfirmValid(false);
        }
    };

    // 비밀번호 변경 제출 함수
    const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (isPasswordValid && isConfirmValid) {
            try {
                const response = await axiosInstance.post('/member-pwd', password);
                console.log(response.data);
            } catch (e) {
                console.log(e);
            }
        }
    };

    return (
        <div className="flex flex-col min-h-full justify-between">
            <form onSubmit={handleOnSubmit}>
                <div className="flex-grow p-2">
                    <div className="mb-2">
                        <input
                            type="password"
                            value={password.oldPassword}
                            name="oldPassword"
                            placeholder="비밀번호 입력"
                            className="w-full p-2 border border-red-500 rounded"
                            onChange={handleChangePassword}
                            required
                        />
                    </div>

                    <div className="mb-2">
                        <input
                            type="password"
                            name="newPassword"
                            value={password.newPassword}
                            placeholder="새 비밀번호 입력"
                            onChange={handleChangePassword}
                            className="w-full p-2 border border-gray-300 rounded"
                            required
                        />
                        <p className="text-gray-500 text-sm mt-1">8~30자 이내로 입력해주세요</p>
                    </div>

                    <div className="mb-2">
                        <input
                            type="password"
                            name="newPasswordConfirm"
                            value={passwordConfirm}
                            placeholder="새 비밀번호 확인"
                            onChange={handleChangePasswordConfirm}
                            className="w-full p-2 border border-gray-300 rounded"
                            required
                        />
                        <p className="text-red-500 text-sm mt-1">{passwordConfirmMessage}</p>
                    </div>
                </div>

                {/* 완료 버튼 */}
                <div className="p-2">
                    <button
                        type="submit"
                        disabled={!isPasswordValid || !isConfirmValid}
                        className={`w-full py-2 rounded text-white transition-colors ${isPasswordValid && isConfirmValid ? 'bg-yellow-400 hover:bg-black' : 'bg-gray-300 cursor-not-allowed'}`}
                    >
                        완료
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ChangePassword;
