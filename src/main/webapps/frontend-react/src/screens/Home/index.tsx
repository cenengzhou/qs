/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { setEnv } from '../../config/appConfig'
import { changeEnv } from '../../redux/appConfigReducer'
import { RootState } from '../../redux/store'
import { useObtainUserPreferenceByCurrentUserQuery } from '../../services'
import './style.css'

const Home = () => {
  const env = useSelector((state: RootState) => state.appConfig.env)
  const dispatch = useDispatch()
  const { isLoading, isSuccess, data } =
    useObtainUserPreferenceByCurrentUserQuery(null)
  useEffect(() => {
    dispatch(changeEnv(setEnv()))
  }, [])

  return (
    <div>
      {env}
      {isLoading && <div>loading</div>}
      {isSuccess && <div>isSuccess</div>}
      {data && <div>{data.toString()}</div>}
    </div>
  )
}

export default Home
