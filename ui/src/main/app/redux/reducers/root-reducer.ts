import {combineReducers} from 'redux'
import {History} from 'history'
import {connectRouter} from 'connected-react-router'
// Reducers
import {authorizationReducer} from './login-reducer'
import {registrationReducer} from './registration-reducer'
import {currentUserReducer} from './user-reducer'
import {alertReducer} from './alert-reducer'
import {movieListReducer} from './movie-list-reducer'
import {genreReducer} from "./genre-reducer";
import {movieDetailsReducer} from "./movie-details";
import {seatsReducer} from './seats-reducer';
import {screeningReducer} from './screening-reducer';

export interface ReduxStore {
  router: ReturnType<typeof connectRouter>;
  auth: ReturnType<typeof authorizationReducer>;
  registration: ReturnType<typeof registrationReducer>;
  currentUser: ReturnType<typeof currentUserReducer>;
  alerts: ReturnType<typeof alertReducer>;
  movieList: ReturnType<typeof movieListReducer>;
  genres: ReturnType<typeof genreReducer>;
  movieDetails: ReturnType<typeof movieDetailsReducer>;
  screeningSeats: ReturnType<typeof seatsReducer>;
  movieScreenings: ReturnType<typeof screeningReducer>;
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const rootReducer = (history: History): any => combineReducers({
  router: connectRouter(history),
  auth: authorizationReducer,
  registration: registrationReducer,
  currentUser: currentUserReducer,
  alerts: alertReducer,
  movieList: movieListReducer,
  genres: genreReducer,
  movieDetails: movieDetailsReducer,
  screeningSeats: seatsReducer,
  movieScreenings: screeningReducer
});