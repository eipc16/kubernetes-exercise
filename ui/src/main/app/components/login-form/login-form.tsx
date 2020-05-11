import React from "react";
import {connect, useDispatch} from "react-redux";
import {Button, Checkbox, Form, Input} from "antd";

import {AuthorizationState} from "../../redux/reducers/login-reducer";
import {LoginActionPublisher} from "../../redux/actions/login";
import './login-form.scss'
import {Link} from "react-router-dom";

interface OwnProps {
    loginPublisher: LoginActionPublisher;
}

interface State {
    loggingIn?: boolean;
    loggedIn?: boolean;
}

type LoginFormProps = OwnProps & State;

const LoginFormComponent: React.FC<LoginFormProps> = (props: LoginFormProps) => {
    const dispatch = useDispatch();
    const { loggedIn , loggingIn, loginPublisher } = props;

    const onFinish = (values: any) => {
        const loginData = {
            usernameOrEmail: values['usernameOrEmail'],
            password: values['password']
        };
        dispatch(loginPublisher.login(loginData));
    };

    return (
        <div>
            <Form
                name='login-form'
                initialValues={{
                    remember: true
                }}
                onFinish={onFinish}
            >
                <Form.Item>
                    <h1 className="text-center"> Welcome</h1>
                </Form.Item>
                <Form.Item
                    label='Username / Email'
                    name='usernameOrEmail'
                    rules={[
                        {
                            required: true,
                            message: 'Username / Email is required!'
                        }
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='Password'
                    name='password'
                    rules={[
                        {
                            required: true,
                            message: 'Password is required!'
                        }
                    ]}
                >
                    <Input.Password />
                </Form.Item>
                <Form.Item
                    className='checkbox'
                    name='remember'
                    valuePropName='checked'
                >
                    <Checkbox>Remember Me</Checkbox>
                </Form.Item>
                <Form.Item>
                    <Button
                        className='button'
                        type='primary'
                        htmlType='submit'
                        disabled={loggedIn || loggingIn}
                    >
                        { loggedIn && 'Logged In'}
                        { loggingIn && 'Logging in...'}
                        { !loggedIn && !loggingIn && 'Log in!'}
                    </Button>
                </Form.Item>
            </Form>
            <div className="text-center">
                If you don't have account yet, please:
            </div>
            <div className="text-center">
                <Link
                    className="text-center"
                    to="/register"
                    type='primary'
                >
                    Create account!
                </Link>
            </div>
        </div>

    )
};

const mapStateToProps = (state: AuthorizationState, ownProps: OwnProps) => ({
    loggingIn: state.loggingIn,
    loggedIn: state.loggedIn,
    ...ownProps
});

export const LoginForm: React.FC<OwnProps> = connect(mapStateToProps)(LoginFormComponent);