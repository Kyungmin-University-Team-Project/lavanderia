import React from "react";
import { useNavigate } from "react-router-dom";
import { ServiceCardProps } from "../../Typings/Application/Applicattion";

const CustomServiceCard: React.FC<ServiceCardProps> = ({
                                                   title,
                                                   description,
                                                   details,
                                                   image,
                                                   url,
                                                 }) => {
  const navigate = useNavigate();

  const handleCardClick = () => {
    navigate(`/${url}`, { state: { title } });
  };

  return (
    <button
      className="flex h-full w-full cursor-pointer flex-row items-center justify-between rounded-lg border border-gray-300 p-5 shadow-md transition-transform duration-300 hover:-translate-y-2 hover:shadow-lg focus:outline-none"
      onClick={handleCardClick}
    >
      <div className="mt-0 flex h-full flex-grow flex-col ">
        <h2 className="flex mb-2 text-2xl font-bold">{title}</h2>
        <p className="mb-2 text-left text-base text-gray-500">
          {description}
        </p>
        <p className="text-left text-sm text-blue-400">{details}</p>
      </div>
      <div className="flex h-full items-end bg">
        {image && (
          <img
            src={image}
            alt={title}
            className={`h-20 w-20`}
          />
        )}
      </div>
    </button>
  );
};

export default CustomServiceCard;
