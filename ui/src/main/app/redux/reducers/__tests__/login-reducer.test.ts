import { authorizationReducer } from '../login-reducer'
import { loginConstants } from '../../constants'

describe('Authorization reducer', () => {
    it('should handle LOGIN_REQUEST', () => {
        expect(
            authorizationReducer({ loggedIn: false, loggingIn: false, token: undefined }, {
                type: loginConstants.LOGIN_REQUEST
            })
        ).toEqual(
            {
                loggingIn: true
            }
        )
    })
    it('should handle LOGIN_FAILURE', () => {
        expect(
            authorizationReducer({ loggedIn: false, loggingIn: false, token: undefined }, {
                type: loginConstants.LOGIN_FAILURE
            })
        ).toEqual(
            {
                loggingIn: false
            }
        )
    })
    it('should handle LOGIN_SUCCESS', () => {
        expect(
            authorizationReducer({ loggedIn: false, loggingIn: true, token: undefined }, {
                type: loginConstants.LOGIN_SUCCESS, token: {accessToken: 'a', tokenType: 'a'}
            })
        ).toEqual(
            {
                loggingIn: false,
                loggedIn: true,
                token: {accessToken: 'a', tokenType: 'a'}
            }
        )
    })
    it('should handle LOGOUT', () => {
        expect(
            authorizationReducer({ loggedIn: true, loggingIn: false, token: undefined }, {
                type: loginConstants.LOGOUT
            })
        ).toEqual(
            {
                loggedIn: false
            }
        )
    })
});
