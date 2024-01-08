import { createSlice } from '@reduxjs/toolkit'

import { RootState } from './store'

export const loadingSlice = createSlice({
  name: 'loading',
  initialState: {
    isLoading: false
  },
  reducers: {
    closeLoading: state => {
      state.isLoading = false
    },
    openLoading: state => {
      state.isLoading = true
    }
  }
})

export const { closeLoading, openLoading } = loadingSlice.actions

export default loadingSlice.reducer

export const selectIsLoading = (state: RootState) => state.loading.isLoading
