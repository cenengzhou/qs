/* eslint-disable @typescript-eslint/naming-convention */
import { SyntheticEvent, useCallback, useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  AppBarComponent,
  ItemDirective,
  ItemsDirective,
  ToolbarComponent
} from '@syncfusion/ej2-react-navigations'
import {
  DialogComponent,
  hideSpinner,
  showSpinner
} from '@syncfusion/ej2-react-popups'
import {
  DropDownButtonComponent,
  ItemModel
} from '@syncfusion/ej2-react-splitbuttons'

import logo from '../../assets/gammon.png'
import userImg from '../../assets/profile.png'
import { setEnv } from '../../config/appConfig'
import { GLOBALPARAMETER } from '../../constants/global'
import { useHasRole } from '../../hooks/useHasRole'
import {
  changeEnv,
  currentUser,
  hasRolesList
} from '../../redux/appConfigReducer'
import { RootState, useAppDispatch, useAppSelector } from '../../redux/store'
import {
  Role,
  useGetCurrentUserQuery,
  useGetNotificationReadStatusByCurrentUserQuery,
  useUpdateNotificationReadStatusByCurrentUserMutation
} from '../../services'
import RightSidebar from '../RightSidebar'
import './style.css'

const Header = () => {
  const dispatch = useAppDispatch()
  const env = useAppSelector((state: RootState) => state.appConfig.env)
  const getCurrentUser = useAppSelector(
    (state: RootState) => state.appConfig.currentUser
  )
  const getRolesList = useAppSelector(
    (state: RootState) => state.appConfig.rolesList
  )
  const navigate = useNavigate()
  const { isSuccess, data } = useGetCurrentUserQuery()
  const { data: readStatus } = useGetNotificationReadStatusByCurrentUserQuery()
  const [updateNotificationReadStatus, { isLoading: updateLoading }] =
    useUpdateNotificationReadStatusByCurrentUserMutation()

  const [notify, setNotify] = useState(false)
  const [profile, setProfile] = useState(false)
  const [rightSidebar, SetRightSidebar] = useState(false)
  const [notifyPosition, setNotifyPosition] = useState({ X: 0, Y: 0 })
  const [profilePosition, setProfilePosition] = useState({ X: 0, Y: 0 })
  const notifyBtnRef = useRef(null)
  const profileBtnRef = useRef(null)
  const notifyRef = useRef<DialogComponent>(null)
  const profileRef = useRef<DialogComponent>(null)

  const [imageUrl, setImageUrl] = useState(
    GLOBALPARAMETER.imageServerAddress + getCurrentUser?.StaffID + '.jpg'
  )

  const [rolesList, setRolesList] = useState<ItemModel[]>([])

  const notifyShow = (e: SyntheticEvent) => {
    setNotify(true)
    setNotifyPosition({ X: (e.target as HTMLElement).offsetLeft - 280, Y: 56 })
  }
  const profileShow = (e: SyntheticEvent) => {
    e.stopPropagation()
    setProfile(true)
    setProfilePosition({
      X: (profileBtnRef.current! as HTMLElement).offsetLeft! - 120,
      Y: 56
    })
  }
  const notifyClose = () => {
    setNotify(false)
  }
  const profileClose = () => {
    setProfile(false)
  }
  const rightSidebarToggle = () => {
    SetRightSidebar(!rightSidebar)
  }

  const handleDocumentClick = useCallback((e: MouseEvent) => {
    if (
      e.target !== notifyBtnRef.current &&
      notifyRef.current &&
      !notifyRef.current.element.contains(e.target as HTMLElement)
    ) {
      notifyClose()
    }
    if (
      e.target !== profileBtnRef.current &&
      profileRef.current &&
      !profileRef.current.element.contains(e.target as HTMLElement)
    ) {
      profileClose()
    }
  }, [])

  useEffect(() => {
    if (isSuccess && data) {
      dispatch(currentUser(data))
      dispatch(hasRolesList(data.UserRoles?.map((e: Role) => e.authority)))
    }
    const root = document.getElementById('root')!
    if (updateLoading) {
      showSpinner(root)
    } else {
      hideSpinner(root)
    }
  }, [isSuccess, data, updateLoading])

  useEffect(() => {
    dispatch(changeEnv(setEnv()))
    if (getRolesList && getRolesList.length > 0) {
      setRolesList(getRolesList.map((e: string) => ({ text: e })))
    }
    document.onclick = async (e: MouseEvent) => {
      const target = e.target as HTMLElement
      if (target.id === 'read') {
        try {
          await updateNotificationReadStatus('Y').unwrap()
        } catch (error) {
          console.log(error)
        }
      }
    }
  }, [])

  useEffect(() => {
    document.addEventListener('click', handleDocumentClick)
    return () => {
      document.removeEventListener('click', handleDocumentClick)
    }
  }, [handleDocumentClick])

  return (
    <AppBarComponent
      colorMode={env !== 'DEV' && env !== 'UAT' ? 'Primary' : 'Dark'}
    >
      <div className="logo-container">
        <img src={logo} alt="logo" />
        {env !== 'PRO' && <span>{env}</span>}
      </div>
      <div className="toolbar_scrollable">
        <ToolbarComponent overflowMode="Scrollable">
          <ItemsDirective>
            <ItemDirective
              prefixIcon="e-icons e-volume"
              text="Repackaging"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-bookmark-fill"
              text="Main Contract Certificate"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-activities"
              text="Subcontract"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-chart-2d-stacked-line"
              text="Internal Valuation"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-circle-info"
              text="Enquiry"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-changes-track"
              text="Reports"
              click={() => navigate('/')}
            />
            <ItemDirective
              prefixIcon="e-icons e-page-columns"
              text="Transit"
              click={() => navigate('/')}
            />
            {useHasRole('QS_ENQ') && (
              <ItemDirective
                prefixIcon="e-icons e-people"
                text="Admin"
                click={() => navigate('/admin/Revisions')}
              />
            )}
          </ItemsDirective>
        </ToolbarComponent>
      </div>
      <div className="header-right">
        <div
          className="e-icons e-comments e-large"
          onClick={notifyShow}
          ref={notifyBtnRef}
        >
          {readStatus === 'Y' && <span className="read"></span>}
        </div>
        <div
          className="user-Container"
          onClick={profileShow}
          ref={profileBtnRef}
        >
          <div className="e-avatar e-avatar-circle">
            <img src={imageUrl} onError={() => setImageUrl(userImg)} alt="" />
          </div>
          <div className="username">
            {getCurrentUser?.username}
            <span className="e-icons e-chevron-down-fill e-small"></span>
          </div>
        </div>
        <span
          className="e-icons e-align-left"
          onClick={() => rightSidebarToggle()}
        ></span>
      </div>
      <DialogComponent
        position={notifyPosition}
        cssClass="nopadding"
        width="315px"
        visible={notify}
        ref={notifyRef}
      >
        <div>
          <div className="notifyHeader">Notifications</div>
          <div className="notifyBody">
            <span className="e-avatar template-image e-avatar-large e-avatar-circle"></span>
            <div className="mContent">
              <div className="mTitle">Year End Cutoff 2022</div>
              <div className="mTime">
                Special attention for Singapore users!
              </div>
            </div>
            <span className="e-icons e-eye" id="read"></span>
          </div>
          <div className="notifyFooter">Go to Announcement Board</div>
        </div>
      </DialogComponent>
      <DialogComponent
        position={profilePosition}
        width="280px"
        visible={profile}
        cssClass="nopadding"
        ref={profileRef}
      >
        <div>
          <div className="profileHeader">
            <div className="e-avatar template-image e-avatar-circle">
              <img src={imageUrl} onError={() => setImageUrl(userImg)} alt="" />
            </div>
            <div className="userName">
              {getCurrentUser?.fullname + ' ' + getCurrentUser?.title}
            </div>
            <div className="staff">
              <small>Staff ID: {getCurrentUser?.StaffID}</small>
              <DropDownButtonComponent
                items={rolesList}
                cssClass="e-caret-hide"
                className="e-success e-small"
              >
                Role
              </DropDownButtonComponent>
            </div>
          </div>
          <div className="profileBody"></div>
          <div className="profileFooter">
            <ButtonComponent>Profile</ButtonComponent>
            <ButtonComponent>Sign out</ButtonComponent>
          </div>
        </div>
      </DialogComponent>
      <DialogComponent
        position={{ X: 'right', Y: 56 }}
        width="250px"
        height={window.innerHeight - 56}
        visible={rightSidebar}
        cssClass="nopadding"
      >
        <RightSidebar></RightSidebar>
      </DialogComponent>
    </AppBarComponent>
  )
}
export default Header
