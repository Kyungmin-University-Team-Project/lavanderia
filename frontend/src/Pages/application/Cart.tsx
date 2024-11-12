import React from "react";
import { Link } from "react-router-dom";
import clothes_1 from "../../Assets/Img/clothes_1.png";
import clothes_2 from "../../Assets/Img/clothes_2.png";
import clothes_3 from "../../Assets/Img/clothes_3.png";

const OrderItem: React.FC<{
  item: { image: string, name: string, description: string, price: string },
  onRemove: () => void,
  onIncrease: () => void,
  onDecrease: () => void,
  quantity: number
}> = ({ item, onRemove, onIncrease, onDecrease, quantity }) => (
    <div className="mb-2 w-full flex md:flex-row flex-col items-center border p-3 rounded-lg bg-white">
      <div className="flex items-center md:w-auto w-full mb-4 md:mb-0">
        <img src={item.image} alt="Laundry Item" className="mr-4 h-24 w-24"/>
        <div className="flex flex-col text-left">
          <p className="font-semibold text-sm">{item.name}</p>
          <p className="text-gray-500 text-sm">{item.description}</p>
          <p className="text-gray-500 text-sm">{item.price}</p>
        </div>
      </div>
      <div className="flex items-center md:ml-auto md:w-auto w-full justify-between md:justify-end">
        <div className="flex items-center">
          <button onClick={onDecrease} className="px-2 border rounded">-</button>
          <span className="px-2">{quantity}</span>
          <button onClick={onIncrease} className="px-2 border rounded">+</button>
        </div>
        <div className="flex items-center ml-4">
          <p className="font-semibold text-sm">{(parseInt(item.price.replace(/[^0-9]/g, "")) * quantity).toLocaleString()}원</p>
        </div>
        <button onClick={onRemove} className="ml-4 text-red-500">X</button>
      </div>
    </div>
);

const Cart: React.FC = () => {
  const [orderItems, setOrderItems] = React.useState([
    {
      image: clothes_1,
      name: "맞춤 세탁",
      description: "일반 의류 다림질 항균 추가",
      price: "30,000원",
      quantity: 1
    },
    {
      image: clothes_2,
      name: "프리미엄 세탁",
      description: "고급 의류 다림질 항균 추가",
      price: "50,000원",
      quantity: 1
    },
    {
      image: clothes_3,
      name: "신속 세탁",
      description: "신속 의류 다림질 항균 추가",
      price: "40,000원",
      quantity: 1
    }
  ]);

  const handleRemove = (index: number) => {
    const newItems = [...orderItems];
    newItems.splice(index, 1);
    setOrderItems(newItems);
  };

  const handleIncrease = (index: number) => {
    const newItems = [...orderItems];
    newItems[index].quantity += 1;
    setOrderItems(newItems);
  };

  const handleDecrease = (index: number) => {
    const newItems = [...orderItems];
    if (newItems[index].quantity > 1) {
      newItems[index].quantity -= 1;
      setOrderItems(newItems);
    }
  };

  const totalAmount = orderItems.reduce((total, item) => total + parseInt(item.price.replace(/[^0-9]/g, "")) * item.quantity, 0);

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
                {orderItems.map((item, index) => (
                    <OrderItem
                        key={index}
                        item={item}
                        quantity={item.quantity}
                        onRemove={() => handleRemove(index)}
                        onIncrease={() => handleIncrease(index)}
                        onDecrease={() => handleDecrease(index)}
                    />
                ))}
              </div>
            </div>
          </div>

          <div className="w-full flex flex-col justify-between">
            <div className="flex flex-col justify-between rounded-lg border h-auto p-5 shadow-md mb-3 flex-grow">
              <h2 className="mb-3  font-bold border-b border-gray-600 pb-2">상품 내역</h2>
              <div className="text-start text-black">
                {orderItems.map(item => (
                    <p key={item.name} className="text-sm text-gray-500 mb-1 font-bold">{item.name} x {item.quantity}</p>
                ))}
                <div className="flex justify-between items-center mt-3">
                  <p className=" font-bold">총 결제금액: </p>
                  <p className=" font-bold">{totalAmount.toLocaleString()}원</p>
                </div>
              </div>
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
