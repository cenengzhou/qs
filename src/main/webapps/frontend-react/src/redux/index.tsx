import { combineReducers } from '@reduxjs/toolkit'

import { ReducerName } from '../interfaces/Redux'
import apiSlice from '../services'
import appConfigReducer from './appConfigReducer'
import loadingReducer from './loadingReducer'

export default combineReducers({
  [apiSlice.reducerPath]: apiSlice.reducer,
  [ReducerName.APP_CONFIG]: appConfigReducer,
  loading: loadingReducer
})
