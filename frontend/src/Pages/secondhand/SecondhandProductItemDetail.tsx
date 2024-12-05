import React, { useEffect, useState } from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import axios from 'axios';
import {FaLocationDot} from "react-icons/fa6";


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

const ItemDetail: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [item, setItem] = useState<Item | null>(null);
    const navigate = useNavigate();

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

    const handleBuyClick = () => {
        navigate(`/secondhand/${id}/secpmhandChat`);
    }

    const handleChatCLick = () => {
        navigate(`/secondhand/${id}/secondhandBuy`);
    }

    const getImageUrl = (imageUrl: string) => {
        try {
            return require(`../../Assets/Img/home/useTrade/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    if (!item) {
        return <div>Loading...</div>;
    }

    return (
        <div className='max-w-2xl mx-auto p-4'>
            <h1 className="flex items-center mb-4">
                <FaLocationDot className="text-red-600 mr-2"/> {item.region} {item.city} {item.subregion}
            </h1>
            <img src={getImageUrl(item.imgUrl)} alt={item.name} className='p-6 w-full object-cover border mb-2'/>
            <h2 className='text-2xl font-semibold mt-4'>{item.name}</h2>
            <p className='text-base mt-1'>{item.description}</p>
            <p className='text-lg font-bold mb-1'>{item.price.toLocaleString()}원</p>
            <p>
                년식 2020년 6월 입니다. 현재도 사용중 인데 전혀 문제 없고요 앞,뒤 타이어 교체 했습니다. 킬로수 현재 5289인데 계속 올라갈예정 입니다.
                사용감 많으니 막 타실분만 부탁드립니다. 슬슬 자전거랑 이별할라고 정리 합니다!.
            </p>
            <p className='text-sm text-gray-500 mt-1'>관심 77 · 채팅 84</p>
            <div className='flex items-center justify-between mt-1'>
                <button
                    className='mt-4 bg-red-500 text-white  w-11/12 font-bold py-2 px-4 rounded'
                    onClick={handleChatCLick}
                >
                    구매하기
                </button>

                <button
                    className='mt-4 bg-amber-500 text-white font-bold py-2 px-4 rounded w-11/12  ml-4'
                    onClick={handleBuyClick}>
                    하루톡
                </button>
            </div>
        </div>
    );
};

export default ItemDetail;
