import React from 'react';
import {useState} from "react";

import {LoginActionPublisherImpl} from "../../redux/actions/login";
import {AuthenticationServiceImpl} from "../../services";
import {LoginForm} from "../login-form/login-form";

export const LoginPage = (props: any) => {
    const [ authService, ] = useState(new AuthenticationServiceImpl());
    const [ loginPublisher, ] = useState(new LoginActionPublisherImpl(authService))

    return (
        <div className='login--page'>
            <LoginForm loginPublisher={loginPublisher} {...props} />
        </div>
    )
};