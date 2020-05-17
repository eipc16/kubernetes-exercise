import React, { useState } from 'react'

import { MovieListComponent } from '../movie-list/movie-list'
import { MovieListActionPublisherImpl } from '../../redux/actions/movie-list'
import { connect } from 'react-redux'
import { MovieList } from '../../models/movies-list'
import { PageHeader } from 'antd'
import './main-page.scss'
import { MovieListServiceImpl } from '../../services'
import { useFetching } from '../../utils/custom-fetch-hook'
import { Link } from 'react-router-dom'

interface OwnProps {
}

interface State {
    loggedIn?: boolean,
    isFetched?: boolean;
    isFetching?: boolean;
    movieList: MovieList
}
const WEEK_IN_MS = 604800000

type MainPageProps = State & OwnProps;

const MainPageComponent : React.FC<MainPageProps> = (props: MainPageProps) => {
  const { loggedIn, movieList } = props
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
          className='text-center'
          to='/login'
          type='primary'>
          {loggedIn && 'Log out'}
          {!loggedIn && 'Log in'}
        </Link>]}>
      </PageHeader>
      { movieList && <MovieListComponent list={movieList.list}/> }
    </div>
  )
}

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
  loggedIn: state.auth.loggedIn,
  isFetched: state.movieList.isFetched,
  isFetching: state.movieList.isFetching,
  movieList: state.movieList.movieList,
  ...ownProps
})

export const MainPage: React.FC<OwnProps> = connect(mapStateToProps)(MainPageComponent)
