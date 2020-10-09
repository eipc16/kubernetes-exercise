interface Config {
    apiUrl: string;
    omdbApiUrl: string;
    locale: string;
}

export const appConfig: Config = {
  apiUrl: 'http://localhost:8081/cinema-tickets-app/api',
  omdbApiUrl: 'http://www.omdbapi.com/',
  locale: 'pl-PL'
}