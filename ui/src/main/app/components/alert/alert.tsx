import React, { useState } from 'react'
import { connect, useDispatch } from 'react-redux'
import { Alert } from '../../models/infrastructure'

import { Alert as AntdAlert } from 'antd'
import { AlertTypes } from '../../models/infrastructure/Alert'
import { AlertPublisher, AlertPublisherImpl } from '../../redux/actions/alert'
import './alert.scss'
import {ReduxStore} from "../../redux/reducers/root-reducer";

interface OwnProps {
    component: string;
    alertPublisher?: AlertPublisher;
}

interface State {
    alerts: Alert[];
}

export type AlertProps = State & OwnProps;
export type AntdAlertType = 'success' | 'info' | 'warning' | 'error' | undefined;

const AlertContainerComponent: React.FC<AlertProps> = (props: AlertProps): JSX.Element => {
  const dispatch = useDispatch();
  const [alertPublisher] = useState(props.alertPublisher
    ? props.alertPublisher : AlertPublisherImpl.createInstance());
  const alerts = props.alerts;

  const getAlertTitle = (alertType: AlertTypes): (string | undefined)[] => {
    switch (alertType) {
      case AlertTypes.SUCCESS:
        return ['success', 'Success'];
      case AlertTypes.INFO:
        return ['info', 'Info'];
      case AlertTypes.WARNING:
        return ['warning', 'Warning'];
      case AlertTypes.ERROR:
        return ['error', 'Error'];
      default:
        return [undefined, 'Unknown'];
    }
  };

  const getSingleAlert = (alertData: Alert): JSX.Element => {
    const [type] = getAlertTitle(alertData.type);
    const alertType = type as AntdAlertType;

    return (
      <div className='single--alert'>
        <AntdAlert
          key={alertData.id}
          message={alertData.message}
          type={alertType}
          showIcon={true}
          closable={alertData.canDismiss}
          afterClose={(): void => { dispatch(alertPublisher.dismissAlert(alertData.component, alertData.id))}}
        />
      </div>
    )
  };

  return (
    alerts && alerts.length > 0
      ? (
        <div className='alert-container'>
          {alerts.map(alert => getSingleAlert(alert))}
        </div>
      ) : null
  )
}

const mapStateToProps = (state: ReduxStore, ownProps: OwnProps): AlertProps => ({
  alerts: state.alerts[ownProps.component],
  ...ownProps
});

export const AlertContainer: React.FC<OwnProps> = connect(mapStateToProps)(AlertContainerComponent);
