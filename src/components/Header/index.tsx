import { useState, useRef, useEffect } from 'react'
import { AppBarComponent, ToolbarComponent, ItemsDirective, ItemDirective, TabComponent, TabItemsDirective, TabItemDirective } from '@syncfusion/ej2-react-navigations'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DialogComponent } from '@syncfusion/ej2-react-popups'
import {
  DropDownButtonComponent,
  ItemModel,
} from "@syncfusion/ej2-react-splitbuttons"
import RightSidebar from '../RightSidebar'
import './style.css'
import logo from '../../asset/gammon.png'
import user from '../../asset/user.png'

const Header = () => {
  const [notify, setNotify] = useState(false)
  const [profile, setProfile] = useState(false)
  const [rightSidebar, SetRightSidebar] = useState(false);
  const [notifyPosition, setNotifyPosition] = useState({ X: 0, Y: 0 })
  const [profilePosition, setProfilePosition] = useState({X: 0, Y: 0})
  const notifyBtnRef = useRef(null)
  const profileBtnRef = useRef(null)
  const notifyRef = useRef<DialogComponent | null>()
  const profileRef = useRef<DialogComponent | null>()
  const notifyShow = (e: any) => {
    setNotify(true);
    setNotifyPosition({ X: e.target.offsetLeft - 280, Y: 56 })
  }
  const profileShow = (e: any) => {
    setProfile(true)
    setProfilePosition({ X: e.target.offsetLeft - 200, Y: 56 })
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

  const handleDocumentClick = (e: any) => {
    if (
      e.target !== notifyBtnRef.current &&
      notifyRef.current &&
      !notifyRef.current.element.contains(e.target)
    ) {
      notifyClose()
    }
    if (e.target !== profileBtnRef.current && profileRef.current && !profileRef.current.element.contains(e.target)) {
      profileClose()
    }
  }

  const roleList: ItemModel[] = [
    {
      text: "JOB_ALL",
    },
    {
      text: "ROLE_QS_ENQ",
    },
    {
      text: "ROLE_QS_IMS_ADM",
    },
    {
      text: "ROLE_QS_IMS_ENQ",
    },
    {
      text: "ROLE_QS_QS",
    },
    {
      text: "ROLE_QS_REVIEWER",
    },
  ]

  useEffect(() => {
    document.addEventListener('click', handleDocumentClick)

    return () => {
      document.removeEventListener('click', handleDocumentClick)
    }
  }, [])

  return (
    <AppBarComponent colorMode="Dark">
      <div className="logo-container">
        <img src={logo} alt="logo" />
        <span>DEV</span>
      </div>
      <ToolbarComponent overflowMode="Scrollable" id="toolbar_scrollable">
        <ItemsDirective>
          <ItemDirective
            prefixIcon="e-icons e-cut"
            text="Repackaging"
            click={(e) => console.log(e)}
          />
          <ItemDirective
            prefixIcon="e-icons e-copy"
            text="Main Contract Certificate"
          />
          <ItemDirective prefixIcon="e-icons e-paste" text="Subcontract" />
          <ItemDirective
            prefixIcon="e-icons e-bold"
            text="Internal Valuation"
          />
          <ItemDirective prefixIcon="e-icons e-underline" text="Enquiry" />
          <ItemDirective prefixIcon="e-icons e-italic" text="Reports" />
          <ItemDirective prefixIcon="e-icons e-align-left" text="Transit" />
          <ItemDirective prefixIcon="e-icons e-align-right" text="Admin" />
        </ItemsDirective>
      </ToolbarComponent>
      <div className="header-right">
        <span
          className="e-icons e-comments e-large"
          onClick={(e) => notifyShow(e)}
          ref={notifyBtnRef}
        ></span>
        <div className="e-avatar e-avatar-circle">
          <img src={user} alt="" />
        </div>
        <div
          className="username"
          onClick={(e) => profileShow(e)}
          ref={profileBtnRef}
        >
          cenengzhou
          <span className="e-icons e-chevron-down-fill e-small"></span>
        </div>
        <span
          className="e-icons e-align-left e-large"
          onClick={() => rightSidebarToggle()}
        ></span>
      </div>
      <DialogComponent
        position={notifyPosition}
        id="notify"
        width="315px"
        visible={notify}
        ref={(e) => (notifyRef.current = e)}
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
        ref={(e) => (profileRef.current = e)}
      >
        <div>
          <div className="profileHeader">
            <span className="e-avatar template-image e-avatar-circle"></span>
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
        position={{ X: "right", Y: 56 }}
        width="250px"
        height={window.innerHeight - 56}
        visible={rightSidebar}
      >
        <RightSidebar></RightSidebar>
      </DialogComponent>
    </AppBarComponent>
  );
}
export default Header;