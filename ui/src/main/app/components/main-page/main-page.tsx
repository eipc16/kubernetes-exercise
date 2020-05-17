import React, {useState} from 'react'

import {MovieList} from '../movie-list/movie-list'
import {MovieListActionPublisherImpl} from '../../redux/actions/movie-list'
import {connect, useDispatch} from 'react-redux'
import {Button, PageHeader} from 'antd'
import './main-page.scss'
import {AuthenticationServiceImpl, MovieListServiceImpl} from '../../services'
import {useFetching} from '../../utils/custom-fetch-hook'
import {LoginActionPublisherImpl} from '../../redux/actions/login'
import { useHistory } from 'react-router-dom'

interface OwnProps {
}

interface State {
    loggedIn?: boolean
}

const WEEK_IN_MS = 604800000

type MainPageProps = State & OwnProps;

const MainPageComponent: React.FC<MainPageProps> = (props: MainPageProps) => {
    const {loggedIn} = props
    const [movieListService] = useState(MovieListServiceImpl.createInstance())
    const [movieListPublisher] = useState(new MovieListActionPublisherImpl(movieListService))
    const [authService] = useState(AuthenticationServiceImpl.createInstance())
    const [loginPublisher] = useState(new LoginActionPublisherImpl(authService))
    const dispatch = useDispatch()

    useFetching(movieListPublisher.getMovieList({
        beginDate: Date.now(),
        endDate: Date.now() + WEEK_IN_MS
    }))

    let history = useHistory();
    const onClick = () => {
        if(loggedIn)
            dispatch(loginPublisher.logout())
        history.push('/login')
    }

    return (
        <div>
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
        </div>
    )
}

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
    loggedIn: state.auth.loggedIn,
    ...ownProps
})

export const MainPage: React.FC<OwnProps> = connect(mapStateToProps)(MainPageComponent)
