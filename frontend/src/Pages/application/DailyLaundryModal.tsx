import React, {useState} from 'react';
import emptyBasket from '../../Assets/Img/application/empty_laundry_basket.png';
import fullBasket from '../../Assets/Img/application/full_laundry_basket.png';

// TODO: 타입 모듈화 하기
const DailyLaundryModal = ({service, closeModal}: any) => {
    const [selectedOption, setSelectedOption] = useState<string | null>(null);

    // 각 버튼 클릭 시 처리 함수
    const handleButton1Click = () => {
        setSelectedOption("3.5KG 미만");

    };

    const handleButton2Click = () => {
        setSelectedOption("3.5KG 이상");

    };

    // 확인 버튼 클릭 시 처리 함수
    const handleConfirmClick = () => {
        if (selectedOption) {

            closeModal();
        }
    };

    return (
        <div
            className="fixed z-[999] top-0 left-0 w-full h-full bg-black bg-opacity-30"
            onClick={closeModal}
        >
            <div
                className="flex flex-col justify-between fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[550px] h-[500px] rounded-lg bg-white shadow-2xl p-5"
                onClick={(event) => event.stopPropagation()} // 이벤트 전파 중지
            >
                <h2 className="text-center text-xl font-bold mb-4">선택된 상품을 추가합니다</h2>

                <div className="flex w-full h-full mb-4 gap-2">
                    <button
                        className={`w-1/2 flex flex-col items-center justify-center p-2 border rounded-lg transition ${
                            selectedOption === "3.5KG 미만"
                                ? "border-blue-600 bg-blue-100"
                                : "border-gray-300 hover:border-blue-600 hover:bg-blue-50"
                        }`}
                        onClick={handleButton1Click}
                    >
                        <img src={emptyBasket} alt="Empty Basket" className="w-16 h-16 mb-2"/>
                        <span className="text-black font-bold">3.5KG 미만</span>
                        <span className="text-gray-400">3.5KG 까지 13,500원</span>
                    </button>
                    <button
                        className={`w-1/2 flex flex-col items-center justify-center p-2 border rounded-lg transition ${
                            selectedOption === "3.5KG 이상"
                                ? "border-blue-600 bg-blue-100"
                                : "border-gray-300 hover:border-blue-600 hover:bg-blue-50"
                        }`}
                        onClick={handleButton2Click}
                    >
                        <img src={fullBasket} alt="Full Basket" className="w-16 h-16 mb-2"/>
                        <span className="text-black font-bold">3.5KG 이상</span>
                        <span className="text-gray-400">초과 0.5KG당 1,500원 추가</span>
                    </button>
                </div>

                {/* 하단 설명과 확인/닫기 버튼 */}
                <div className="flex flex-col items-center justify-end">
                    <p className="text-center text-gray-500 mb-5 font-bold">
                        주문 목록에 추가하려면 확인 버튼을 눌러주세요.
                    </p>
                    <div className="flex gap-2">
                        <button
                            className={`px-4 py-2 rounded-md text-white transition ${
                                selectedOption
                                    ? "bg-blue-500 hover:bg-blue-600"
                                    : "bg-blue-300 cursor-not-allowed"
                            }`}
                            onClick={handleConfirmClick}
                            disabled={!selectedOption} // 옵션이 선택되지 않으면 비활성화
                        >
                            확인
                        </button>
                        <button
                            className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
                            onClick={closeModal} // 닫기 버튼 클릭 시 모달 닫기
                        >
                            닫기
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DailyLaundryModal;
