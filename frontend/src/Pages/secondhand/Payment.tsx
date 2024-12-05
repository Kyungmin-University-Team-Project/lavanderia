import React, {  useState } from 'react'
import axiosInstance from '../../Utils/axios/axiosInstance'
import { kakaoPaymentRequest } from '../../Typings/payment/payment'
type OrderDetail = {
  productId: string;
  quantity: number;
  price: number;
};

type Order = {
  rcvrName: string;
  rcvrPhone: string;
  rcvrAddress: string;
  rcvrDetailAddress: string;
  rcvrPostalCode: string;
  dlvrReqMessage: string;
  orderDetailList: OrderDetail[];
};


const Payment: React.FC = () => {
  const totalPrice = 120000
  const [order, setOrder] = useState<Order>({
    rcvrName: "1",
    rcvrPhone: "1",
    rcvrAddress: "1",
    rcvrDetailAddress: "1",
    rcvrPostalCode: "1",
    dlvrReqMessage: "1",
    orderDetailList: [
      {
        productId: "b3c9e0a8-7b4b-4a7b-bd9a-4a84f43c2b08",
        quantity: 1,
        price: totalPrice,
      },
    ],
  });

  const handleKakaoPaymentBtn = async () => {
    try {
      const response = await axiosInstance.post(`/order/add`,  {...order});
      if (response.status === 200) {
        console.log(response.data);
      }
      await kakaoPaymentRequest(response.data, totalPrice);
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <div>
      <button className='bg-black w-full p-2 rounded text-white' onClick={handleKakaoPaymentBtn}>결제하기</button>
    </div>
  );
};

export default Payment;
