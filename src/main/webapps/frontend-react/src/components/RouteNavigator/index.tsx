import React from 'react'
import { Route, Routes } from 'react-router-dom'

import { Route as RouteType } from '../../interfaces/Route'
import './styles.css'

interface Props {
  routeList: Array<RouteType>
}

const RouteNavigator = ({ routeList }: Props) => {
  const renderRoutes = () => {
    return routeList.map((item, index) => (
      <Route key={index} path={item.path} element={item.element} />
    ))
  }

  return <Routes>{renderRoutes()}</Routes>
}

export default RouteNavigator
