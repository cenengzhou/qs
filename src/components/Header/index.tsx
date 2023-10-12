import { useState, useRef, useEffect } from 'react'
import { AppBarComponent, MenuComponent, MenuItemModel, MenuEventArgs } from '@syncfusion/ej2-react-navigations'
import { DropDownButtonComponent, ItemModel } from '@syncfusion/ej2-react-splitbuttons'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DialogComponent } from '@syncfusion/ej2-react-popups'
import './style.css'
import logo from '../../asset/gammon.png'

const Header = () => {
  const [message, setMessage] = useState(false)
  const [profile, setProfile] = useState(false)
  const messageRef = useRef<DialogComponent | null>()
  const profileRef = useRef<DialogComponent | null>()
  const messageShow = () => {
    setMessage(true);
  }
  const profileShow = () => {
    setProfile(true);
  }
  const messageClose = () => {
    setMessage(false);
  }
  const profileClose = () => {
    setProfile(false);
  }
  const handleDocumentClick = (e: any) => {
    if (messageRef.current && !messageRef.current.element.contains(e.target)) {
      messageClose()
    }
    if (profileRef.current && !profileRef.current.element.contains(e.target)) {
      profileClose()
    }
  }

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
      <ButtonComponent cssClass='e-inherit'>Repackaging</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Main Contract Certificate</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Subcontract</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Internal Valuation</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Enquiry</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Reports</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Transit</ButtonComponent>
      <ButtonComponent cssClass='e-inherit'>Admin</ButtonComponent>
      <div className="header-right">
        <span className="e-icons e-comments e-large" onClick={() => messageShow()}></span>
        <div className="e-avatar">
          <img src={logo} alt="" />
        </div>
        <div className="username" onClick={() => profileShow()}>cenengzhou<span className="e-icons e-chevron-down-fill e-small"></span></div>
        <span className="e-icons e-align-left e-large"></span>
      </div>
      <DialogComponent id="message" width="300px" visible={message} ref={e => messageRef.current = e}><h1>Message</h1></DialogComponent>
      <DialogComponent width="300px" visible={profile} ref={e => profileRef.current = e}>Profile</DialogComponent>
    </AppBarComponent>
  );
}
export default Header;