import React from "react";
import { Outlet } from "react-router-dom";
import Navigation from "../navigation/Navigation";

const Header = () => {
    return (
        <div className="bg-gray-50">
            <header className='m-auto max-w-xl min-h-screen bg-white'>
                <Navigation/>
                <Outlet/>
            </header>
        </div>

    );
};

export default Header;
