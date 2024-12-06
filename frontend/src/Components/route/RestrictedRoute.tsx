import React, { useContext, ReactNode } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { AuthContext } from "../../Context";

interface RestrictedRouteProps {
  children?: ReactNode;
}

const RestrictedRoute: React.FC<RestrictedRouteProps> = ({ children }) => {
  const { isLoggedIn } = useContext(AuthContext);

  if (isLoggedIn) {
    return <Navigate to="/" />;
  }

  return children ? <>{children}</> : <Outlet />;
};

export default RestrictedRoute;
