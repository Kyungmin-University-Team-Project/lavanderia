import React, { useEffect, useRef, useState } from 'react'
import { useLocation } from 'react-router-dom'
import Calendar, { CalendarProps } from 'react-calendar'
import 'react-calendar/dist/Calendar.css' // Import CSS for the calendar
import { EditFieldProps } from '../../Typings/Application/Applicattion'
import Postcode from '../../Components/postcode/Postcode'
import { kakaoPaymentRequest } from '../../Typings/payment/payment'
import axiosInstance from './axiosInstance'
import { API_URL } from '../../Api/api'

const EditField: React.FC<EditFieldProps> = ({ label, defaultValue }) => (
  <div className="mb-2">
    <label className="block font-semibold">{label}</label>
    <input
      type="text"
      className="w-full rounded border p-2 text-gray-400"
      defaultValue={defaultValue}
      onFocus={(e) =>
        e.target.scrollIntoView({ behavior: 'smooth', block: 'center' })
      }
    />
  </div>
)

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
  // const location = useLocation()
  const totalPrice = 123
  const [order, setOrder] = useState<Order>({
    rcvrName: "1",
    rcvrPhone: "1",
    rcvrAddress: "1",
    rcvrDetailAddress: "1",
    rcvrPostalCode: "1",
    dlvrReqMessage: "1",
    orderDetailList: [
      {
        productId: "029f45a1-42d4-4a36-9a40-7421b2aeda37",
        quantity: 1,
        price: totalPrice,
      },
    ],
  });

  const [isPostcodeOpen, setIsPostcodeOpen] = useState(false)
  // const [address, setAddress] = useState('')
  const [pickupDate, setPickupDate] = useState<Date | null>(null)
  const [deliveryDate, setDeliveryDate] = useState<Date | null>(null)
  const [isPickupCalendarOpen, setIsPickupCalendarOpen] = useState(false)
  const [isDeliveryCalendarOpen, setIsDeliveryCalendarOpen] = useState(false)

  const pickupCalendarRef = useRef<HTMLDivElement>(null)
  const deliveryCalendarRef = useRef<HTMLDivElement>(null)

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setOrder((prev) => ({
      ...prev,
      [name]: value,
    }));
  };


  const handleAddressClick = () => {
    setIsPostcodeOpen(true)
  }

  const handleAddressComplete = (selectedAddress: string) => {
    setOrder((prev) => ({
      ...prev,
      rcvrAddress: selectedAddress,
    }))
    setIsPostcodeOpen(false)
  }

  const handleModalClick = (event: React.MouseEvent<HTMLDivElement>) => {
    if (event.target === event.currentTarget) {
      setIsPostcodeOpen(false)
    }
  }

  const handlePickupDateChange: CalendarProps['onChange'] = (value) => {
    setPickupDate(value as Date)
    setIsPickupCalendarOpen(false)
  }

  const handleDeliveryDateChange: CalendarProps['onChange'] = (value) => {
    setDeliveryDate(value as Date)
    setIsDeliveryCalendarOpen(false)
  }

  const handleClickOutside = (event: MouseEvent) => {
    if (
      pickupCalendarRef.current &&
      !pickupCalendarRef.current.contains(event.target as Node)
    ) {
      setIsPickupCalendarOpen(false)
    }
    if (
      deliveryCalendarRef.current &&
      !deliveryCalendarRef.current.contains(event.target as Node)
    ) {
      setIsDeliveryCalendarOpen(false)
    }
  }

  useEffect(() => {
    if (isPickupCalendarOpen || isDeliveryCalendarOpen) {
      document.addEventListener('mousedown', handleClickOutside)
    } else {
      document.removeEventListener('mousedown', handleClickOutside)
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [isPickupCalendarOpen, isDeliveryCalendarOpen])

  const handleKakaoPaymentBtn = async () => {
    try {
      const response = await axiosInstance.post(`${API_URL}/order/add`, { ...order });
      if (response.status === 200) {
        console.log(response.data);
      }
      await kakaoPaymentRequest(response.data, totalPrice);
    } catch (e) {
      console.log(e);
    }
  };



  return (
    <div className="container mx-auto w-full p-5">
      <header className="mb-5 border-b border-black pb-5">
        <h1 className="text-xl font-bold">결제 페이지</h1>
      </header>

      <div className="lg:mx-auto">
        <div className="flex flex-col gap-5">
          <div className="flex flex-col justify-center rounded-lg border p-5 shadow-md">
            <h2 className="mb-4 text-xl font-bold">수거일/배송일</h2>
            <div className="relative mb-2">
              <label className="block font-semibold">수거일</label>
              <input
                type="text"
                className="w-full rounded border p-2"
                value={pickupDate ? pickupDate.toLocaleDateString() : ''}
                onClick={() => setIsPickupCalendarOpen(true)}
                readOnly
                placeholder="수거일을 선택해주세요"
                onFocus={(e) =>
                  e.target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center',
                  })
                }
              />
              {isPickupCalendarOpen && (
                <div className="absolute z-50" ref={pickupCalendarRef}>
                  <Calendar
                    onChange={handlePickupDateChange}
                    value={pickupDate}
                  />
                </div>
              )}
            </div>
            <div className="relative mb-2">
              <label className="block font-semibold">배송일</label>
              <input
                type="text"
                className="w-full rounded border p-2"
                value={deliveryDate ? deliveryDate.toLocaleDateString() : ''}
                onClick={() => setIsDeliveryCalendarOpen(true)}
                readOnly
                placeholder="배송일을 선택해주세요"
                onFocus={(e) =>
                  e.target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center',
                  })
                }
              />
              {isDeliveryCalendarOpen && (
                <div className="absolute z-50" ref={deliveryCalendarRef}>
                  <Calendar
                    onChange={handleDeliveryDateChange}
                    value={deliveryDate}
                  />
                </div>
              )}
            </div>
          </div>

          <form className="flex flex-col justify-center rounded-lg border p-5 shadow-md">
            <h2 className="mb-4 text-xl font-bold">수거/배송 정보</h2>
            <div>
              <label htmlFor="rcvrName">
                이름
                <input
                  name="rcvrName"
                  className="w-full rounded border p-2"
                  value={order.rcvrName}
                  onChange={handleOnChange}
                  placeholder="이름"
                />
              </label>
            </div>
            <div>
              <label htmlFor="rcvrPhone">
                핸드폰 번호
                <input
                  className="w-full rounded border p-2"
                  name="rcvrPhone"
                  value={order.rcvrPhone}
                  onChange={handleOnChange}
                  placeholder="이름"
                />
              </label>
            </div>
            <div className="mb-2">
              <label htmlFor='rcvrAddress' className="block font-semibold">주소</label>
              <input
                type="text"
                className="w-full rounded border p-2"
                name="rcvrAddress"
                value={order.rcvrAddress}
                onChange={handleOnChange}
                onClick={handleAddressClick}
                placeholder="주소를 입력해주세요"
                onFocus={(e) =>
                  e.target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center'
                  })
                }
              />
            </div>
            <div>
              <label htmlFor='rcvrDetailAddress' className="block font-semibold">상세 주소</label>
              <input
                className="w-full rounded border p-2 mb-2"
                name="rcvrDetailAddress"
                value={order.rcvrDetailAddress}
                onChange={handleOnChange}
                placeholder="상세주소를 입력해주세요"
              />
            </div>
            <div>
              <label htmlFor="rcvrPostalCode">
                부부 조소
                <input type="text"
                       name="rcvrPostalCode"
                       value={order.rcvrPostalCode}
                       onChange={handleOnChange} />
              </label>
            </div>
            <div>
              <label htmlFor="dlvrReqMessage">
                배송 메세지
                <input type="text"
                       name="dlvrReqMessage"
                       value={order.dlvrReqMessage}
                       onChange={handleOnChange} />
              </label>
            </div>
            {/*<EditField label="수거 장소" defaultValue="문 앞" />*/}
            {/*<EditField label="배송 장소" defaultValue="문 앞" />*/}
          </form>

          <div className="flex flex-col justify-center rounded-lg border p-5 shadow-md">
            <h2 className="mb-4 text-xl font-bold">포인트 혜택</h2>
            <div className="mb-2 flex justify-between">
              <p>구매적립</p>
              <p>총 199점</p>
            </div>
            <div className="mb-2 flex justify-between">
              <p>기본적립</p>
              <p>100점</p>
            </div>
            <div className="mb-2 flex justify-between">
              <p>추가적립</p>
              <p>최대 150점</p>
            </div>
            <p className="text-right text-blue-500">최대 300점 적립</p>
          </div>

          <div className="flex flex-col justify-center rounded-lg border p-5 shadow-md">
            <h2 className="mb-4 text-xl font-bold">결제수단</h2>
            <div className="mb-2">
              <label className="mb-2 flex items-center">
                <input type="radio" name="payment" className="mr-2" /> 카드 결제
              </label>
              <div className="ml-4">
                <select className="mb-2 w-full rounded border p-2">
                  <option>카드를 선택해주세요</option>
                  <option>카드 1</option>
                  <option>카드 2</option>
                </select>
                <input
                  type="text"
                  placeholder="할부개월수 입력"
                  className="w-full rounded border p-2"
                  onFocus={(e) =>
                    e.target.scrollIntoView({
                      behavior: 'smooth',
                      block: 'center',
                    })
                  }
                />
              </div>
            </div>
            <div className="mb-2">
              <label className="mb-2 flex items-center">
                <input type="radio" name="payment" className="mr-2" /> 계좌 이체
              </label>
            </div>
          </div>
        </div>

        <div className="mt-5 text-center lg:mt-10">
          <button
            className="w-full rounded-lg bg-blue-500 px-6 py-3 text-xl text-white"
            onClick={handleKakaoPaymentBtn}
          >
            세탁 신청하기
          </button>
        </div>
      </div>

      {isPostcodeOpen && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-gray-900 bg-opacity-50 px-5 lg:px-0"
          onClick={handleModalClick}
        >
          <div className="w-full max-w-md rounded-lg bg-white p-4">
            <Postcode onComplete={handleAddressComplete} />
          </div>
        </div>
      )}
    </div>
  )
}

export default Payment
