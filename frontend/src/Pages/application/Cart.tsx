import React from 'react'
import { Link } from "react-router-dom";
import useCartAdd from '../../hooks/useCartAdd'



const Cart: React.FC = () => {
  const {data: repairData, loading: repairLoading, error: repairError, handleRemove: handleRemove} =
    useCartAdd(
      '/repair/cart-list', '/repair/delete', 'repairCartId')

  const {
    data: lifeData,
    loading: lifeLoading,
    error: lifeLoadingError,
    handleRemove: handleLifeRemove
  } = useCartAdd('/life-laundry/cart-list', '/life-laundry/delete', 'lifeLaundryCartId');
  
  let totalPrice = repairData.reduce((total, item) => total + item.price, 0)
  + lifeData.reduce((total, item) => total + item.price, 0);

  if (repairLoading || lifeLoading) {
    return <div>로딩 중...</div>;
  } else if(lifeLoadingError || repairError){
    return <div>오류 입니다...</div>
  }

  return (
    <div className="max-w-6xl w-full p-5 mx-auto">
      <header className="mb-4 border-b border-black pb-5">
        <h1 className="text-xl font-bold">세탁물 확인</h1>
      </header>

      <div className="w-full bg-white">
        <div className="w-full mb-4">
          <div className="flex flex-col rounded-lg border p-5 shadow-md h-auto">
            <h2 className="mb-4 text-xl font-bold">주문상품</h2>
            <div>
              <ul className="space-y-4">
                {repairData.map((item) => (
                  <li key={item.id}
                      className="flex justify-between items-center p-4 bg-white border border-gray-300 rounded-lg shadow-md hover:shadow-lg transition-all">
                    <div className="flex flex-col w-3/4">
                      <p className="font-semibold text-lg text-gray-800">{item.clothesType}</p>
                      <p className="text-gray-600">{item.howTo}</p>
                      <p className="text-gray-700 font-medium text-xl">{item.price.toLocaleString()}원</p>
                    </div>

                    <button
                      onClick={() => handleRemove(item.id)}
                      className="px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none transition-all"
                    >
                      X
                    </button>
                  </li>
                ))}
              </ul>
              <ul className="space-y-4">
                {lifeData && lifeData.length > 0 && (
                  <>
                    <h2 className="text-xl font-bold mb-4 mt-4">생활빨래</h2>
                    <ul>
                      {lifeData.map((item) => (
                        <li key={item.id}
                            className="flex justify-between items-center p-4 bg-white border border-gray-300 rounded-lg shadow-md hover:shadow-lg transition-all mb-2">
                          <div>
                            <p className="text-gray-700 font-medium text-xl">
                              {item.type}
                            </p>
                            <p className="text-gray-600">
                              {item.weight}Kg
                            </p>
                            <p className="font-semibold text-lg text-gray-800">
                              {item.price.toLocaleString()}원
                            </p>
                          </div>
                          <button
                            onClick={() => handleLifeRemove(item.id)}
                            className="px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none transition-all"
                          >
                            X
                          </button>
                        </li>
                      ))}
                    </ul>
                  </>
                )}
              </ul>
            </div>
          </div>
        </div>

        <div className="w-full flex flex-col justify-between">
          <div className="flex flex-col justify-between rounded-lg border h-auto p-5 shadow-md mb-3 flex-grow">
            <h2 className="mb-3  font-bold border-b border-gray-600 pb-2">상품 내역</h2>
            <p>
              {
                totalPrice.toLocaleString()
              }
              원
            </p>
            <div className="text-start text-black"></div>
          </div>

          <div className="flex flex-col md:flex-row items-center w-full justify-between">
            <Link to="/application" className="mb-3 md:mb-0 md:mr-3 w-full md:w-auto">
              <button className="rounded-lg w-full md:w-64 bg-amber-500 p-2 text-xl text-white">
                세탁물 추가
              </button>
            </Link>
            <Link to="/payment" className="w-full md:w-auto">
              <button className="rounded-lg w-full md:w-64 bg-red-500 p-2 text-xl text-white">
                결제하기
              </button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
