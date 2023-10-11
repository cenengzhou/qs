import { ReactElement } from 'react'

export interface Route {
  path: string
  name: string
  icon: ReactElement
  element: ReactElement
}
