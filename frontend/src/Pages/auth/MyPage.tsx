import React, { useContext, useState, useEffect } from "react";
import { AuthContext } from '../../Context';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const MyPage = () => {
    const { access, logout } = useContext(AuthContext);
    const [userName, setUserName] = useState<string | null>(null);
    const navigate = useNavigate();

    console.log(access)
    useEffect(() => {
        const fetchUserName = async () => {
            try {
                const response = await axios.post('localhost:8080/member-info', {
                    headers: {
                        access: access,
                    }
                });
                setUserName(response.data.memberName);
            } catch (error : any) {
                if (error.response && error.response.status === 404) {
                    console.error('Endpoint not found:', error);
                } else if (error.response && error.response.status === 401) {
                    console.error('Failed to fetch user information:', error);
                } else {
                    console.error('eeeee')
                }
            }
        };

        if (access) {
            fetchUserName();
            console.log(1)
        }
    }, [access]);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <article>
            <section>
                <h1>Welcome, {userName ? userName : "Loading..."}</h1>
            </section>
            <button onClick={handleLogout}>Logout</button>
        </article>
    );
};

export default MyPage;
