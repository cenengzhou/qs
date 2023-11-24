import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'

import { RootState } from '../redux/store'

export const useHasRole = (role: string) => {
  const [isRole, setIsRole] = useState<boolean>(false)
  const getRolesList = useSelector(
    (state: RootState) => state.appConfig.rolesList
  )
  useEffect(() => {
    if (getRolesList && getRolesList?.length > 0) {
      setIsRole(JSON.stringify(getRolesList).indexOf(role) > -1)
    }
  }, [getRolesList?.length])

  return isRole
}
