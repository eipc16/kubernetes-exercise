import React from 'react'
import { Route, Redirect, RouteProps } from 'react-router-dom'

export interface PrivateRouteProps extends RouteProps {
    isAuthenticated: boolean;
    authenticationPath: string;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = props => {
  let redirectPath: string = ''
  const { isAuthenticated, authenticationPath } = props
  if (!isAuthenticated) {
    redirectPath = authenticationPath
  }

  if (redirectPath) {
    const redirectComponent =
            () => <Redirect to= { { pathname: redirectPath } } />
    return <Route { ...props }
      component={redirectComponent}
      render={undefined}
    />
  }
  return <Route { ...props } />
}
