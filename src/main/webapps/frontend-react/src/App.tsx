/* eslint-disable @typescript-eslint/naming-convention */
import React, { useEffect } from 'react'
import { Provider } from 'react-redux'

import { createSpinner } from '@syncfusion/ej2-react-popups'

import './App.css'
import Header from './components/Header'
import NotificationModal from './components/NotificationModal'
import RouteNavigator from './components/RouteNavigator'
import Loading from './components/Spinner'
import { ROUTE_LIST } from './constants/route'
import { persistor, store } from './redux/store'
import { PersistGate } from 'redux-persist/integration/react'

const App = () => {
  useEffect(() => {
    createSpinner({ target: document.getElementById('root')! })
  }, [])

  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}></PersistGate>
      <React.Fragment>
        <Header></Header>
        <RouteNavigator routeList={ROUTE_LIST} />
        <Loading />
        <NotificationModal />
      </React.Fragment>
    </Provider>
  )
}

export default App
