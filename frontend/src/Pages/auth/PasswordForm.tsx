import React from 'react';

const PasswordForm = () => {
    return (
        <>
            <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">
                    비밀번호를 찾으려는 아이디
                </label>
                <input
                    type="text"
                    id="username"
                    placeholder="아이디"
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
            </div>
            <div className="flex justify-center">
                <button className="px-4 py-2 bg-blue-500 text-white rounded">다음</button>
            </div>
        </>
    );
};

export default PasswordForm;
