import { createSlice } from '@reduxjs/toolkit'

import { RootState } from './store'

interface NotificationState {
  visible?: boolean
  mode?: 'Success' | 'Warn' | 'Fail'
  content?: string
}

const initialState: NotificationState = {
  visible: false,
  mode: 'Success',
  content: ''
}
export const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    setNotificationVisible: (
      state,
      action: { payload: NotificationState; type: string }
    ) => {
      state.visible = action.payload.visible
      state.mode = action.payload.mode
      state.content = action.payload.content
    }
  }
})

export const { setNotificationVisible } = notificationSlice.actions

export default notificationSlice.reducer

export const selectIsVisible = (state: RootState) => state.notification
