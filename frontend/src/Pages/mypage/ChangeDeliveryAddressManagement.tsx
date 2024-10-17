import React, { useState, useEffect } from 'react';
import Postcode from "../../Components/postcode/Postcode";

const ChangeDeliveryAddressManagement = () => {
    const [password, setPassword] = useState('');
    const [address, setAddress] = useState('');
    const [isPostcodeOpen, setIsPostcodeOpen] = useState(false);
    const [textModal, setTextModal] = useState(false);
    const [selectedOption, setSelectedOption] = useState('');
    const [customRequest, setCustomRequest] = useState(''); // 사용자 요청사항을 위한 상태 추가

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
    };

    const handleAddressClick = () => {
        setIsPostcodeOpen(true);
    };

    const handleAddressComplete = (selectedAddress: string) => {
        setAddress(selectedAddress);
        setIsPostcodeOpen(false);
    };

    const handleModalClick = (event: React.MouseEvent<HTMLDivElement>) => {
        if (event.target === event.currentTarget) {
            setIsPostcodeOpen(false);
        }
    };

    const handleOptionChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const value = e.target.value;
        setSelectedOption(value);

        // "직접입력"이 선택된 경우 모달 열기
        if (value === 'option5') {
            setTextModal(true);
        } else {
            setTextModal(false); // 다른 옵션 선택 시 텍스트 모달 닫기
        }
    };

    const handleTextChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setCustomRequest(e.target.value); // 텍스트 입력 상태 업데이트
    };

    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.key === 'Escape') {
                setIsPostcodeOpen(false);
            }
        };

        if (isPostcodeOpen) {
            document.addEventListener('keydown', handleKeyDown);
        }

        // Cleanup 함수: 컴포넌트가 언마운트되거나 모달이 닫힐 때 이벤트 리스너 제거
        return () => {
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, [isPostcodeOpen]);

    return (
        <section className="min-h-full flex flex-col justify-between">
            <h6 className="font-semibold mt-4 mb-2 p-2 ">
                배송지 수정
            </h6>
            <form className="p-2 relative flex-grow flex flex-col justify-between">
                <label>
                    <p className='mb-2'>이름</p>
                    <input
                        className="w-full mb-4 p-1 border border-red-500 rounded"
                        placeholder="받는 분의 이름을 입력해주세요"
                        value={password}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    <p className='mb-2'>휴대폰번호</p>
                    <input
                        className="w-full mb-4 p-1 border border-red-500 rounded"
                        placeholder="받는 분의 휴대폰번호를 입력해주세요"
                        value={password}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    <p className='mb-2'>주소</p>
                    <input
                        className="w-full mb-4 p-1 border border-red-500 rounded"
                        placeholder="받는 분의 주소를 입력해주세요."
                        onClick={handleAddressClick}
                        value={address}
                    />
                    <input
                        className="w-full mb-4 p-1 border border-red-500 rounded"
                        placeholder="부조수를 입력해주세요"
                    />
                    {isPostcodeOpen && (
                        <div
                            className="fixed inset-0 z-50 flex items-center justify-center bg-gray-900 bg-opacity-50 px-5 lg:px-0"
                            onClick={handleModalClick}
                        >
                            <div className="w-full max-w-md rounded-lg bg-white p-4">
                                <Postcode onComplete={handleAddressComplete}/>
                            </div>
                        </div>
                    )}
                </label>
                <label>
                    <p className='mb-2'>배송 요청사항(선택)</p>
                    <select
                        onChange={handleOptionChange}
                        value={selectedOption}
                        className="w-full mb-4 p-1 border border-red-500 rounded"
                    >
                        <option value="" disabled>
                            배송 요청사항을 선택해주세요
                        </option>
                        <option value="option1">문 앞에 놔주세요</option>
                        <option value="option2">경비실에 맡겨주세요</option>
                        <option value="option3">택배함에 넣어주세요</option>
                        <option value="option4">배송 전에 연락 주세요</option>
                        <option value="option5">직접입력</option>
                    </select>
                    {textModal && (
                        <textarea
                            className="w-full p-1 border border-red-500 rounded"
                            placeholder="배송 요청사항을 입력하세요"
                            value={customRequest}
                            onChange={handleTextChange} // 텍스트 입력 핸들러 추가
                        />
                    )}
                </label>
                <label>
                    <input
                        type="checkbox"
                        className='bg-gray-300 rounded focus:bg-gray-400 outline-0'
                    />
                    <span className='text-xs inline-block ml-2 text-gray-400'>기본 배송지로 설정</span>
                </label>
                <div className='mt-16'>

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

export default ChangeDeliveryAddressManagement;
