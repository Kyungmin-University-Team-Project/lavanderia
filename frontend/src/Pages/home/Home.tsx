import React, {useEffect,  useState} from 'react';
import { useSwipeable } from 'react-swipeable';
import gitImage from "../../Assets/Img/home/af9e0e944df57d63d4eec5a60a89cfb2.gif";
import storyImage from "../../Assets/Img/home/story.png";
import CardComponent from "../../Components/Card/CardComponent";
import { Advertisement, CardsData, StepsData } from "../../Typings/Home/CardsData";
import CardStepsData from "../../Components/Card/CardStepsData";
import { FaAngleRight, FaAngleLeft } from "react-icons/fa6";

const Home: React.FC = () => {
    const [data, setData] = useState<{ cardsData: CardsData[]; stepsData: StepsData[]; advertisement: Advertisement[] }>({
        cardsData: [],
        stepsData: [],
        advertisement: [],
    });

    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    const handleNext = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex + 1) % data.advertisement.length);
    }

    const handlePrevious = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex - 1 + data.advertisement.length) % data.advertisement.length);
    }


    useEffect(() => {
        fetch('/mock/home.json')
            .then(response => response.json())
            .then(setData)
            .catch(error => console.error('Error fetching data:', error));
    }, []);

    const getAdvertisementImageUrl = (imageUrl: string) => {
        try {
            return require(`../../Assets/Img/home/advertisement/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    const getImageUrl = (imageUrl: string) => {
        try {
            return require(`../../Assets/Img/home/card/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    const getStepImageUrl = (imageUrl: string) => {
        try {
            return require(`../../Assets/Img/home/step/${imageUrl}`);
        } catch (error) {
            console.error('Error loading image:', error);
            return '';
        }
    };

    useEffect(() => {
        const interval = setInterval(handleNext, 5000);
        return () => clearInterval(interval);
    }, [data.advertisement.length]);

    const handlers = useSwipeable({
        onSwipeStart: handleNext,
        onSwipedRight: handlePrevious,
    });

    return (
        <section className="font-roboto m-auto">
            {/* 광고 */}
            <article className="relative">
                <div className="m-auto max-w-7xl" {...handlers}>
                    {data.advertisement.length > 0 && (
                        <div className="relative">
                            <div
                                className={`transition-transform duration-500  transform translate-x-0`}>
                                <img
                                    src={getAdvertisementImageUrl(data.advertisement[currentImageIndex].imgSrc)}
                                    alt="Advertisement"
                                    className="w-full object-cover"
                                />
                            </div>
                            <button
                                onClick={handlePrevious}
                                className="absolute left-2 top-2/4 transform -translate-y-1/2  text-white px-1 py-1 rounded-3xl"
                            >
                                <FaAngleLeft/>
                            </button>
                            <button
                                onClick={handleNext}
                                className="absolute right-2 top-1/2 transform -translate-y-1/2  text-white px-1 py-1 rounded-3xl"
                            >
                                <FaAngleRight/>
                            </button>
                        </div>
                    )}
                </div>
            </article>

            {/* 첫 번쨰 소개글 */}
            <article className="m-auto max-w-7xl">
                <div className="mt-20 sm:mt-20 pb-20">
                    <div className="sm:w-1/2 m-auto text-center sm:text-left sm:p-4">
                        <h4 className="mb-4 text-2xl text-center text-blue-500">D A I L Y W A S H</h4>
                        <h1 className="mb-4 text-2xl w-full text-center  sm:text-3xl">
                            귀찮은 세탁을 간편
                            <br/>
                            하게 모두를 위한
                            <br/>
                            편리한 세탁 서비스
                        </h1>
                    </div>
                    <img src={gitImage} alt="dailywash" className="h-90   p-4 rounded-3xl sm:w-full "/>
                </div>
            </article>

            {/* 두 번째 소개글 */}
            <article className="m-auto max-w-7xl">
                <div className="text-center mb-16 mt-16">
                    <h1 className="text-3xl text-blue-500">All In One</h1>
                    <h1 className="text-3xl">Laundry Service</h1>
                    <p className="mt-4 text-base text-gray-500">수거부터 세탁, 새벽까지 배달</p>
                </div>
                {/* 카드 */}
                <div className="grid grid-cols-1 gap-4">
                    {data.stepsData.map((step, index) => (
                        <CardStepsData
                            key={index}
                            title={step.title}
                            description={step.description}
                            imgSrc={getStepImageUrl(step.imgSrc)}
                        />
                    ))}
                </div>
            </article>
            {/* 세 번째 소개글 */}
            <article className="m-auto max-w-7xl">
                <div className="mt-32 flex flex-col  items-center justify-between">
                    {data.cardsData.map((card, index) => (
                        <CardComponent
                            key={index}
                            title={card.title}
                            description={card.description}
                            isReversed={card.isReversed}
                            imgSrc={getImageUrl(card.imgSrc)} // Use the dynamic image URL
                        />
                    ))}
                </div>
            </article>

            {/* 네 번쨰 소개글 */}
            <article className="m-auto max-w-7xl p-4">
                <div className="mt-10 sm:ml-4 sm:mr-4">
                    <h4 className="text-lg sm:text-3xl  sm:text-left leading-relaxed sm:leading-snug">
                        세탁으로 부터 찾는 나의 시간,
                        <br/>
                        이제 <strong className="font-bold">하루 세탁</strong>에 맡기세요
                    </h4>
                    <img src={storyImage} className="w-full mt-4 sm:mt-8 object-cover h-full" alt="Home"/>
                </div>
            </article>
        </section>
    );
};

export default Home;
