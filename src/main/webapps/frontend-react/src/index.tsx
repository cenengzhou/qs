import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'

import { registerLicense } from '@syncfusion/ej2-base'

import App from './App'
import { ROUTE_BASE_NAME } from './constants/route'
import './index.css'
import reportWebVitals from './reportWebVitals'

registerLicense(
  'Ngo9BigBOggjHTQxAR8/V1NHaF5cXmVCf1FpRmJGdld5fUVHYVZUTXxaS00DNHVRdkdgWH9ccHRVRmhdV01/X0Y='
)

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement)
root.render(
  <React.StrictMode>
    <BrowserRouter basename={ROUTE_BASE_NAME}>
      <App />
    </BrowserRouter>
  </React.StrictMode>
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals()
