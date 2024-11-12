import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useLocation, useParams } from 'react-router-dom';
import Postcode from "../../Components/postcode/Postcode";
import Modal from "./Modal";
import ImportPayment from "./ImportPayment";

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

interface LocationState {
    item: Item;
    selectedMethod: string;
}

const SecondPayment = () => {
    const { id } = useParams<{ id: string }>();
    const location = useLocation();
    const { item: itemFromState, selectedMethod } = location.state as LocationState;
    const [item, setItem] = useState<Item | null>(itemFromState || null);
    const [postcodeData, setPostcodeData] = useState<string>("경기도 동두천시 삼육사로1002번길 97 (지행동, 지행주공아파트1단지), 102동301호");
    const [isPostcodeVisible, setIsPostcodeVisible] = useState<boolean>(false);

    useEffect(() => {
        const fetchItem = async () => {
            if (!item && id) {
                try {
                    const response = await axios.get('/mock/usdTrade.json');
                    const foundItem = response.data.find((item: Item) => item.id === parseInt(id, 10));
                    setItem(foundItem || null);
                } catch (error) {
                    console.error('Error fetching item:', error);
                }
            }
        };
        fetchItem();
    }, [id, item]);

    const getImageUrl = (imageUrl: string | undefined) => {
        try {
            if (!imageUrl) {
                return '';
            }
            return require(`../../Assets/Img/home/useTrade/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    const handleComplete = (data: any) => {
        setPostcodeData(data.address);
        setIsPostcodeVisible(false);
    };

    const togglePostcodePopup = () => {
        setIsPostcodeVisible(!isPostcodeVisible);
    };

    return (
        <div className="container mx-auto p-4">
            <section>
                <h1 className="text-2xl font-bold mb-4">결제하기</h1>
                <article className="mb-6">
                    <div className="flex items-center mb-4">
                        <img src={item ? getImageUrl(item.imgUrl) : ''} alt={item?.name} className="w-24 h-24 object-cover mr-4"/>
                        <div>
                            <h4 className="text-lg font-semibold">{item?.price.toLocaleString()}원</h4>
                            <p className="text-gray-600">{item?.name}</p>
                        </div>
                    </div>
                    <div className="mb-4">
                        <p className="text-gray-600">거래 방법: {selectedMethod}</p>
                    </div>
                </article>

                <article className="mb-6">
                    <h3 className="text-xl font-bold mb-2">배송지</h3>
                    <div className="border p-4 rounded mb-4">
                        <h3 className="text-lg font-semibold mb-2">이관용</h3>
                        <p className="text-gray-600 mb-2">
                            {postcodeData}
                        </p>
                        <p className="text-gray-600">010-4338-6492</p>
                        <button className="text-blue-500 mt-2" onClick={togglePostcodePopup}>변경</button>
                    </div>
                </article>

                <article className="mb-6">
                    <h3 className="text-xl font-bold mb-2">거래 요청 사항</h3>
                    <p className="text-gray-600 mb-2">판매자에게 전달되는 요청사항이에요.</p>
                    <textarea className="w-full border p-2 rounded" placeholder="예) 포장 꼼꼼하게 부탁드려요" />
                </article>
            </section>

            <Modal isVisible={isPostcodeVisible} onClose={togglePostcodePopup}>
                <Postcode onComplete={handleComplete} />
            </Modal>

            <article>
                <h3>결제하기</h3>
                <div>
                    <ImportPayment/>
                </div>
            </article>
        </div>
    );
};

export default SecondPayment;
