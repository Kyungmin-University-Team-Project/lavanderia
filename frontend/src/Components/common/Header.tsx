import React from "react";
import { Outlet } from "react-router-dom";
import Navigation from "../navigation/Navigation";

const Header = () => {
    return (
        <header className='m-auto max-w-2xl'>
            <Navigation />
            <Outlet />
        </header>
    );
};

export default Header;
