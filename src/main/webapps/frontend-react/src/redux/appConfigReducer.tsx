import { createSlice } from '@reduxjs/toolkit'

import { ReducerName } from '../interfaces/Redux'
import { CurrentUser } from '../services'

type InitialState = {
  env?: string
  currentUser?: CurrentUser
  rolesList?: string[]
}

const initialState: InitialState = {
  env: 'PRO',
  currentUser: {},
  rolesList: []
}

export const appConfigSlice = createSlice({
  name: ReducerName.APP_CONFIG,
  initialState,
  reducers: {
    changeEnv: (state, action) => {
      return { ...state, env: action.payload }
    },
    currentUser: (state, action) => {
      return { ...state, currentUser: action.payload }
    },
    hasRolesList: (state, action) => {
      return { ...state, rolesList: action.payload }
    }
  }
})

export const { changeEnv, currentUser, hasRolesList } = appConfigSlice.actions
export default appConfigSlice.reducer
