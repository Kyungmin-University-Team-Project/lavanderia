import React, { useEffect, useState } from 'react';
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";


interface Item {
    id: number;
    name: string;
    description: string;
    price: number;
    city: string;
    region: string;
    subregion: string;
    imgUrl: string;
}

const SecondhandBuy = () => {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const [item, setItem] = useState<Item | null>(null);
    const [selectedMethod, setSelectedMethod] = useState<string>('택배로 받기');

    useEffect(() => {
        const fetchItem = async () => {
            if (id) {
                try {
                    const response = await axios.get('/mock/usdTrade.json');
                    const foundItem = response.data.find((item: Item) => item.id === parseInt(id, 10));
                    setItem(foundItem || null);
                } catch (error) {
                    console.error("Error fetching item:", error);
                }
            }
        };
        fetchItem();
    }, [id]);

    if (!item) {
        return <div>Loading...</div>;
    }

    const getImageUrl = (imageUrl: string) => {
        try {
            return require(`../../Assets/Img/home/useTrade/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    const handleMethodChange = (method: string) => {
        setSelectedMethod(method);
    };

    const handlePaymentCLick = () => {
        navigate(`/secondhand/${id}/secondPayment`, { state: { item, selectedMethod } });
    }

    return (
        <div className="p-4">
            <h3 className="text-xl mb-4 font-bold">거래방법 선택하기</h3>
            <div className="flex items-center mb-4">
                <img src={getImageUrl(item.imgUrl)} alt={item.name} className="w-24 h-24 object-cover mr-4"/>
                <div className="flex flex-col">
                    <h2 className="text-xl font-bold">{item.price.toLocaleString()}원</h2>
                    <p className="text-sm">{item.name}</p>
                </div>
            </div>
            <div className="border-t pt-4">
                <div className="flex flex-col mb-4">
                    <button
                        onClick={() => handleMethodChange('택배로 받기')}
                        className={`rounded flex justify-between items-center p-4 text-lg border mb-2 ${selectedMethod === '택배로 받기' ? 'border-red-500' : 'hover:border-red-500'}`}
                    >
                        택배로 받기 <span className="text-red-500">무료</span>
                    </button>
                    <button
                        onClick={() => handleMethodChange('직접 만나서 받기')}
                        className={`rounded flex justify-between items-center p-4 text-lg border mb-2 ${selectedMethod === '직접 만나서 받기' ? 'border-red-500' : 'hover:border-red-500'}`}
                    >
                        직접 만나서 받기
                    </button>
                </div>
                <div className="mt-20   lg:mt-40 md:mt-96">
                    <p className='text-gray-500 text-sm mb-1'>예상금액</p>
                    <h2 className="text-xl font-bold mb-1">{item.price.toLocaleString()}원</h2>
                    <p className='text-gray-500 text-sm mb-2'>배송비 무료</p>
                </div>
                <button className="bg-red-500 text-white  p-2 w-full text-lg" onClick={handlePaymentCLick}>다음</button>
            </div>
        </div>
    );
};

export default SecondhandBuy;
