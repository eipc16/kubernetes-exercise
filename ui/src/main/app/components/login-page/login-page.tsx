import React, { useState } from 'react'

import { LoginActionPublisherImpl } from '../../redux/actions/login'
import { AuthenticationServiceImpl } from '../../services'
import { LoginForm } from '../login-form/login-form'
import './login-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";

export const LoginPage = (props: any) => {
  const [authService] = useState(AuthenticationServiceImpl.createInstance())
  const [loginPublisher] = useState(new LoginActionPublisherImpl(authService))

  const history = useHistory();
  const onClick = () => {
    history.push('/')
  }

  return (
      <React.Fragment>
        <PageHeader
          className="site-page-header"
          onBack={() => onClick()}
          title="Login page"
        />
        <div className='login--page'>
          <LoginForm loginPublisher={loginPublisher} {...props} />
        </div>
      </React.Fragment>
  )
}
