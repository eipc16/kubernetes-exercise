import React, {useState} from 'react'

import {LoginActionPublisherImpl} from '../../redux/actions/login'
import {AuthenticationServiceImpl} from '../../services'
import {LoginForm} from '../login-form/login-form'
import './login-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const LoginPage = (props: any): JSX.Element => {
    const [authService] = useState(AuthenticationServiceImpl.createInstance());
    const [loginPublisher] = useState(new LoginActionPublisherImpl(authService));

    const history = useHistory();
    const onClick = (): void => {
        history.push('/')
    };

    return (
        <React.Fragment>
            <PageHeader
                className="site-page-header"
                onBack={(): void => onClick()}
                title="Login page"
            />
            <div className='login--page'>
                <LoginForm loginPublisher={loginPublisher} {...props} />
            </div>
        </React.Fragment>
    )
};
