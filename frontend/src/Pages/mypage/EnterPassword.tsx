import React, {useState} from 'react';

const EnterPassword = () => {
    const [password, setPassword] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
    }
    return (
        <section className="min-h-full flex flex-col justify-between">
            <h4 className="text-xs  font-semibold mt-4 mb-4 p-2 ">
                정보를 안전하게 보호하기 위해 비밀번호를 다시 한 번 입력해주세요
            </h4>
            <form className="p-2 relative flex-grow flex flex-col justify-between">
                <label>
                    <input
                        type="password"
                        className="w-full mb-2 p-2 border border-red-500 rounded"
                        placeholder="비밀번호 입력"
                        value={password}
                        onChange={handleChange}
                    />
                </label>
                <div className='mb-16'>

                </div>
                <button
                    disabled={password.length <= 4}
                    className={`absolute bottom-0 w-full p-2 rounded text-white transition-all duration-300 
                        ${password.length > 4 ? 'bg-black hover:bg-black' : 'bg-gray-400 cursor-not-allowed'}`}
                >
                    완료
                </button>
            </form>
        </section>
    );
};

export default EnterPassword;
