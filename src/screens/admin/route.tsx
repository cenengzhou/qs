import { Route } from '../../interfaces/Route'
import Session from './Session'
import Revisions from './Revisisons'

export const ROUTE_LIST: Route[] = [
  {
    path: "/session",
    name: "SESSION",
    element: <Session />,
  },
  {
    path: "/revisisons",
    name: "REVISISONS",
    element: <Revisions />,
  },
  {
    path: "/",
    name: "REVISISONS",
    element: <Revisions />,
  }
];
