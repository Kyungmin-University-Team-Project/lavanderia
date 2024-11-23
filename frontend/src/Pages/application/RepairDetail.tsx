import React, { useState, useEffect, FormEvent } from 'react'
import { useLocation } from 'react-router-dom';
import BackButton from "../../Components/common/BackButton";
import axiosInstance from '../../Utils/axios/axiosInstance'

interface DetailOption {
    type: string;
    price: number;
}


const RepairDetail: React.FC = () => {
    const location = useLocation();
    const { category, tag } = location.state;
    const [details, setDetails] = useState<DetailOption[]>([]);
    const [selectedOption, setSelectedOption] = useState<string>('');
    const [requestText, setRequestText] = useState<string>('');
    const [isButtonEnabled, setIsButtonEnabled] = useState<boolean>(false);

    // 장바구니 Add
    useEffect(() => {
        fetch('/mock/repairPrices.json')
            .then((response) => response.json())
            .then((data) => {
                const categoryDetails = data[category]?.[tag] || [];
                setDetails(categoryDetails);
            });
    }, [category, tag]);

    useEffect(() => {
        setIsButtonEnabled(selectedOption !== '' && requestText.trim() !== '');
    }, [selectedOption, requestText]);

    const handleOptionChange = (option: string) => {
        setSelectedOption(option);
    };

    const handleRequestChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setRequestText(e.target.value);
    };

  // clothesType(String): 옷 종류
  // howTo(String): 어떻게
  // detailInfo(String): 상세 정보
  // request(String): 요청 사항
  // price(number): 가격

    // TODO: Api 나오면 연결하기
  const handleButtonClick = async () => {
    const selectedDetail = details.find((item) => item.type === selectedOption);
    if (selectedDetail) {
      const updatedRepair = {
        clothesType: category,
        howTo: tag,
        request: selectedDetail.type,
        detailInfo: requestText,
        price: selectedDetail.price,
      };

      try {
        const response = await axiosInstance.post('/repair/add', updatedRepair);
        console.log(response)
      } catch (e) {
        console.log(e)
      }
    }
  };

    return (
        <div className="p-6">
            <BackButton />
            <h2 className="text-2xl font-bold mb-4">{tag} 상세 정보</h2>

            {/* 커스텀 라디오 버튼 목록 */}
            <ul className="space-y-4">
                {details.map((item, index) => (
                    <li
                        key={index}
                        className="flex justify-between items-center p-4 border rounded-lg cursor-pointer"
                        onClick={() => handleOptionChange(item.type)}
                    >
                        <label className="flex items-center cursor-pointer">
                            <span
                                className={`w-5 h-5 inline-flex items-center justify-center rounded-full border-2 mr-3 ${selectedOption === item.type ? 'border-black' : 'border-gray-300'}`}
                            >
                                {selectedOption === item.type ?
                                    <span className="w-2 h-2 bg-black rounded-full" /> :
                                    <span className="w-2 h-2 bg-gray-300 rounded-full" />}
                            </span>
                            <input
                                type="radio"
                                name="repairOption"
                                value={item.type}
                                checked={selectedOption === item.type}
                                onChange={() => handleOptionChange(item.type)}
                                className="hidden"
                            />
                            <span className="text-gray-700">{item.type}</span>
                        </label>
                        <span className="text-gray-500">{item.price.toLocaleString()}원</span>
                    </li>
                ))}
            </ul>

            <div className="mt-6">
                <h3 className="text-md font-semibold mb-2">수선 요청사항</h3>
                <textarea
                    className="w-full h-32 p-2 border-gray-200 rounded-md resize-none focus:outline-none"
                    placeholder="예) 소매기장을 3cm 줄여주세요."
                    value={requestText}
                    onChange={handleRequestChange}
                />
                <p className="text-xs text-gray-500 mt-2">수선 부위와 방법을 구체적으로 입력해주세요.</p>
            </div>

            <div className="mt-6">
                <button
                    onClick={handleButtonClick}
                    disabled={!isButtonEnabled}
                    className={`w-full py-2 px-4 rounded-lg font-semibold text-white ${isButtonEnabled ? 'bg-black hover:bg-gray-800' : 'bg-gray-300 cursor-not-allowed'}`}
                >
                    선택 완료
                </button>
            </div>
        </div>
    );
};

export default RepairDetail;
