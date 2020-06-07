import React, { useState } from 'react'

import { AuthenticationServiceImpl } from '../../services'
import { RegisterForm } from '../register-form/register-form'
import './register-page.scss'
import { RegisterActionPublisherImpl } from '../../redux/actions/register'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const RegisterPage = (props: any): JSX.Element => {
  const [authService] = useState(AuthenticationServiceImpl.createInstance());
  const [registerPublisher] = useState(new RegisterActionPublisherImpl(authService));

  const history = useHistory();
  const onClick = (): void => {
    history.push('/login')
  };

  return (
      <React.Fragment>
        <PageHeader
            className="site-page-header"
            onBack={(): void => onClick()}
            title="Register page"
        />
        <div className="register--page">
          <RegisterForm registerPublisher={registerPublisher} {...props} />
        </div>
      </React.Fragment>
  )
};