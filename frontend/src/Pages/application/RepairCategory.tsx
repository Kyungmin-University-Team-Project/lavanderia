import React, {useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import BackButton from "../../Components/common/BackButton";

interface RepairData {
    [key: string]: {
        tags: string[];
        imagePaths: string[];
    };
}

const repairData: RepairData = {
    shirt: {
        tags: ["소매/팔 수선", "어깨/품 수선", "기장 수선", "재박음질/누빔", "단추/후크 수선", "올뜯김 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/shirt_${i + 1}.webp`)
    },
    sweatshirt: {
        tags: ["소매/팔 수선", "어깨/품 수선", "기장 수선", "시보리 수선", "재박음질/누빔", "단추/후크 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/sweatshirt_${i + 1}.webp`)
    },
    knit: {
        tags: ["소매/팔 수선", "어깨/품 수선", "기장 수선", "재박음질/누빔", "단추/후크 수선", "올뜯김 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/knit_${i + 1}.webp`)
    },
    dress: {
        tags: ["소매/팔 수선", "어깨/품 수선", "기장 수선", "시보리 수선", "재박음질/누빔", "단추/후크 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/dress_${i + 1}.webp`)
    },
    pants: {
        tags: ["기장 수선", "통 수선", "허리/힙 수선", "재박음질/누빔", "단추/후크 수선", "지퍼 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/pants_${i + 1}.webp`)
    },
    skirts: {
        tags: ["기장 수선", "통 수선", "허리/힙 수선", "재박음질/누빔", "단추/후크 수선", "올뜯김 수선"],
        imagePaths: Array.from({length: 6}, (_, i) => `${process.env.PUBLIC_URL}/img/repair/skirts_${i + 1}.webp`)
    },
};

const RepairCategory: React.FC = () => {
    const {category} = useParams<{ category: string }>();
    const navigate = useNavigate();

    const currentData = category ? repairData[category] : {tags: [], imagePaths: []};
    const [loadedImages, setLoadedImages] = useState<boolean[]>(Array(currentData.imagePaths.length).fill(false));

    const handleImageLoad = (index: number) => {
        setLoadedImages((prevLoadedImages) => {
            const updatedLoadedImages = [...prevLoadedImages];
            updatedLoadedImages[index] = true;
            return updatedLoadedImages;
        });
    };

    const handleItemClick = (tag: string) => {
        navigate(`/repair/${category}/detail`, {state: {category, tag}});
    };

    return (
        <div className="p-6">
            <h2 className="text-2xl font-bold mb-4">어떻게 수선할까요?</h2>

            <div className="my-4 grid grid-cols-3 gap-4">
                {currentData.imagePaths.map((path: string, index: number) => (
                    <div key={index} className="text-center cursor-pointer"
                         onClick={() => handleItemClick(currentData.tags[index])}>
                        {!loadedImages[index] && (
                            <div className="w-40 h-40 max-w-xs mx-auto rounded-2xl bg-gray-200 animate-pulse"></div>
                        )}
                        <img
                            src={path}
                            alt={`${category} 이미지 ${index + 1}`}
                            className={`w-40 h-40 max-w-xs mx-auto rounded-2xl border-[1px] transition-opacity duration-300 ${
                                loadedImages[index] ? 'block' : 'hidden'
                            }`}
                            onLoad={() => handleImageLoad(index)}
                        />
                        <span className="text-sm text-gray-600 mt-4 block">
                            {currentData.tags[index] || "기타 수선"}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default RepairCategory;
