import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../Context";

const useProtectedNavigation = () => {
  const { isLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  return (path: string) => {
    if (!isLoggedIn) {
      const shouldNavigate = window.confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?");
      if (shouldNavigate) {
        navigate("/auth/login");
      }
    } else {
      navigate(path);
    }
  };
};

export default useProtectedNavigation;
