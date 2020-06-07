import { compose } from 'redux'

declare global {
    interface Window {
        // eslint-disable-next-line no-undef
        __REDUX_DEVTOOLS_EXTENSION_COMPOSE__?: typeof compose;
    }
}

export const composeEnhancers =
    (process.env.NODE_ENV === 'development' &&
        window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__'] as typeof compose || compose);
