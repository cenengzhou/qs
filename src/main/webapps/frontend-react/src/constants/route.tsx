import { Route } from '../interfaces/Route'
import Admin from '../screens/Admin'
import Home from '../screens/Home'
import Reports from '../screens/Reports'
import { ScreenName } from './screen'

export const ROUTE_BASE_NAME = 'pcms/web'

export const ROUTE_LIST: Route[] = [
  {
    path: '/home',
    name: ScreenName.HOME,
    element: <Home />
  },
  {
    path: '/admin/*',
    name: ScreenName.ADMIN,
    element: <Admin />
  },
  {
    path: '/Reports',
    name: ScreenName.REPORTS,
    element: <Reports />
  },
  {
    path: '/',
    name: ScreenName.HOME,
    element: <Home />
  }
]
