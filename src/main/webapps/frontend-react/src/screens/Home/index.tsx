/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { setEnv } from '../../config/appConfig'
import { changeEnv } from '../../redux/appConfigReducer'
import { RootState } from '../../redux/store'
import {
  useGetCurrentUserQuery,
  useGetJobQuery,
  useObtainCacheKeyQuery,
  useObtainUserPreferenceByCurrentUserQuery,
  useValidateCurrentSessionQuery
} from '../../services'
import './style.css'

const Home = () => {
  const env = useSelector((state: RootState) => state.appConfig.env)
  const dispatch = useDispatch()
  const { isLoading, isSuccess, data } =
    useObtainUserPreferenceByCurrentUserQuery(undefined)
  const {
    isLoading: loading,
    isSuccess: success,
    data: user
  } = useGetCurrentUserQuery(undefined)
  const { data: data3 } = useValidateCurrentSessionQuery(undefined)
  const { data: data2 } = useObtainCacheKeyQuery('COMPLETED_JOB_LIST')
  useGetJobQuery('90013')
  useEffect(() => {
    dispatch(changeEnv(setEnv()))
  }, [])

  return (
    <div>
      {env}
      {isLoading && <div>loading</div>}
      {loading && <div>loading22</div>}
      {isSuccess && <div>isSuccess</div>}
      {success && <div>isSuccess222</div>}
      {user && <div>{JSON.stringify(user)}</div>}
      {data && <div>{JSON.stringify(data)}</div>}
      {data2 && <div>{JSON.stringify(data2)}</div>}
      {data3 && <div>{JSON.stringify(data3)}</div>}
    </div>
  )
}

export default Home
