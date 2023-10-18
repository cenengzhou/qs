import { Route } from '../interfaces/Route'
import Home from '../screens/Home'
import Admin from '../screens/admin/Revisisons'
import { ScreenName } from './screen'

export const ROUTE_BASE_NAME = 'pcms/web'

export const ROUTE_LIST: Route[] = [
  {
    path: "/home",
    name: ScreenName.HOME,
    element: <Home />,
  },
  {
    path: "/admin",
    name: ScreenName.Admin,
    element: <Admin />,
  },
  {
    path: "/",
    name: ScreenName.HOME,
    element: <Home />,
  },
];
