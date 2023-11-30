import { configureStore } from '@reduxjs/toolkit'

import apiSlice from '../services'
import combinedReducer from './'
import logger from 'redux-logger'
import {
  FLUSH,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
  REHYDRATE,
  persistReducer,
  persistStore
} from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const persistConfig = {
  key: 'root',
  storage,
  blacklist: [apiSlice.reducerPath]
}
const persistedReducer = persistReducer(persistConfig, combinedReducer)
const store = configureStore({
  reducer: persistedReducer,
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER]
      }
    }).concat(logger, apiSlice.middleware)
})
const persistor = persistStore(store)

export type RootDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>
export { store, persistor }
