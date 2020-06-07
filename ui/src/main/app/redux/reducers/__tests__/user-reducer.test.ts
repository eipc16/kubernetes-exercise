import { currentUserReducer } from '../user-reducer';
import { userConstants } from '../../constants';

describe('User reducer', () => {
    it('should handle USER_REQUEST', () => {
        expect(
            currentUserReducer({}, {
                type: userConstants.CURRENT_USER_REQUEST
            })
        ).toEqual(
            {
                fetching: true
            }
        )
    });
    it('should handle USER_SUCCESS', () => {
        expect(
            currentUserReducer({
                fetching: true}, {
                type: userConstants.CURRENT_USER_SUCCESS, userData: {name: '', email: '', id: 1, password: '',
                surname: '', phoneNumber: '', role: '', username: ''}
            })
        ).toEqual(
            {
                fetched: true,
                data:  {name: '', email: '', id: 1, password: '',
                    surname: '', phoneNumber: '', role: '', username: ''}
            }
        )
    });
    it('should handle USER_FAILURE', () => {
        expect(
            currentUserReducer({
                fetching: true}, {
                type: userConstants.CURRENT_USER_FAILURE
            })
        ).toEqual(
            {

            }
        )
    })
});
