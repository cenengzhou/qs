import { createSlice } from '@reduxjs/toolkit'

import { ReducerName } from '../interfaces/Redux'
import { CurrentUser } from '../services'

type InitialState = {
  env?: string
  currentUser?: CurrentUser
}

const initialState: InitialState = {
  env: 'PRO',
  currentUser: {}
}

export const appConfigSlice = createSlice({
  name: ReducerName.APP_CONFIG,
  initialState,
  reducers: {
    changeEnv: (state, action) => {
      return { ...state, env: action.payload }
    },
    setCurrentUser: (state, action) => {
      return { ...state, currentUser: action.payload }
    }
  }
})

export const { changeEnv, setCurrentUser } = appConfigSlice.actions
export default appConfigSlice.reducer
