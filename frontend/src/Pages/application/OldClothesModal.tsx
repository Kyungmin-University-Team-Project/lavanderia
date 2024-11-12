import React, {useState} from 'react';
import clothes_collection_box from '../../Assets/Img/application/clothes_collection_box.png';

// TODO: 타입 모듈화 하기
const OldClothesModal = ({closeModal}: any) => {
    const [selectedOption, setSelectedOption] = useState<string | null>(null);

    const handleButton1Click = () => {
        setSelectedOption("담기");

    };

    // TODO: Api 나오면 연결
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
                className="flex flex-col justify-between fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[350px] h-[400px] rounded-lg bg-white shadow-2xl p-5"
                onClick={(event) => event.stopPropagation()} // 이벤트 전파 중지
            >
                <h2 className="text-center text-xl font-bold mb-4">선택된 상품을 추가합니다</h2>

                <div className="flex w-full h-full mb-4 gap-2">
                    <button
                        className={`w-full flex flex-col items-center justify-center p-2 border rounded-lg transition ${
                            selectedOption === "담기"
                                ? "border-blue-600 bg-blue-100"
                                : "border-gray-300 hover:border-blue-600 hover:bg-blue-50"
                        }`}
                        onClick={handleButton1Click}
                    >
                        <img src={clothes_collection_box} alt="Full Basket" className="w-16 h-16 mb-2"/>
                        <span className="text-gray-400">
                            다양한 옷들을 보내주세요! 하루세탁에서 검수후 기부를 진행할게요!
                        </span>
                    </button>
                </div>

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
                            disabled={!selectedOption}
                        >
                            확인
                        </button>
                        <button
                            className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
                            onClick={closeModal}
                        >
                            닫기
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OldClothesModal;
