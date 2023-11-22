/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import React from 'react'
import { useSelector } from 'react-redux'

import { RootState } from '../../redux/store'
import {
  useGetCurrentUserQuery,
  useGetJobDatesQuery,
  useGetJobQuery,
  useGetRepackagingQuery,
  useObtainCacheKeyQuery,
  useObtainUserPreferenceByCurrentUserQuery,
  useValidateCurrentSessionQuery
} from '../../services'
import './style.css'

const Home = () => {
  const env = useSelector((state: RootState) => state.appConfig.env)
  const { isLoading, isSuccess, data } =
    useObtainUserPreferenceByCurrentUserQuery(undefined)
  const {
    isLoading: loading,
    isSuccess: success,
    data: user
  } = useGetCurrentUserQuery(undefined)
  const { data: data3 } = useValidateCurrentSessionQuery(undefined)
  const { data: data2 } = useObtainCacheKeyQuery('COMPLETED_JOB_LIST')
  useGetJobQuery({ jobNo: '90013' })
  useGetJobDatesQuery({ jobNo: '90013' })
  useGetRepackagingQuery({ jobNo: '90013', version: 1 })

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
