import React, { useEffect } from 'react'
import { Provider } from 'react-redux'

import { createSpinner } from '@syncfusion/ej2-react-popups'

import './App.css'
import Header from './components/Header'
import RouteNavigator from './components/RouteNavigator'
import { ROUTE_LIST } from './constants/route'
import { persistor, store } from './redux/store'
import { PersistGate } from 'redux-persist/integration/react'

const App = () => {
  useEffect(() => {
    createSpinner({ target: document.getElementById('root') as HTMLElement })
  }, [])
  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}></PersistGate>
      <React.Fragment>
        <Header></Header>
        <RouteNavigator routeList={ROUTE_LIST} />
      </React.Fragment>
    </Provider>
  )
}

export default App
