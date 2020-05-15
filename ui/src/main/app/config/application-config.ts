interface Config {
    apiUrl: string;
    omdbApiUrl: string;
}

export const appConfig: Config = {
    apiUrl: 'http://localhost:8080/api',
    omdbApiUrl: 'http://www.omdbapi.com/'
};