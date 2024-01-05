import { createSlice } from '@reduxjs/toolkit'

import { RootState } from './store'

interface NotificationState {
  isVisible: boolean
  mode: 'Success' | 'Warn' | 'Fail'
  content: string
}

const initialState: NotificationState = {
  isVisible: false,
  mode: 'Success',
  content: ''
}
export const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    setNotificationVisible: (state, payload) => {
      state.isVisible = payload.payload
    },
    setNotificationMode: (state, payload) => {
      state.mode = payload.payload
    },
    setNotificationContent: (state, payload) => {
      state.content = payload.payload
    }
  }
})

export const {
  setNotificationVisible,
  setNotificationMode,
  setNotificationContent
} = notificationSlice.actions

export default notificationSlice.reducer

export const selectIsVisible = (state: RootState) =>
  state.notification.isVisible
export const selectMode = (state: RootState) => state.notification.mode
export const selectContent = (state: RootState) => state.notification.content
