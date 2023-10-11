import { configureStore } from '@reduxjs/toolkit'

import combinedReducer from './'
import logger from 'redux-logger'
import { persistReducer, persistStore } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const persistConfig = {
  key: 'root',
  storage
}
const persistedReducer = persistReducer(persistConfig, combinedReducer)
const store = configureStore({
  reducer: persistedReducer,
  middleware: [logger]
})
const persistor = persistStore(store)

export type RootDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>
export { store, persistor }
