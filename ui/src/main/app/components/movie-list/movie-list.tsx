import React from 'react'
import { Col, Row } from 'antd'
import { Movie } from '../../models/movies-list'
import {connect} from "react-redux";
import {MovieListActionPublisher} from "../../redux/actions/movie-list";
import {MovieListEntry} from "./movie-list-entry/movie-list-entry";

import './movie-list.scss';

interface OwnProps {
  movieListPublisher: MovieListActionPublisher
}

interface State {
  isFetched?: boolean;
  isFetching?: boolean;
  movieList: Movie[]
}

type MovieListProps = State & OwnProps

const MovieListComponent = (props: MovieListProps) => {
  const { isFetching, movieList } = props

  if(isFetching) {
    return <div>Fetching..</div>
  }

  return (
      <div className='main--list--container'>
        <Row gutter={[16, 16]}>
          {movieList && movieList.map(movie =>
              <Col key={movie.imdbId}>
                <MovieListEntry movie={movie} />
              </Col>
          )}
        </Row>
      </div>
  )
}

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
  isFetched: state.movieList.isFetched,
  isFetching: state.movieList.isFetching,
  movieList: state.movieList.movieList,
  ...ownProps
})

export const MovieList: React.FC<OwnProps> = connect(mapStateToProps)(MovieListComponent)
