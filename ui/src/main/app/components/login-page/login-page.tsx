import React from 'react';
import {useState} from "react";

import {LoginActionPublisherImpl} from "../../redux/actions/login";
import {AuthenticationServiceImpl} from "../../services";
import {LoginForm} from "../login-form/login-form";
import './login-page.scss'

export const LoginPage = (props: any) => {
    const [ authService, ] = useState(new AuthenticationServiceImpl());
    const [ loginPublisher, ] = useState(new LoginActionPublisherImpl(authService))

    return (
        <div className='Login'>
            <LoginForm loginPublisher={loginPublisher} {...props} />
        </div>
    )
};