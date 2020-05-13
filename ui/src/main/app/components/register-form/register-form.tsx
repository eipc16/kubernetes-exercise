import React from "react";
import {connect, useDispatch} from "react-redux";
import {Button, Form, Input} from "antd";

import {AuthorizationState} from "../../redux/reducers/login-reducer";
import {RegisterActionPublisher} from "../../redux/actions/register";
import './register-form.scss'
import {Alert} from "../../models/infrastructure";
import {AlertTypes} from "../../models/infrastructure/Alert";
import {AlertContainer} from "../alert/alert";

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

    const alertSupplier = (message: string) => {
        const alert: Alert = {
            id: 'register-failure-alert',
            component: 'register-form',
            message: message,
            type: AlertTypes.ERROR,
            canDismiss: true
        };
        return alert;
    };

    const onFinish = (values: any) => {
        const registerData = {
            name: values['name'],
            surname: values['surname'],
            username: values['username'],
            password: values['password'],
            email: values['email'],
            phoneNumber: values['phoneNumber']
        };
        dispatch(registerPublisher.register(registerData, alertSupplier));
    };

    return (
        <Form
            name='register-form'
            initialValues={{
                remember: true
            }}
            onFinish={onFinish}
        >
            <h1 className="text-center"> Welcome</h1>
            <div className="text-center">
                Please fill in the fields below
            </div>
            <AlertContainer component='register-form' />
            <label htmlFor='name'>Name</label>
            <Form.Item
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
            <label htmlFor='surname'>Surname</label>
            <Form.Item
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
            <label htmlFor='username'>Username</label>
            <Form.Item
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
            <label htmlFor='password'>Password</label>
            <Form.Item
                name='password'
                rules={[
                    {
                        required: true,
                        message: 'Password is required!',
                    },
                    {
                        type: 'string',
                        pattern: new RegExp("^.{8,}$"),
                        message: 'Password must have len at least 8 and contains letter and number!'
                    }
                ]}
            >
                <Input.Password />
            </Form.Item>
            <label htmlFor='email'>Email</label>
            <Form.Item
                name='email'
                rules={[
                    {
                        required: true,
                        message: 'Email is required!'
                    },
                    {
                        type: 'email'
                    }
                ]}
            >
                <Input />
            </Form.Item>
            <label htmlFor='phoneNumber'>Phone number</label>
            <Form.Item
                name='phoneNumber'
                rules={[
                    {
                        required: true,
                        message: 'Phone number is required!'
                    },
                    {
                        pattern: new RegExp("[0-9]{9}"),
                        message: 'Phone number contains 9 digits.'
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