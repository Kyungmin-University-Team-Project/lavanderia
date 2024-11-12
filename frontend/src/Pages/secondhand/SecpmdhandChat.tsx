import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import sponge from "../../Assets/sponge.jpg"; // Make sure the path is correct

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

const SecpmdhandChat: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [item, setItem] = useState<Item | null>(null);

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

    return (
        <div className="flex flex-col p-4 bg-gray-100">
            <div className="flex items-center mb-4">
                <img src={sponge} alt="avatar" className="w-10 h-10 rounded-full mr-2"/>
                <div className="flex flex-col">
                    <span className="font-bold">스위티</span>
                    <span className="text-xs text-gray-500">하루톡</span>
                </div>
            </div>
            <div className="flex-grow overflow-y-auto bg-white rounded-lg shadow-inner">
                <div className="">
                    <div className="text-sm text-gray-700 flex items-start">
                        <img src={sponge} alt="avatar" className="w-6 h-6 rounded-full mr-2"/>
                        <p className="bg-gray-200 rounded-lg p-2">
                            안녕하세요. 스위티에요.
                            <br/>
                            만나서 반가워요~
                            <br/>
                            궁금하신 점은 무엇이든지 물어보세요!
                        </p>
                    </div>
                </div>
                <div className="mb-4 flex justify-end">
                <p className="bg-red-200 rounded-lg p-2">
                    제품문의 드려요~~
                </p>
                </div>
                <div className="mb-4 flex justify-end">
                    <div className="text-sm text-gray-700 flex items-start">
                        <img src={getImageUrl(item.imgUrl)} alt={item.name}
                             className="w-20 h-20 object-cover mr-2"/>
                        <div>
                            <p className="text-sm font-semibold">{item.name}</p>
                            <p className="text-xs text-gray-500">2016.05.09</p>
                            <p className="text-xs text-red-500">결제완료</p>
                        </div>
                    </div>
                </div>
                <div className="mb-2">
                    <p className="text-sm text-gray-700 flex items-start">
                        <img src={sponge} alt="avatar" className="w-6 h-6 rounded-full mr-2"/>
                        <div className="bg-gray-200 rounded-lg p-2">무엇이 궁금하신가요?</div>
                    </p>
                </div>
            </div>
            <div className="flex items-center border-t p-2 bg-white">
                <input
                    type="text"
                    placeholder="무엇이든 물어보세요."
                    className="flex-grow p-2 border rounded-lg mr-2"
                />
                <button className="bg-blue-500 text-white rounded-lg p-2">전송</button>
            </div>
        </div>
    );
};

export default SecpmdhandChat;
