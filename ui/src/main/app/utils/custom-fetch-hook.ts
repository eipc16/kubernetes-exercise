import { Action, Dispatch } from 'redux'
import { useDispatch } from 'react-redux'
import { useEffect } from 'react'

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useFetching = (actionCreator: (dispatch: Dispatch<Action>) => void, deps?: any[]): void => {
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(actionCreator);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  },deps ? deps : [])
};
