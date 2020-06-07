import { registrationReducer } from '../registration-reducer'
import { registerConstants } from "../../constants";

describe('Registration reducer', () => {
    it('should handle REGISTER_REQUEST', () => {
        expect(
            registrationReducer({
                    registered: false,
                    registering: false,
                    resource: undefined}, {
                type: registerConstants.REGISTER_REQUEST, registrationData: {name: "", surname: "", email: "",
                    password: "", phoneNumber: "", username: "" } })
        ).toEqual(
            {
                registering: true,
            }
        )
    })
    it('should handle REGISTER_SUCCESS', () => {
        expect(
            registrationReducer({
                registered: false,
                registering: true,
                resource: undefined }, {
                type: registerConstants.REGISTER_SUCCESS, registrationData: {name: "", surname: "", email: "",
                    password: "", phoneNumber: "", username: "" }
            })
        ).toEqual(
            {
                registered: true
            }
        )
    })
    it('should handle REGISTER_FAILURE', () => {
        expect(
            registrationReducer({
                registered: false,
                registering: true,
                resource: undefined}, {
                type: registerConstants.REGISTER_FAILURE, registrationData: {name: "", surname: "", email: "",
                    password: "", phoneNumber: "", username: "" }
            })
        ).toEqual(
            {

            }
        )
    })
});
