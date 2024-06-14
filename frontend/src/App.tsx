import React from "react";
import { Route, Routes } from "react-router-dom";
import "tailwindcss/tailwind.css";
import Community from "./Pages/community/Community";
import ServiceCenter from "./Pages/ServiceCenter/ServiceCenter";
import Home from "./Pages/home/Home";
import Login from "./Pages/auth/Login";
import Agreement from "./Pages/auth/Agreement";
import Header from "./Components/common/Header";
import ScrollToTop from "./Components/common/ScrollToTop";
import Signup from "./Pages/auth/Signup";
import Application from "./Pages/application/Application";
import ApplicationDetail from "./Pages/application/ApplicationDetail";
import ImgApplication from "./Pages/application/ImgApplication";
import Cart from "./Pages/application/Cart";
import { AuthProvider } from "./Context";
import Payment from "./Pages/application/Payment";
import ProtectedRoute from "./Components/route/ProtectedRoute";
import RestrictedRoute from "./Components/route/RestrictedRoute";
import MyPage from "./Pages/auth/MyPage";
import SecondhandProduct from "./Pages/secondhand/SecondhandProduct";
import PostDetail from "./Pages/community/PostDetail";

function App() {
  return (
    <AuthProvider>
      <ScrollToTop />
      <Routes>
        <Route element={<Header />}>
          <Route path="/" element={<Home />} />

          {/* 로그인 필요 페이지 */}
          <Route element={<ProtectedRoute />}>
            <Route path="application" element={<Application />} />
            <Route path="applicationDetail" element={<ApplicationDetail />} />
            <Route path="imgApplication" element={<ImgApplication />} />
            <Route path="payment" element={<Payment />} />
            <Route path="cart" element={<Cart />} />
            <Route path="mypage" element={<MyPage />} />
          </Route>

          {/* 로그인 불필요 페이지 */}
          <Route path="community" element={<Community />} />
          <Route path="community/:postId" element={<PostDetail/>} /> {/* Add PostDetail route */}
          <Route path="secondhand" element={<SecondhandProduct />} />
          <Route path="servicecenter" element={<ServiceCenter />} />
        </Route>


        {/* 로그인시 접근 제한 페이지 */}
        <Route path="/auth" element={<RestrictedRoute />}>
          <Route path="login" element={<Login />} />
          <Route path="agreement" element={<Agreement />} />
          <Route path="signup" element={<Signup />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}

export default App;
