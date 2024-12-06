import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <div className="flex h-screen items-center justify-center">
      <div className="text-center">
        <h1 className="text-4xl font-bold">404</h1>
        <p className="text-xl">페이지를 찾을 수 없습니다.</p>
        <Link to="/" className="text-blue-500 mt-4 inline-block">홈으로 돌아가기</Link>
      </div>
    </div>
  );
};

export default NotFound;
