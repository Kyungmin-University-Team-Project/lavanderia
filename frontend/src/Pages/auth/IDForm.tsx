import React from 'react';

const IDForm = () => {
    return (
        <>
            <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">
                    이름
                </label>
                <input
                    type="text"
                    id="name"
                    placeholder="이름"
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
            </div>
            <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
                    이메일
                </label>
                <div className="flex w-full">
                    <input
                        type="email"
                        id="email"
                        placeholder="이메일"
                        className="shadow appearance-none border rounded w-3/4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                    <button className="ml-2 w-1/4 px-2 py-2 bg-blue-500 text-white rounded">인증 요청</button>
                </div>
            </div>
            <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="verification">
                    본인인증
                </label>
                <input
                    type="text"
                    id="verification"
                    placeholder="본인인증"
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
            </div>
            <div className="flex justify-center">
                <button className="px-4 py-2 bg-blue-500 text-white rounded">아이디 찾기</button>
            </div>
        </>
    );
};

export default IDForm;
