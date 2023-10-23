import { Route } from '../../interfaces/Route'
import Revisions from './Revisisons'
import Session from './Session'

export const ROUTE_LIST: Route[] = [
  {
    path: '/session',
    name: 'SESSION',
    element: <Session />
  },
  {
    path: '/revisisons',
    name: 'REVISISONS',
    element: <Revisions />
  },
  {
    path: '/',
    name: 'REVISISONS',
    element: <Revisions />
  }
]
