import React, {useState} from 'react'

import {MovieList} from '../movie-list/movie-list'
import {MovieListActionPublisherImpl} from '../../redux/actions/movie-list'
import {connect, useDispatch} from 'react-redux'
import {Button, PageHeader} from 'antd'
import './main-page.scss'
import {AuthenticationServiceImpl, MovieListServiceImpl} from '../../services'
import {LoginActionPublisherImpl} from '../../redux/actions/login'
import {useHistory} from 'react-router-dom'
import {ReduxStore} from "../../redux/reducers/root-reducer";

interface State {
    loggedIn?: boolean;
}

type MainPageProps = State & {};

const MainPageComponent: React.FC<MainPageProps> = (props: MainPageProps) => {
    const {loggedIn} = props;
    const [movieListService] = useState(MovieListServiceImpl.createInstance());
    const [movieListPublisher] = useState(new MovieListActionPublisherImpl(movieListService));
    const [authService] = useState(AuthenticationServiceImpl.createInstance());
    const [loginPublisher] = useState(new LoginActionPublisherImpl(authService));
    const dispatch = useDispatch();

    const history = useHistory();
    const onClick = (): void => {
        if (loggedIn) {
            dispatch(loginPublisher.logout());
        }
        history.push('/login')
    };

    return (
        <React.Fragment>
            <PageHeader className='site-page-header'
                        title='Cinema tickets sale system'
                        extra={[<Button
                            key={'auth-btn'}
                            className='text-center'
                            onClick={onClick}
                            type='primary'>
                            {loggedIn && 'Log out'}
                            {!loggedIn && 'Log in'}
                        </Button>]}>
            </PageHeader>
            <React.Fragment>
                {<MovieList movieListPublisher={movieListPublisher}/>}
            </React.Fragment>
        </React.Fragment>
    )
};

const mapStateToProps = (state: ReduxStore, ownProps: {}): MainPageProps => ({
    loggedIn: state.auth.loggedIn,
    ...ownProps
});

export const MainPage: React.FC<{}> = connect(mapStateToProps)(MainPageComponent);
