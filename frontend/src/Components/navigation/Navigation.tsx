import React, { useEffect, useState } from "react";
import { FiBell, FiUser, FiShoppingCart } from "react-icons/fi";
import Logo from "../common/Logo";
import useProtectedNavigation from "../../hooks/useProtectedNavigation";
import FloatingUpButton from "../floating/FloatingUpButton";
import { useSearchParams, useLocation } from "react-router-dom";

const navLinks = [
  { path: "/application", label: "신청하기", protected: true },
  { path: "/community", label: "커뮤니티", protected: false },
  { path: "/secondhand", label: "중고장터", protected: false },
  { path: "/serviceCenter", label: "고객센터", protected: false },
];

const Navigation: React.FC = () => {
  const [activePath, setActivePath] = useState<string>("");
  const protectedNavigate = useProtectedNavigation();
  const location = useLocation();

  // 현재 경로를 상태에 저장하여 활성화된 카테고리를 설정
  useEffect(() => {
    setActivePath(location.pathname);
  }, [location]);

  const handleNavigation = (path: string, isProtected: boolean) => {
    setActivePath(path); // 클릭된 경로를 활성화 상태로 설정
    if (isProtected) {
      protectedNavigate(path);
    } else {
      window.location.href = path;
    }
  };

  return (
      <div className="sticky top-0 z-50 bg-white shadow text-base">
        <div className="flex h-16 w-full items-center justify-between px-7">
          <div className="flex w-1/6 items-center justify-start">
            <FiBell className="h-6 w-6 text-black" />
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
