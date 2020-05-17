import { ObjectState } from '../infrastructure'

export interface MovieInterface {
    id: number;
    imdbId: string;
    objectState: ObjectState;
    releaseDate: Date;
    posterUrl: string;
}
