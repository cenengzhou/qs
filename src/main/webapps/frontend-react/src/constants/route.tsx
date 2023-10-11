import { HomeOutlined } from '@ant-design/icons'

import { Route } from '../interfaces/Route'
import Home from '../screens/Home'
import { ScreenName } from './screen'

export const ROUTE_BASE_NAME = 'pcms/web'

export const ROUTE_LIST: Route[] = [
  {
    path: '/home',
    name: ScreenName.HOME,
    icon: <HomeOutlined />,
    element: <Home />
  }
]
