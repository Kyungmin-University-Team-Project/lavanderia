import React, {useState, useEffect} from 'react';
import {FaHeart, FaRegHeart, FaComment, FaShare, FaBookmark, FaRegBookmark} from 'react-icons/fa';

const ActionBar = () => {
    const [liked, setLiked] = useState(false);
    const [saved, setSaved] = useState(false);
    const [isAnimating, setIsAnimating] = useState(false);

    const onLike = (e: React.MouseEvent) => {
        e.stopPropagation(); // 이벤트 버블링 방지
        setLiked(!liked);
        setIsAnimating(true);
        console.log("Liked:", !liked);
    };


    // TODO: 코멘트 아이콘 클릭시 이동 이벤트를 어떻게 처리할지 생각하기
    const onComment = (e: React.MouseEvent) => {
        e.stopPropagation(); // 이벤트 버블링 방지
        console.log("Comment clicked");
    };

    const onShare = (e: React.MouseEvent) => {
        e.stopPropagation(); // 이벤트 버블링 방지
        console.log("Share clicked");
    };

    const onSave = (e: React.MouseEvent) => {
        e.stopPropagation(); // 이벤트 버블링 방지
        setSaved(!saved);
        console.log("Saved:", !saved);
    };

    useEffect(() => {
        if (isAnimating) {
            const timer = setTimeout(() => setIsAnimating(false), 50);
            return () => clearTimeout(timer);
        }
    }, [isAnimating]);

    return (
        <div className="flex items-center justify-between py-2">
            <div className="flex space-x-4">
                <button onClick={onLike} className="text-2xl focus:outline-none">
                    {liked ? (
                        <FaHeart
                            className={`text-red-500 transition-transform duration-200 ${
                                isAnimating ? 'transform scale-125' : 'transform scale-100'
                            }`}
                        />
                    ) : (
                        <FaRegHeart className={`text-gray-600 transition-transform duration-200 ${
                            isAnimating ? 'transform scale-125' : 'transform scale-100'
                        }`}/>
                    )}
                </button>
                <button onClick={onComment} className="text-2xl focus:outline-none">
                    <FaComment className="text-gray-600 hover:text-gray-800"/>
                </button>
                <button onClick={onShare} className="text-2xl focus:outline-none">
                    <FaShare className="text-gray-600 hover:text-gray-800"/>
                </button>
            </div>

            <button onClick={onSave} className="text-2xl focus:outline-none">
                {saved ? <FaBookmark className="text-black"/> :
                    <FaRegBookmark className="text-gray-600 hover:text-gray-800"/>}
            </button>
        </div>
    );
};

export default ActionBar;
