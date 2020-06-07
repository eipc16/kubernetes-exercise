import {AuthenticationService} from "../../../../services";
import {TokenInterface} from "../../../../models/authorization/Token";
import {UserIdentifiers} from "../../../../models/users";
import {Resource} from "../../../../models/infrastructure";
import {Available, LoginData, RegistrationData} from "../../../../models/authorization";
import {appConfig} from "../../../../config";
import {handleResponse} from "../../../../services/response-handler";

export class MockAuthenticationService implements AuthenticationService {
    currentToken: TokenInterface | null;

    constructor() {
        this.currentToken = {
            accessToken: "12345",
            tokenType: "Bearer"
        }
    }

    clearToken(): void {
        this.currentToken = null;
    }

    getCurrentToken(): TokenInterface | null {
        return this.currentToken;
    }

    getCurrentTokenAsString(): string {
        const token = this.getCurrentToken();
        return token ? `${token.tokenType} ${token.accessToken}` : '';
    }

    getLocalToken(): TokenInterface | null {
        return this.currentToken;
    }

    getTokenKey(): string {
        return this.currentToken && this.currentToken.accessToken
            ? this.currentToken.accessToken : "";
    }

    getTokenType(): string {
        return this.currentToken && this.currentToken.tokenType
            ? this.currentToken.tokenType : "";
    }

    login(loginData: LoginData): Promise<TokenInterface> {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(loginData)
        };
        this.clearToken();
        return fetch(`${appConfig.apiUrl}/auth/signin`, requestOptions)
            .then(handleResponse)
            .then((token: TokenInterface) => {
                this.currentToken = token;
                if (loginData.remember) {
                    this.saveToken(token)
                }
                return token
            })
    }

    logout(): void {
        this.clearToken();
    }

    register(registrationData: RegistrationData): Promise<Resource> {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(registrationData)
        };

        return fetch(`${appConfig.apiUrl}/auth/signup`, requestOptions)
            .then(handleResponse)
            .then((resource: Resource) => resource)
    }

    isTaken(identifier: UserIdentifiers, value: string): Promise<boolean> {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        };

        return fetch(`${appConfig.apiUrl}/users/check/${identifier}/${value}`, requestOptions)
            .then(handleResponse)
            .then((available: Available) => {
                return available.isAvailable ? available.isAvailable : false
            })
    }

    saveToken(token: TokenInterface): void {
        this.currentToken = token;
    }
}