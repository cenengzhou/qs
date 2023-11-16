import { createSlice } from '@reduxjs/toolkit'

import { ReducerName } from '../interfaces/Redux'

const initialState = {
  env: 'PRO'
}

export const appConfigSlice = createSlice({
  name: ReducerName.APP_CONFIG,
  initialState,
  reducers: {
    changeEnv: (state, action) => {
      return { ...state, env: action.payload }
    }
  }
})

export const { changeEnv } = appConfigSlice.actions
export default appConfigSlice.reducer
