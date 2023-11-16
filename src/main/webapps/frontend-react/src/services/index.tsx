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
          url: '/service/userPreference/obtainUserPreferenceByCurrentUser'
        })
      })
    }
  }
})

export const { useObtainUserPreferenceByCurrentUserQuery } = apiSlice
export default apiSlice
