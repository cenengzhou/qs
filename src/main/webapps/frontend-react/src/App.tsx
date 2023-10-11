import React from 'react'
import { Provider } from 'react-redux'

import './App.css'
import RouteNavigator from './components/RouteNavigator'
import { ROUTE_LIST } from './constants/route'
import { persistor, store } from './redux/store'
import { PersistGate } from 'redux-persist/integration/react'

const App = () => {
  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}></PersistGate>
      <React.Fragment>
        <RouteNavigator routeList={ROUTE_LIST} />
      </React.Fragment>
    </Provider>
  )
}

export default App
