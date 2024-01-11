/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'

import { RootState } from '../redux/store'

export const useHasRole = () => {
  const [showQSAdmin, setShowQSAdmin] = useState<boolean>(false)
  const [showIMSAdmin, setShowIMSAdmin] = useState<boolean>(false)
  const [showIMSEnquiry, setShowIMSEnquiry] = useState<boolean>(false)
  const [isQsAdmin, setIsQsAdmin] = useState<boolean>(false)
  const getRolesList = useSelector(
    (state: RootState) => state.appConfig.rolesList
  )
  const hasRole = (role: string): boolean => {
    if (getRolesList && getRolesList?.length > 0) {
      return JSON.stringify(getRolesList).indexOf(role) > -1
    }
    return false
  }

  useEffect(() => {
    if (getRolesList && getRolesList?.length > 0) {
      setShowQSAdmin(
        getRolesList.includes('ROLE_QS_QS_ADM') ||
          getRolesList.includes('ROLE_PCMS_QS_ADMIN')
      )
      setShowIMSAdmin(
        getRolesList.includes('ROLE_QS_IMS_ADM') ||
          getRolesList.includes('ROLE_PCMS_IMS_ADMIN')
      )
      setShowIMSEnquiry(
        getRolesList.includes('ROLE_QS_IMS_ENQ') ||
          getRolesList.includes('ROLE_PCMS_IMS_ENQ')
      )
      setIsQsAdmin(getRolesList.includes('ROLE_QS_QS_ADM'))
    }
  }, [getRolesList?.length])

  return { hasRole, showQSAdmin, showIMSAdmin, showIMSEnquiry, isQsAdmin }
}
