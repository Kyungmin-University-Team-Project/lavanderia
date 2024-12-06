import React, { useEffect, useState } from "react";
import { FiBell, FiUser, FiShoppingCart } from "react-icons/fi";
import Logo from "../common/Logo";
import useProtectedNavigation from "../../hooks/useProtectedNavigation";
import FloatingUpButton from "../floating/FloatingUpButton";
import { useLocation } from "react-router-dom";
import BackButton from "../common/BackButton";

const navLinks = [
  { path: "/application", label: "신청하기", protected: true },
  { path: "/community", label: "커뮤니티", protected: false },
  { path: "/secondhand", label: "중고장터", protected: false },
  { path: "/serviceCenter", label: "고객센터", protected: false },
];

const Navigation: React.FC = () => {
  const [activePath, setActivePath] = useState<string>("");
  const [isScrollingUp, setIsScrollingUp] = useState(true);
  const protectedNavigate = useProtectedNavigation();
  const location = useLocation();
  const lastScrollTop = React.useRef(0);

  // 현재 경로를 상태에 저장하여 활성화된 카테고리를 설정
  useEffect(() => {
    setActivePath(location.pathname);
  }, [location]);

  useEffect(() => {
    const handleScroll = () => {
      const scrollTop = window.scrollY;
      if (scrollTop > lastScrollTop.current) {
        setIsScrollingUp(false); // 스크롤 다운
      } else {
        setIsScrollingUp(true); // 스크롤 업
      }
      lastScrollTop.current = scrollTop <= 0 ? 0 : scrollTop;
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleNavigation = (path: string, isProtected: boolean) => {
    setActivePath(path);
    if (isProtected) {
      protectedNavigate(path);
    } else {
      window.location.href = path;
    }
  };

  return (
      <div className={`sticky z-50 bg-white shadow text-base transition-all duration-300 ${isScrollingUp ? "top-0" : "-top-16"}`}>
        <div className="flex h-16 w-full items-center justify-between px-6">
          <div className="flex w-1/6 h-full items-center">
            <BackButton/>
          </div>
          <Logo />
          <div className="flex w-1/6 items-center justify-end gap-2">
            <FiUser className="h-6 w-6 text-black cursor-pointer" onClick={() => protectedNavigate("/mypage")} />
            <FiShoppingCart className="h-6 w-6 text-black cursor-pointer" onClick={() => protectedNavigate("/cart")} />
          </div>
        </div>
        <div className="flex w-full flex-row items-center justify-center px-5">
          <ul className="flex w-full items-center justify-between">
            {navLinks.map((link) => (
                <li key={link.path} className="m-1.5">
              <span
                  className={`block py-2 text-base cursor-pointer ${
                      activePath === link.path ? "text-blue-500 font-bold" : "text-black"
                  }`}
                  onClick={() => handleNavigation(link.path, link.protected)}
              >
                {link.label}
              </span>
                </li>
            ))}
          </ul>
        </div>

        <FloatingUpButton />
      </div>
  );
};

export default Navigation;
