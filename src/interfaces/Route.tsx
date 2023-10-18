import { ReactElement } from 'react'

export interface Route {
  path: string
  name: string
  element: ReactElement
}
