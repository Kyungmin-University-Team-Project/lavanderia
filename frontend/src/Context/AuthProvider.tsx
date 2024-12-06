import React, { createContext, useState, useEffect, ReactNode } from 'react';
import { encryptToken } from '../Utils/auth/crypto';

// Context 생성
interface AuthContextType {
  isLoggedIn: boolean;
  access: string;
  login: (access: string, username: string, rememberMe: boolean) => void;
  logout: () => void;
}

interface AuthProviderProps {
  children: ReactNode;
}

const defaultAuthContext: AuthContextType = {
  isLoggedIn: false,
  access: '',
  login: () => {},
  logout: () => {},
};

export const AuthContext = createContext<AuthContextType>(defaultAuthContext);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [access, setAccess] = useState('');

  useEffect(() => {
    const encryptedAccess = window.localStorage.getItem('access');
    const username = window.localStorage.getItem('username'); // 유저명 로드
    const rememberMe = window.localStorage.getItem('rememberMe');

    // 로그인 유지 (토큰과 유저명이 존재할 경우)
    if (encryptedAccess && username) {
      setIsLoggedIn(true);
    }

    // 추가적으로 액세스 토큰 재발급 로직을 작성할 수 있음
    // 예: if (encryptedAccess && rememberMe) { ... }
  }, []);

  const login = (access: string, username: string, rememberMe: boolean) => {
    setIsLoggedIn(true);
    setAccess(access);
    // 토큰 및 유저명 저장
    window.localStorage.setItem('access', encryptToken(access));
    window.localStorage.setItem('username', username); // 유저명 저장
    window.localStorage.setItem('rememberMe', rememberMe ? 'true' : 'false');
  };

  const logout = () => {
    setIsLoggedIn(false);
    setAccess('');
    // 로컬스토리지에서 데이터 제거
    window.localStorage.removeItem('access');
    window.localStorage.removeItem('username'); // 유저명 제거
    window.localStorage.removeItem('rememberMe');
  };

  return (
      <AuthContext.Provider value={{ isLoggedIn, access, login, logout }}>
        {children}
      </AuthContext.Provider>
  );
};
