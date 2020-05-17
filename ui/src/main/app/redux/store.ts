import { createStore, applyMiddleware } from 'redux'
import thunkMiddleware from 'redux-thunk'
import { createLogger } from 'redux-logger'
import { createBrowserHistory } from 'history'

import { rootReducer } from './reducers'
import { composeEnhancers } from './utils'

// Create router history
export const history = createBrowserHistory()

// Create middleware
const loggerMiddleware = createLogger()

// Create middleware list
const middlewares = [thunkMiddleware, loggerMiddleware]

// Define initial state
const initialState = {}

// Define enhancer
const enhancer = composeEnhancers(applyMiddleware(...middlewares))

export const store = createStore(
  rootReducer(history),
  initialState,
  enhancer
)
