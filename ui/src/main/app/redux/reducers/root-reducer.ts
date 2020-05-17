import { combineReducers } from 'redux'
import { History } from 'history'
import { connectRouter } from 'connected-react-router'

// Reducers
import { authorizationReducer } from './login-reducer'
import { registrationReducer } from './registration-reducer'
import { currentUserReducer } from './user-reducer'
import { alertReducer } from './alert-reducer'
import { movieListReducer } from './movie-list-reducer'

export const rootReducer = (history: History) => combineReducers({
  router: connectRouter(history),
  auth: authorizationReducer,
  registration: registrationReducer,
  currentUser: currentUserReducer,
  alerts: alertReducer,
  movieList: movieListReducer
})
