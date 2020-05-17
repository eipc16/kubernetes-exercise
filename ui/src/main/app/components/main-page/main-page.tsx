import React, {useState} from 'react'

import {MovieList} from '../movie-list/movie-list'
import {MovieListActionPublisherImpl} from '../../redux/actions/movie-list'
import {connect} from 'react-redux'
import {PageHeader} from 'antd'
import './main-page.scss'
import {MovieListServiceImpl} from '../../services'
import {useFetching} from '../../utils/custom-fetch-hook'
import {Link} from 'react-router-dom'

interface OwnProps {
}

interface State {
    loggedIn?: boolean,
}

const WEEK_IN_MS = 604800000

type MainPageProps = State & OwnProps;

const MainPageComponent: React.FC<MainPageProps> = (props: MainPageProps) => {
    const {loggedIn} = props
    const [movieListService] = useState(MovieListServiceImpl.createInstance())
    const [movieListPublisher] = useState(new MovieListActionPublisherImpl(movieListService))

    useFetching(movieListPublisher.getMovieList({
        beginDate: Date.now(),
        endDate: Date.now() + WEEK_IN_MS
    }))

    return (
        <div>
            <PageHeader className='site-page-header'
                        title='Cinema tickets sale system'
                        extra={[<Link
                            key={'auth-btn'}
                            className='text-center'
                            to='/login'
                            type='primary'>
                            {loggedIn && 'Log out'}
                            {!loggedIn && 'Log in'}
                        </Link>]}>
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
