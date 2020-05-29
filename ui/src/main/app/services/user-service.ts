import { User } from '../models/users'
import { handleResponse } from './response-handler'
import { appConfig } from '../config'

export interface UserService {
    // Current user
    getCurrentUser(): Promise<User>;
}

export class UserServiceImpl implements UserService {
  getCurrentUser (): Promise<User> {
    return fetch(`${appConfig.apiUrl}/user/current`)
      .then(handleResponse)
      .then((user: User) => user)
  }
}
