import { Action, Dispatch } from 'redux'
import { useDispatch } from 'react-redux'
import { useEffect } from 'react'

export const useFetching = (actionCreator: (dispatch: Dispatch<Action>) => void, deps?: any[]) => {
  const dispatch = useDispatch()
  useEffect(() => {
    dispatch(actionCreator)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps ? deps : [])
}
