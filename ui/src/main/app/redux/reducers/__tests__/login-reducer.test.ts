import { authorizationReducer } from "../login-reducer";
import {AuthenticationServiceImpl} from "../../../services"
import { loginConstants} from "../../constants";

const authService = AuthenticationServiceImpl.createInstance()
const localToken = authService.getLocalToken()
const initialState =
    localToken ? { loggedIn: true, token: localToken } : {}

describe('Authorization reducer', () => {
    it('should handle ADD_TODO', () => {
        expect(
            authorizationReducer(initialState, {
                type: loginConstants.LOGIN_SUCCESS
            })
        ).toEqual([
            {
                loggedIn: true,
                loggingIn: false
            }
        ])
    })
})