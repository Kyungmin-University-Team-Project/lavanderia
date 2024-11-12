import React, { useContext } from "react";
import { AuthContext } from '../../Context';
import { useNavigate } from 'react-router-dom';

const MyPage = () => {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div>
      <h1>My Page</h1>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
};

export default MyPage;
