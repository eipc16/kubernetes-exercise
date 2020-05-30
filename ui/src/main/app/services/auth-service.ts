import { LoginData, RegistrationData, Token, Available } from '../models/authorization'
import { UserIdentifiers } from '../models/users'
import { Resource } from '../models/infrastructure'
import { handleResponse } from './response-handler'
import { appConfig } from '../config'

export interface AuthenticationService {
    // Token actions
    getTokenKey(): string;
    getTokenType(): string;
    getLocalToken(): Token | null;
    getCurrentToken(): Token | null;
    getCurrentTokenAsString(): string;
    saveToken(token: Token): void;
    clearToken(): void;

    // login / logout
    login(loginData: LoginData): Promise<Token>;
    logout(): void;

    // register
    register(registrationData: RegistrationData): Promise<Resource>;

    // validation
    isTaken(identifier: UserIdentifiers, value: string): Promise<boolean>;
}

export class AuthenticationServiceImpl implements AuthenticationService {
    static authService: AuthenticationService;
    currentToken?: Token | null;
    tokenKey: string = 'user';
    tokenType: string = 'Bearer';

    private constructor () {
    }

    static createInstance () {
      if (!AuthenticationServiceImpl.authService) {
        AuthenticationServiceImpl.authService = new AuthenticationServiceImpl()
      }
      return AuthenticationServiceImpl.authService
    }

    getTokenKey (): string {
      return this.tokenKey
    }

    getTokenType (): string {
      return this.tokenType
    }

    getLocalToken (): Token | null {
      const user = localStorage.getItem(this.tokenKey)
      const userObject = user ? JSON.parse(user) : {}
      console.log('User Object: ', userObject)
      if (userObject && userObject.accessToken) {
        return {
          tokenType: this.tokenType,
          accessToken: userObject.accessToken
        }
      }
      return null
    }

    getCurrentToken (): Token | null {
      if (!this.currentToken) {
        this.currentToken = this.getLocalToken()
      }
      return this.currentToken
    }

    getCurrentTokenAsString(): string {
        const token = this.getCurrentToken();
        return token ? `${token.tokenType} ${token.accessToken}` : '';
    }

    saveToken (token: Token) {
      localStorage.setItem(this.tokenKey, JSON.stringify(token))
    }

    clearToken () {
      this.currentToken = null
      localStorage.removeItem(this.tokenKey)
    }

    login (loginData: LoginData): Promise<Token> {
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginData)
      }
      this.clearToken()
      return fetch(`${appConfig.apiUrl}/auth/signin`, requestOptions)
        .then(handleResponse)
        .then((token: Token) => {
          this.currentToken = token
          if (loginData.remember) {
            this.saveToken(token)
          }
          return token
        })
    }

    logout () {
      this.clearToken()
    }

    register (registrationData: RegistrationData): Promise<Resource> {
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registrationData)
      }

      return fetch(`${appConfig.apiUrl}/auth/signup`, requestOptions)
        .then(handleResponse)
        .then((resource: Resource) => resource)
    }

    isTaken (identifier: UserIdentifiers, value: string): Promise<boolean> {
      const requestOptions = {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      }

      return fetch(`${appConfig.apiUrl}/users/check/${identifier}/${value}`, requestOptions)
        .then(handleResponse)
        .then((available: Available) => {
          return available.isAvailable ? available.isAvailable : false
        })
    }
}
