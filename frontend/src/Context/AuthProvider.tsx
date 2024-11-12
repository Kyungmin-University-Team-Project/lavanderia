import React, { createContext, useState, useEffect, ReactNode } from 'react';
import { authenticateAccess } from '../Utils/auth/tokenService';
import { decryptToken, encryptToken } from '../Utils/auth/crypto';

// Context 생성
interface AuthContextType {
  isLoggedIn: boolean;
  access: string;
  login: (access: string, rememberMe: boolean) => void;
  logout: () => void;
}

const defaultAuthContext: AuthContextType = {
  isLoggedIn: false,
  access: '',
  login: () => {},
  logout: () => {}
};

export const AuthContext = createContext<AuthContextType>(defaultAuthContext);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [access, setAccess] = useState('');

  useEffect(() => {
    const encryptedAccess = window.localStorage.getItem('access');
    const rememberMe = window.localStorage.getItem('rememberMe');

    // 우선 액세스토큰 존재시(로그인시) 로그인 유지
    if (encryptedAccess){
      setIsLoggedIn(true)

    }

    // 여기서 리프레쉬와 리멤버 존재시 해당 토큰으로 액세스 재발급후 로그인 유지기능 구현
    // if (encryptedAccess && rememberMe) {

      // const storedAccess = decryptToken(encryptedAccess);
      // 액세스 토큰 인증 500에러뜸
      // if (storedAccess) {
      //   authenticateAccessToken(storedAccess);
      // } 
    // }
  }, []);

  const authenticateAccessToken = async (token: string) => {
    try {
      const data = await authenticateAccess(token);
      const newAccess = data.access || token;

      setAccess(newAccess); // 새로운 액세스 토큰이 있으면 설정하고, 없으면 기존 토큰 유지
      setIsLoggedIn(true);

      if (window.localStorage.getItem('rememberMe') && data.access) {
        window.localStorage.setItem('access', encryptToken(newAccess));
      }
    } catch (error) {
      console.error('Failed to authenticate or refresh access token:', error);
      logout();
    }
  };

  const login = (access: string, rememberMe: boolean) => {
    setIsLoggedIn(true);
    setAccess(access);
    window.localStorage.setItem('access', encryptToken(access));
    window.localStorage.setItem('rememberMe', 'true');
  };

  const logout = () => {
    setIsLoggedIn(false);
    setAccess('');
    window.localStorage.removeItem('access');
    window.localStorage.removeItem('rememberMe');
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, access, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
