import React, { useState } from 'react';
import story from "../../Assets/Img/customer_02.de69bf86.png";
import { faqs } from '../../Typings/Service/Faq';

const ServiceCenter: React.FC = () => {
    const [openFAQ, setOpenFAQ] = useState<number | null>(null);

    const toggleFAQ = (index: number) => {
        setOpenFAQ(openFAQ === index ? null : index);
    };

    return (
        <section className='max-w-screen-lg mx-auto px-4'>
            <article className='flex flex-col md:flex-row items-center justify-between mt-20'>
                <div className='mb-8 md:mb-0'>
                    <p className='text-2xl font-bold mb-1'>무엇을 도와드릴까요?</p>
                    <h1 className='text-4xl font-bold mb-4'>000-0000-0000</h1>
                    <p className='text-gray-500 mb-1'>평일: 10:00 ~ 18:00</p>
                    <p className='text-gray-500 mb-1'>점심: 12:30 ~ 13:30</p>
                    <p className='text-gray-500'>주말 공휴일 제외</p>
                </div>
                <div>
                    <img src={story} alt='service' className='w-60 sm:w-80 md:w-96'/>
                </div>
            </article>
            <article className='mt-12'>
                <div>
                    <h1 className='text-2xl font-bold mb-6'>자주 묻는 질문</h1>
                </div>
                <div>
                    {faqs.map((faq, index) => (
                        <div key={index} className='cursor-pointer p-4 border border-gray-300 rounded-md mb-2 bg-gray-100'>
                            <div className='flex justify-between items-center' onClick={() => toggleFAQ(index)}>
                                <p className={`text-lg font-bold ${openFAQ === index ? 'text-red-500' : ''}`}>Q {faq.question}</p>
                                <span>{openFAQ === index ? '▲' : '▼'}</span>
                            </div>
                            {openFAQ === index && (
                                <div className='mt-2 text-gray-700 whitespace-pre-line'>
                                    {faq.answer}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </article>
        </section>
    );
};

export default ServiceCenter;
