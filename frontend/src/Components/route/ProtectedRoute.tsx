import React, { useContext } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { AuthContext } from "../../Context";

const ProtectedRoute: React.FC = () => {
  const { isLoggedIn } = useContext(AuthContext);

  if (!isLoggedIn) {
    return <Navigate to="/auth/login" />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
