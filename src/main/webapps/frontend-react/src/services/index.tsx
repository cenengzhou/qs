import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'

const apiSlice = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: '/pcms'
  }),
  endpoints: builder => {
    return {
      obtainUserPreferenceByCurrentUser: builder.query({
        query: () => ({
          method: 'POST',
          url: 'service/userPreference/obtainUserPreferenceByCurrentUser'
        })
      }),
      getCurrentUser: builder.query({
        query: () => ({
          method: 'GET',
          url: 'service/security/getCurrentUser'
        })
      }),
      validateCurrentSession: builder.query({
        query: () => ({
          method: 'POST',
          url: 'service/system/ValidateCurrentSession'
        })
      }),
      obtainCacheKey: builder.query({
        query: queryArg => ({
          method: 'POST',
          url: 'service/system/obtainCacheKey',
          body: queryArg
        })
      }),
      getNotificationReadStatusByCurrentUser: builder.query({
        query: () => ({
          method: 'GET',
          url: 'service/userPreference/getNotificationReadStatusByCurrentUser'
        })
      }),
      getProperties: builder.query({
        query: () => ({
          method: 'POST',
          url: 'service/system/getProperties'
        })
      }),
      getJob: builder.query({
        query: queryArg => ({
          method: 'GET',
          url: 'service/job/getJob',
          params: { jobNo: queryArg }
        })
      })
    }
  }
})

export const {
  useObtainUserPreferenceByCurrentUserQuery,
  useGetCurrentUserQuery,
  useValidateCurrentSessionQuery,
  useObtainCacheKeyQuery,
  useGetNotificationReadStatusByCurrentUserQuery,
  useGetPropertiesQuery,
  useGetJobQuery
} = apiSlice
export default apiSlice
