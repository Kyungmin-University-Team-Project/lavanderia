import * as PortOne from '@portone/browser-sdk/v2'
import axios from 'axios'
import axiosInstance from '../../Utils/axios/axiosInstance'
export const kakaoPaymentRequest = async (
  orderId: number,
  totalAmount: number,
) => {
  try {
    const responseUserData = await PortOne.requestPayment({
      storeId: 'store-9d87031f-513e-444c-95d0-341302227a19',
      totalAmount,
      channelKey: 'channel-key-18691381-0f3b-4160-9e46-e483e6ae1b5b',
      paymentId: crypto.randomUUID(),
      orderName: '나이키 와플 트레이너 2 SD',
      currency: 'CURRENCY_KRW',
      payMethod: 'EASY_PAY',
      isTestChannel: true,
      customer: {
        email: 'customer@example.com',
        phoneNumber: '010-6662-8752',
        fullName: '이관용',
      },
    });

    if (!responseUserData || responseUserData.code != null) {
      console.error('Payment request failed:', responseUserData);
      return null; // Return null on failure
    }

    // Prepare data for completion


    const kakaoResponse = await axiosInstance.post('/payment/complete', {
      paymentId: responseUserData.paymentId,
      orderId: orderId,
    });
    console.log('Payment completion response:', kakaoResponse.data);
    return kakaoResponse.data; // Return the response data

  } catch (e) {
    console.log('Error in payment request:', e);
    if (axios.isAxiosError(e)) {
      console.log('Error response:', e.response?.data);
      console.log('Error status:', e.response?.status);
    }
    return null; // Return null on error
  }
};

