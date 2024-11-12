import React from 'react'

interface CardComponentProps {
  imgSrc: string
  title: string
  description: string
  isReversed?: boolean
}

const CardComponent: React.FC<CardComponentProps> = ({
  imgSrc,
  title,
  description,
}) => {
  const formattedDescription = description.split('\n').map((line, index) => (
    <React.Fragment key={index}>
      {line}
      <br />
    </React.Fragment>
  ))

  return (
    <div
      className={`mb-16 flex w-full flex-col items-center justify-between p-4 sm:flex-row `}
    >
      <div>
        <img src={imgSrc} className=" object-cover" alt={title} />
      </div>
      <div className="m-0.5 ml-4 flex flex-col justify-center sm:w-1/2  sm:text-left md:ml-4">
        <h4 className="mb-4 mt-4 text-2xl sm:mt-0">{title}</h4>
        <p className="text-base text-gray-500">{formattedDescription}</p>
      </div>
    </div>
  )
}

export default CardComponent
