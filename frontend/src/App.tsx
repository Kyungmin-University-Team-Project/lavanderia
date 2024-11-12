import React from 'react'
import {Route, Routes} from 'react-router-dom'
import 'tailwindcss/tailwind.css'
import Community from './Pages/community/Community'
import ServiceCenter from './Pages/ServiceCenter/ServiceCenter'
import Home from './Pages/home/Home'
import Login from './Pages/auth/Login'
import Agreement from './Pages/auth/Agreement'
import Header from './Components/common/Header'
import ScrollToTop from './Components/common/ScrollToTop'
import Signup from './Pages/auth/Signup'
import Application from './Pages/application/Application'
import ImgApplication from './Pages/application/ImgApplication'
import Cart from './Pages/application/Cart'
import {AuthProvider} from './Context'
import Payment from './Pages/application/Payment'
import ProtectedRoute from './Components/route/ProtectedRoute'
import RestrictedRoute from './Components/route/RestrictedRoute'
import MyPage from './Pages/mypage/MyPage'
import SecondhandProduct from './Pages/secondhand/SecondhandProduct'
import PostDetail from './Pages/community/PostDetail'
import SecondhandProductItemDetail from './Pages/secondhand/SecondhandProductItemDetail'
import SecpmdhandChat from './Pages/secondhand/SecpmdhandChat'
import SecondhandBuy from './Pages/secondhand/SecondhandBuy'
import SecondhandDeliveryBuy from './Pages/secondhand/SecondhandDeliveryBuy'
import CommunityWrite from './Pages/community/CommunityWrite'
import Find from './Pages/auth/Find'
import SecondPayment from './Pages/secondhand/SecondPayment'
import ChangeMemberInformation from './Pages/mypage/ChangeMemberInformation'
import ChangePassword from './Pages/mypage/ChangePassword'
import OrderDetails from './Pages/mypage/OrderDetails'
import DeliveryAddressManagement from './Pages/mypage/DeliveryAddressManagement'
import PaymentManagement from './Pages/mypage/PaymentManagement'
import Notificationsettings from "./Pages/mypage/Notificationsettings";
import CancellationDetails from "./Pages/mypage/CancellationDetails";
import ChangeDeliveryAddressManagement from "./Pages/mypage/ChangeDeliveryAddressManagement";
import Repair from "./Pages/application/Repair";
import RepairCategory from "./Pages/application/RepairCategory";
import RepairDetail from "./Pages/application/RepairDetail";

function App() {
    return (
        <AuthProvider>
            <ScrollToTop/>
            <Routes>
                <Route element={<Header/>}>
                    <Route path="/" element={<Home/>}/>
                    {/* 로그인 필요 페이지 */}
                    <Route element={<ProtectedRoute/>}>
                        <Route path="application" element={<Application/>}/>
                        <Route path="imgApplication" element={<ImgApplication/>}/>
                        <Route path="payment" element={<Payment/>}/>
                        <Route path="cart" element={<Cart/>}/>
                        <Route path="repair" element={<Repair />} />
                        <Route path="repair/:category" element={<RepairCategory />} />
                        <Route path="repair/:category/detail" element={<RepairDetail />} />
                        <Route path="mypage" element={<MyPage/>}>
                            <Route path="changeMemberInformation" element={<ChangeMemberInformation/>}/>
                            <Route path="changePassword" element={<ChangePassword/>}/>
                            <Route path="orderDetails" element={<OrderDetails/>}/>
                            <Route path="cancellationDetails" element={<CancellationDetails/>}/>
                            <Route path="deliveryAddressManagement" element={<DeliveryAddressManagement/>}/>
                            <Route path="deliveryAddressManagement/ChangeDeliveryAddressManagement"
                                   element={<ChangeDeliveryAddressManagement/>}/>
                            <Route path="paymentManagement" element={<PaymentManagement/>}/>
                            <Route path="notificationsettings" element={<Notificationsettings/>}/>
                        </Route>
                    </Route>
                    {/* 로그인 불필요 페이지! */}
                    {/*커뮤니티*/}
                    <Route path="community" element={<Community/>}/>
                    <Route path="community/:postId" element={<PostDetail/>}/>
                    {/* Add PostDetail route */}
                    <Route path="/community/write" element={<CommunityWrite/>}/>
                    {/*중고장터*/}
                    <Route path="secondhand" element={<SecondhandProduct/>}/>
                    <Route
                        path="secondhand/:id"
                        element={<SecondhandProductItemDetail/>}
                    />
                    <Route
                        path="secondhand/:id/secpmhandChat"
                        element={<SecpmdhandChat/>}
                    />
                    <Route
                        path="secondhand/:id/secondhandBuy"
                        element={<SecondhandBuy/>}
                    />
                    <Route
                        path="secondhand/:id/SecondhandDeliveryBuy"
                        element={<SecondhandDeliveryBuy/>}
                    />
                    <Route path="servicecenter" element={<ServiceCenter/>}/>
                    <Route
                        path="secondhand/:id/secondPayment"
                        element={<SecondPayment/>}
                    />
                </Route>

                {/* 로그인시 접근 제한 페이지 */}
                <Route path="/auth" element={<RestrictedRoute/>}>
                    <Route path="login" element={<Login/>}/>
                    <Route path="agreement" element={<Agreement/>}/>
                    <Route path="signup" element={<Signup/>}/>
                    <Route path="find" element={<Find/>}/>
                </Route>
            </Routes>
        </AuthProvider>
    )
}

export default App