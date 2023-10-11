import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { ROUTE_BASE_NAME } from '../../constants/route'
import { Route as RouteType } from '../../interfaces/Route'
import './styles.css'
import { Layout } from 'antd'

interface Props {
  routeList: Array<RouteType>
}

const RouteNavigator = ({ routeList }: Props) => {
  const renderRoutes = () => {
    return routeList.map((item, index) => (
      <Route key={index} path={item.path} element={item.element} />
    ))
  }

  return (
    <BrowserRouter basename={ROUTE_BASE_NAME}>
      <Layout>
        <Layout.Content className="content">
          <Routes>{renderRoutes()}</Routes>
        </Layout.Content>
      </Layout>
    </BrowserRouter>
  )
}

export default RouteNavigator
