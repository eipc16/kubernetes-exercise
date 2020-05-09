import React from "react";
import {connect, useDispatch} from "react-redux";
import {Button, Checkbox, Form, Input} from "antd";

import {AuthorizationState} from "../../redux/reducers/login-reducer";
import {LoginActionPublisher} from "../../redux/actions/login";

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
        <Form
            name='login-form'
            initialValues={{
                remember: true
            }}
            onFinish={onFinish}
        >
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
                name='remember'
                valuePropName='checked'
            >
                <Checkbox>Remember Me</Checkbox>
            </Form.Item>
            <Form.Item>
                <Button
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
    )
};

const mapStateToProps = (state: AuthorizationState, ownProps: OwnProps) => ({
    loggingIn: state.loggingIn,
    loggedIn: state.loggedIn,
    ...ownProps
});

export const LoginForm: React.FC<OwnProps> = connect(mapStateToProps)(LoginFormComponent);