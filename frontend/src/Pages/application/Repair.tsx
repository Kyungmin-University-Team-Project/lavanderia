import React from 'react';
import {FaChevronRight} from 'react-icons/fa';
import {useNavigate} from 'react-router-dom';
import BackButton from "../../Components/common/BackButton";

// 수선 카테고리 데이터
const repairCategories = [
    {name: '셔츠, 블라우스', link: '/repair/shirt'},
    {name: '맨투맨', link: '/repair/sweatshirt'},
    {name: '니트, 스웨터, 가디건', link: '/repair/knit'},
    {name: '원피스', link: '/repair/dress'},
    {name: '바지', link: '/repair/pants'},
    {name: '스커트', link: '/repair/skirts'},
];

const Repair = () => {
    const navigate = useNavigate();

// handleItemClick
    const handleItemClick = (link: string) => {
        navigate(link);
    };

    return (
        <div className="p-6">
            <BackButton/>

            {/* Title */}
            <h2 className="text-2xl font-bold mb-4">어떤 옷을 수선 하시겠어요?</h2>

            {/* Category List */}
            <ul className="space-y-2">
                {repairCategories.map((item, index) => (
                    <li
                        key={index}
                        className="flex justify-between items-center border-b py-3 cursor-pointer"
                        onClick={() => handleItemClick(item.link)}
                    >
                        <span className="text-gray-800">{item.name}</span>
                        <FaChevronRight className="text-gray-400 text-lg"/>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Repair;
