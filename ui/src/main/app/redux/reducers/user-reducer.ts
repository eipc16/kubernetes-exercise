import { userConstants } from '../constants'
import { CurrentUserAction, CurrentUserSuccessActionInterface } from '../actions/user'
import { User } from '../../models/users'

const initialState = {}

export interface CurrentUserState {
    fetching?: boolean;
    fetched?: boolean;
    data?: User;
}

export function currentUserReducer (state: CurrentUserState = initialState, action: CurrentUserAction): CurrentUserState {
  switch (action.type) {
    case userConstants.CURRENT_USER_REQUEST:
      return {
        fetching: true
      }
    case userConstants.CURRENT_USER_SUCCESS:
      return {
        fetched: true,
        data: (action as CurrentUserSuccessActionInterface).userData
      }
    case userConstants.CURRENT_USER_FAILURE:
      return {}
    default:
      return state
  }
}
