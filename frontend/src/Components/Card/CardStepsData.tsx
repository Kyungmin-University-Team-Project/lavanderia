import React from 'react'

interface CardComponentProps {
  imgSrc: string
  title: string
  description: string
  isReversed?: boolean
}

const CardStepsData: React.FC<CardComponentProps> = ({
  title,
  description,
  imgSrc,
}) => {
  return (
    <div className="mb-16 items-center  p-4 text-center ">
      <img
        src={imgSrc}
        alt={title}
        className="mb-4 w-full object-cover sm:w-auto md:h-96  md:w-full"
      />
      <div className="w-full  md:w-full">
        <p className="mb-4 w-full border-b-4 border-blue-500 "></p>
        <h4 className="mb-2 text-left text-2xl">{title}</h4>
        <p className="text-left text-gray-500 md:text-2xl lg:text-2xl 2xl:text-xl">
          {description}
        </p>
      </div>
    </div>
  )
}

export default CardStepsData
