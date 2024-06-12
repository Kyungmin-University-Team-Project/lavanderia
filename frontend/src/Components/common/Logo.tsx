import React from 'react';
import { Link } from 'react-router-dom';

interface LogoProps {
    handleLinkClick?: () => void;
}

const Logo: React.FC<LogoProps> = ({ handleLinkClick }) => {
    return (
        <Link to="/" onClick={handleLinkClick}>
            <h1 className="text-3xl   font-Gugi">하루 세탁</h1>
        </Link>
    );
};

export default Logo;
