import React from 'react';

interface CardComponentProps {
    imgSrc: string;
    title: string;
    description: string;
    isReversed?: boolean;
}

const CardStepsData: React.FC<CardComponentProps> = ({title, description, imgSrc}) => {
    return (
        <div>
            <div className='mr-4 p-4'>
                <div>
                    <img
                        src={imgSrc}
                        alt="Home" className="mb-4 sm:w-64 w-96"/>
                </div>
                <div className='text-center sm:text-left'>
                    <p className="mb-4 border-b-4 border-blue-500 sm:w-64"></p>
                    <h4 className="mb-2 text-2xl">{title}</h4>
                    <p className="text-sm text-gray-500">{description}</p>
                </div>
            </div>
        </div>
    );
};

export default CardStepsData;