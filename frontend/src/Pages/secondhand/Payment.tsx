import React, { useEffect } from 'react';
import axios from 'axios';

interface Iamport {
  init: (userCode: string) => void;
  request_pay: (params: any, callback: (response: any) => void) => void;
}

declare global {
  interface Window {
    IMP?: Iamport;
  }
}

const Payment: React.FC = () => {
  useEffect(() => {
    const jquery = document.createElement('script');
    jquery.src = 'https://code.jquery.com/jquery-1.12.4.min.js';
    const iamport = document.createElement('script');
    iamport.src = 'https://cdn.iamport.kr/js/iamport.payment-1.1.7.js';
    document.head.appendChild(jquery);
    document.head.appendChild(iamport);
    return () => {
      document.head.removeChild(jquery);
      document.head.removeChild(iamport);
    };
  }, []);

  const requestPay = () => {
    const { IMP } = window;
    if (!IMP) {
      console.error('IMP is not loaded');
      return;
    }

    IMP.init('imp04741334');

    IMP.request_pay(
      {
        pg: "kicc.{TC0ONETIME}",
        pay_method: 'card',
        merchant_uid: new Date().getTime(),
        name: '테스트 상품',
        amount: 1004,
        buyer_email: 'test@naver.com',
        buyer_name: '코드쿡',
        buyer_tel: '010-1234-5678',
        buyer_addr: '서울특별시',
        buyer_postcode: '123-456',
      },
      async (rsp) => {
        try {
          const { data } = await axios.post('http://localhost:8080/verifyIamport/' + rsp.imp_uid);
          if (rsp.paid_amount === data.response.amount) {
            alert('결제 성공');
          } else {
            alert('결제 실패');
          }
        } catch (error) {
          console.error('Error while verifying payment:', error);
          alert('결제 실패');
        }
      }
    );
  };

  return (
    <div>
      <button onClick={requestPay}>결제하기</button>
    </div>
  );
};

export default Payment;