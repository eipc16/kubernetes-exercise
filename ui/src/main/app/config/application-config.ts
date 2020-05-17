interface Config {
    apiUrl: string;
    omdbApiUrl: string;
    locale: string;
}

export const appConfig: Config = {
  apiUrl: 'http://localhost:8080/cinema-tickets-app/api',
  omdbApiUrl: 'http://www.omdbapi.com/',
  locale: 'pl-PL'
}
