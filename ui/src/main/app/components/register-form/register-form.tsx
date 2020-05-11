import React from "react";
import {connect, useDispatch} from "react-redux";
import {Button, Form, Input} from "antd";

import {AuthorizationState} from "../../redux/reducers/login-reducer";
import {RegisterActionPublisher} from "../../redux/actions/register";
import './register-form.scss'

interface OwnProps {
    registerPublisher: RegisterActionPublisher;
}

interface State {
    registered?: boolean;
    registering?: boolean;
}

type RegisterFormProps = OwnProps & State;

const RegisterFormComponent: React.FC<RegisterFormProps> = (props: RegisterFormProps) => {
    const dispatch = useDispatch();
    const { registered , registering, registerPublisher } = props;

    const onFinish = (values: any) => {
        const registerData = {
            name: values['name'],
            surname: values['surname'],
            username: values['username'],
            password: values['password'],
            email: values['email'],
            phoneNumber: values['phoneNumber']
        };
        dispatch(registerPublisher.register(registerData));
    };

    return (
        <Form
            name='register-form'
            initialValues={{
                remember: true
            }}
            onFinish={onFinish}
        >
            <Form.Item>
                <h1 className="text-center"> Welcome</h1>
            </Form.Item>
            <Form.Item>
                <div className="text-center">
                    Please fill in the fields below
                </div>
            </Form.Item>
            <Form.Item
                label='Name'
                name='name'
                rules={[
                    {
                        required: true,
                        message: 'Name is required!'
                    }
                ]}
            >
                <Input />
            </Form.Item>
            <Form.Item
                label='Surname'
                name='surname'
                rules={[
                    {
                        required: true,
                        message: 'Surname is required!'
                    }
                ]}
            >
                <Input />
            </Form.Item>
            <Form.Item
                label='Username'
                name='username'
                rules={[
                    {
                        required: true,
                        message: 'Username is required!'
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
                label='Email'
                name='email'
                rules={[
                    {
                        required: true,
                        message: 'Email is required!'
                    }
                ]}
            >
                <Input />
            </Form.Item>
            <Form.Item
                label='Phone number'
                name='phoneNumber'
                rules={[
                    {
                        required: true,
                        message: 'Phone number is required!'
                    }
                ]}
            >
                <Input />
            </Form.Item>
            <Form.Item>
                <Button
                    className='button'
                    type='primary'
                    htmlType='submit'
                    disabled={registered || registering}
                >
                    { registered && 'Registered'}
                    { registering && 'Registering...'}
                    { !registered && !registering && 'Register!'}
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

export const RegisterForm: React.FC<OwnProps> = connect(mapStateToProps)(RegisterFormComponent);