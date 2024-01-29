import { createSlice } from '@reduxjs/toolkit'

import { ReducerName } from '../interfaces/Redux'
import { CurrentUser } from '../services'

type InitialState = {
  env?: string
  currentUser?: CurrentUser
  rolesList?: string[]
  jobNo?: string
  jobDescription?: string
  jobList?: string
}

const initialState: InitialState = {
  env: 'PRO',
  currentUser: {},
  rolesList: [],
  jobNo: '',
  jobDescription: '',
  jobList: ''
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
    },
    setJobNo: (state, action) => {
      return { ...state, jobNo: action.payload }
    },
    setJobDescription: (state, action) => {
      return { ...state, jobDescription: action.payload }
    },
    setJobList: (state, action) => {
      return { ...state, jobList: action.payload }
    }
  }
})

export const {
  changeEnv,
  currentUser,
  hasRolesList,
  setJobNo,
  setJobDescription
} = appConfigSlice.actions
export default appConfigSlice.reducer
