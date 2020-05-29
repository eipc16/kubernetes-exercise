import {alertReducer} from '../alert-reducer'
import {alertConstants} from '../../constants'
import {AlertTypes} from "../../../models/infrastructure/Alert";

describe('Alert reducer', () => {
    it('should handle PUSH_ALERT', () => {
        expect(
            alertReducer({}, {
                type: alertConstants.PUSH_ALERT,
                alertData: {message: '', canDismiss: false, type: AlertTypes.SUCCESS, duration: 10, component: 'a',
                    id: '12'},
                alertId: '12' , component: 'a'
            })
        ).toEqual({
            a: [{message: '', canDismiss: false, type: AlertTypes.SUCCESS, duration: 10, component: 'a',
                        id: '12'}]
        })
    })
    it('should handle DISMISS_ALERT', () => {
        expect(
            alertReducer({}, {
                type: alertConstants.DISMISS_ALERT,
                alertData: {message: '', canDismiss: false, type: AlertTypes.SUCCESS, duration: 10, component: 'a',
                    id: '12'},
                alertId: '12' , component: 'a'
            })
        ).toEqual(
            {}
        )
    })
})