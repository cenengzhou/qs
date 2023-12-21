import { Route } from '../../interfaces/Route'
import AnnouncementSetting from './AnnouncementSetting'
import HealthCheck from './HealthCheck'
import ManualProcedures from './ManualProcedures'
import Revisions from './Revisisons'
import SchedulerMaintenance from './SchedulerMaintenance'
import Session from './Session'
import StandardTermsMaintenance from './Subcontract/StandardTermsMaintenance'
import SystemInformation from './SystemInformation'
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
    path: '/Revisions',
    name: 'REVISIONS',
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
    path: '/SubcontractStandardTermsMaintenance',
    name: 'StandardTermsMaintenance',
    element: <StandardTermsMaintenance />
  },
  {
    path: '/AnnouncementSetting',
    name: 'AnnouncementSetting',
    element: <AnnouncementSetting />
  },
  {
    path: '/SystemInformation',
    name: 'SystemInformation',
    element: <SystemInformation />
  },
  {
    path: '/HealthCheck',
    name: 'HealthCheck',
    element: <HealthCheck />
  },
  {
    path: '/SchedulerMaintenance',
    name: 'SchedulerMaintenance',
    element: <SchedulerMaintenance />
  },
  {
    path: '/',
    name: 'REVISIONS',
    element: <Revisions />
  }
]
