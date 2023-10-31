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
import { DialogComponent } from '@syncfusion/ej2-react-popups'
import {
  DropDownButtonComponent,
  ItemModel
} from '@syncfusion/ej2-react-splitbuttons'

import logo from '../../assets/gammon.png'
import user from '../../assets/user.png'
import RightSidebar from '../RightSidebar'
import './style.css'

const Header = () => {
  const navigate = useNavigate()
  const [notify, setNotify] = useState(false)
  const [profile, setProfile] = useState(false)
  const [rightSidebar, SetRightSidebar] = useState(false)
  const [notifyPosition, setNotifyPosition] = useState({ X: 0, Y: 0 })
  const [profilePosition, setProfilePosition] = useState({ X: 0, Y: 0 })
  const notifyBtnRef = useRef(null)
  const profileBtnRef = useRef(null)
  const notifyRef = useRef<DialogComponent | null>()
  const profileRef = useRef<DialogComponent | null>()
  const notifyShow = (e: SyntheticEvent) => {
    setNotify(true)
    setNotifyPosition({ X: (e.target as HTMLElement).offsetLeft - 280, Y: 56 })
  }
  const profileShow = (e: SyntheticEvent) => {
    setProfile(true)
    setProfilePosition({ X: (e.target as HTMLElement).offsetLeft - 200, Y: 56 })
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

  const roleList: ItemModel[] = [
    {
      text: 'JOB_ALL'
    },
    {
      text: 'ROLE_QS_ENQ'
    },
    {
      text: 'ROLE_QS_IMS_ADM'
    },
    {
      text: 'ROLE_QS_IMS_ENQ'
    },
    {
      text: 'ROLE_QS_QS'
    },
    {
      text: 'ROLE_QS_REVIEWER'
    }
  ]

  useEffect(() => {
    document.addEventListener('click', handleDocumentClick)
    return () => {
      document.removeEventListener('click', handleDocumentClick)
    }
  }, [handleDocumentClick])

  return (
    <AppBarComponent colorMode="Dark">
      <div className="logo-container">
        <img src={logo} alt="logo" />
        <span>DEV</span>
      </div>
      <ToolbarComponent overflowMode="Scrollable" id="toolbar_scrollable">
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
          <ItemDirective
            prefixIcon="e-icons e-people"
            text="Admin"
            click={() => navigate('/admin')}
          />
        </ItemsDirective>
      </ToolbarComponent>
      <div className="header-right">
        <span
          className="e-icons e-comments"
          onClick={notifyShow}
          ref={notifyBtnRef}
        ></span>
        <div className="e-avatar e-avatar-circle">
          <img src={user} alt="" />
        </div>
        <div className="username" onClick={profileShow} ref={profileBtnRef}>
          cenengzhou
          <span className="e-icons e-chevron-down-fill e-small"></span>
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
        ref={e => (notifyRef.current = e)}
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
              <span className="e-icons e-bullet-1 e-small"></span>
            </div>
          </div>
          <div className="notifyFooter">Go to Announcement Board</div>
        </div>
      </DialogComponent>
      <DialogComponent
        position={profilePosition}
        width="280px"
        visible={profile}
        cssClass="nopadding"
        ref={e => (profileRef.current = e)}
      >
        <div>
          <div className="profileHeader">
            <div className="e-avatar template-image e-avatar-circle">
              <img src={user} alt="" />
            </div>
            <div className="userName">Ce Neng Zhou - System Developer</div>
            <div className="staff">
              <small>Staff ID: 000001</small>
              <DropDownButtonComponent
                items={roleList}
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
        // eslint-disable-next-line @typescript-eslint/naming-convention
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
