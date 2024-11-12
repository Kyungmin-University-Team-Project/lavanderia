import React from 'react';
import EnterPassword from "./EnterPassword";
import {Link} from "react-router-dom";

const DeliveryAddressManagement = () => {
    return (
        <section className='min-h-full'>
           <form>
               <button className='text-center text-xs font-semibold p-2 w-full border-gray-100
               mt-4 mb-4 border border-solid'>
                   배송지 추가하기
               </button>
               <ul>
                   <li>
                       <span className='mb-2 text-sm inline-block
                       font-bold mr-2'>박민규</span>
                       <button className='text-xs border rounded border-solid
                       bg-gray-50 p-1'>
                           기본 배송지</button>
                       <div></div>
                       <span className='mb-2 inline-block text-sm'>경기 양주시 덕계로 111</span>
                       <span className='text-sm'>122121</span>
                       <h6 className='text-sm mb-2'>010-10230=3100</h6>
                       <Link to='ChangeDeliveryAddressManagement'>
                           <button className='text-xs border rounded border-solid
                       bg-gray-50 p-1'>수정
                           </button>
                       </Link>
                   </li>
               </ul>
           </form>
        </section>
    );
};

export default DeliveryAddressManagement;