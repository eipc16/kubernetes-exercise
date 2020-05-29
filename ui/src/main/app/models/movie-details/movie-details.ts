import { ObjectState } from '../infrastructure'

export interface MovieDetailsInterface {
    id: number;
    title: string;
    imdbId: string;
    objectState: ObjectState;
    releaseDate: Date;
    posterUrl: string;
    year: string;
    maturityRate: string;
    runtime: string;
    director: string;
    actors: string;
    plot: string;
    language: string;
    country: string;
    genres: string[];
}
