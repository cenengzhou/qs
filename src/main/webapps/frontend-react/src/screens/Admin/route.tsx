import { Route } from '../../interfaces/Route'
import ManualProcedures from './ManualProcedures'
import Revisions from './Revisisons'
import Session from './Session'
import ResourceCodeMaintenance from './Transit/ResourceCodeMaintenance'
import UOMMaintenance from './Transit/UOMMaintenance'

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
    path: '/TransitUOMMaintenance',
    name: 'UOMMaintenance',
    element: <UOMMaintenance />
  },
  {
    path: '/TransitResourceCodeMaintenance',
    name: 'ResourceCodeMaintenance',
    element: <ResourceCodeMaintenance />
  },
  {
    path: '/',
    name: 'REVISISONS',
    element: <Revisions />
  }
]
