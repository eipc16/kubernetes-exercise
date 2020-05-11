import React from 'react';
import {useState} from "react";

import {AuthenticationServiceImpl} from "../../services";
import {RegisterForm} from "../register-form/register-form";
import './register-page.scss'
import {RegisterActionPublisherImpl} from "../../redux/actions/register";

export const RegisterPage = (props: any) => {
    const [ authService, ] = useState(AuthenticationServiceImpl.createInstance());
    const [ registerPublisher, ] = useState(new RegisterActionPublisherImpl(authService))

    return (
        <div className="Register">
            <RegisterForm registerPublisher={registerPublisher} {...props} />
        </div>
    )
};