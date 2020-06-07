import React from 'react'
import {connect, useDispatch} from 'react-redux'
import {Button, Checkbox, Form, Input} from 'antd'
import {LoginActionPublisher} from '../../redux/actions/login'
import './login-form.scss'
import {Link, useHistory} from 'react-router-dom'
import {Alert} from '../../models/infrastructure'
import {AlertTypes} from '../../models/infrastructure/Alert'
import {AlertContainer} from '../alert/alert'
import {parseQueryParams} from "../../utils/parse-query-params";
import {LoginData} from "../../models/authorization";
import {ReduxStore} from "../../redux/reducers/root-reducer";

interface OwnProps {
    loginPublisher: LoginActionPublisher;
}

interface State {
    loggingIn?: boolean;
    loggedIn?: boolean;
}

type LoginFormProps = OwnProps & State;

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const LoginFormComponent: React.FC<LoginFormProps> = (props: LoginFormProps & any) => {
    const dispatch = useDispatch();
    const history = useHistory();
    const {loggedIn, loggingIn, loginPublisher} = props;

    const redirectPath = parseQueryParams(props.location.search)['redirectPath'] || '/';

    const alertSupplier = (message: string): Alert => {
        return {
            id: 'login-failure-alert',
            component: 'login-form',
            message: message,
            type: AlertTypes.ERROR,
            canDismiss: true
        }
    };

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const onFinish = (values: any): void => {
        const loginData: LoginData = {
            usernameOrEmail: values.usernameOrEmail,
            password: values.password,
            remember: values.remember
        };
        dispatch(loginPublisher.login(loginData, alertSupplier))
    };

    if (loggedIn) {
        history.push(redirectPath);
    }

    return (
        <div className='main--form--container'>
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
                <Form.Item className='alert--container'>
                    <AlertContainer component='login-form'/>
                </Form.Item>
                <label htmlFor='usernameOrEmail'>Username / Email</label>
                <Form.Item
                    name='usernameOrEmail'
                    rules={[
                        {
                            required: true,
                            message: 'Username / Email is required!'
                        }
                    ]}
                >
                    <Input/>
                </Form.Item>
                <label htmlFor='password'>Password</label>
                <Form.Item
                    id="password"
                    name='password'
                    rules={[
                        {
                            required: true,
                            message: 'Password is required!'
                        }
                    ]}
                >
                    <Input.Password/>
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
                        disabled={false}
                    >
                        {loggedIn && 'Logged In'}
                        {loggingIn && 'Logging in...'}
                        {!loggedIn && !loggingIn && 'Log in!'}
                    </Button>
                </Form.Item>
            </Form>
            <div className="text-center">
                {`If you don't have account yet, please:`}
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

const mapStateToProps = (state: ReduxStore, ownProps: OwnProps): LoginFormProps => ({
    loggingIn: state.auth.loggingIn,
    loggedIn: state.auth.loggedIn,
    ...ownProps
});

export const LoginForm: React.FC<OwnProps> = connect(mapStateToProps)(LoginFormComponent);
