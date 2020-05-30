import {handleResponse} from './response-handler'
import {appConfig} from '../config'
import {AuthenticationService, AuthenticationServiceImpl} from "./auth-service";
import {Screening} from "../models/screening";

export interface ScreeningService {
    fetchScreenings(movieId: number): Promise<Screening[]>;
}

export class ScreeningServiceImpl implements ScreeningService {
    static screeningService: ScreeningService;
    private authService: AuthenticationService;

    private constructor(authenticationService?: AuthenticationService) {
        this.authService = authenticationService || AuthenticationServiceImpl.createInstance();
    }

    static createInstance(authenticationService?: AuthenticationService) {
        if (!ScreeningServiceImpl.screeningService) {
            ScreeningServiceImpl.screeningService = new ScreeningServiceImpl(authenticationService)
        }
        return ScreeningServiceImpl.screeningService;
    }

    fetchScreenings(movieId: number): Promise<Screening[]> {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'application/json', 'Authorization': this.authService.getCurrentTokenAsString()}
        }
        return fetch(`${appConfig.apiUrl}/screening/movie/${movieId}`, requestOptions)
            .then(handleResponse)
            .then((screenings: Screening[]) => screenings)
    }
}
