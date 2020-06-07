import { genreReducer } from '../genre-reducer';
import { genreConstants } from '../../constants';

describe('Genre reducer', () => {
    it('should handle GENRE_REQUEST', () => {
        expect(
            genreReducer({
                isFetching: false,
                isFetched: false,
                genreList: [],
                totalGenres: 0 }, {
                type: genreConstants.GENRES_REQUEST
            })
        ).toEqual(
            {
                isFetching: true,
                isFetched: false,
                genreList: [],
                totalGenres: 0
            }
        )
    })
    it('should handle GENRE_SUCCESS empty list', () => {
        expect(
            genreReducer({
                isFetching: true,
                isFetched: false,
                genreList: [],
                totalGenres: 0}, {
                type: genreConstants.GENRES_SUCCESS, genreList: []
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                genreList: [],
                totalGenres: 0
            }
        )
    })
    it('should handle GENRE_SUCCESS not empty list', () => {
        expect(
            genreReducer({
                isFetching: true,
                isFetched: false,
                genreList: [],
                totalGenres: 0}, {
                type: genreConstants.GENRES_SUCCESS, genreList: [{id: 1, name: 'Test'}]
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                genreList: [{id: 1, name: 'Test'}],
                totalGenres: 1
            }
        )
    })
    it('should handle GENRE_FAILURE', () => {
        expect(
            genreReducer({
                isFetching: true,
                isFetched: false,
                genreList: [],
                totalGenres: 0}, {
                type: genreConstants.GENRES_FAILURE
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: false,
                genreList: [],
                totalGenres: 0
            }
        )
    })
});
