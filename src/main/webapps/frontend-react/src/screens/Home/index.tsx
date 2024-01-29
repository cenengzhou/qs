import { Route, Routes } from 'react-router-dom'

import { ROUTE_LIST } from './route'
import './style.css'

const Home = () => {
  return (
    <Routes>
      {ROUTE_LIST.map((item, index) => (
        <Route key={index} path={item.path} element={item.element} />
      ))}
    </Routes>
  )
}

export default Home
