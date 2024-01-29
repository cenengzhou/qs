import { Route } from '../../interfaces/Route'
import Job from './Job'
import JobSelect from './JobSelect'

export const ROUTE_LIST: Route[] = [
  {
    path: '/job/*',
    name: 'job',
    element: <Job />
  },
  {
    path: '/job-select',
    name: 'job-select',
    element: <JobSelect />
  },
  {
    path: '/',
    name: 'job-select',
    element: <JobSelect />
  }
]
