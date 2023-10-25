import { Route } from '../../interfaces/Route'
import ManualProcedures from './ManualProcedures'
import Revisions from './Revisisons'
import Session from './Session'

export const ROUTE_LIST: Route[] = [
  {
    path: '/Session',
    name: 'SESSION',
    element: <Session />
  },
  {
    path: '/ManualProcedures',
    name: 'SESSION',
    element: <ManualProcedures />
  },
  {
    path: '/Revisisons',
    name: 'REVISISONS',
    element: <Revisions />
  },
  {
    path: '/',
    name: 'REVISISONS',
    element: <Revisions />
  }
]
