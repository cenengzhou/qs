import * as React from 'react';
import { AppBarComponent, MenuComponent, MenuItemModel, MenuEventArgs } from '@syncfusion/ej2-react-navigations';
import { DropDownButtonComponent, ItemModel } from '@syncfusion/ej2-react-splitbuttons';
import { ButtonComponent } from '@syncfusion/ej2-react-buttons';
import './style.css';
import logo from '../../asset/gammon.png'

const Header = () => {
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
        <span className="e-icons e-comments e-large"></span>
        <div className="e-avatar">
          <img src={logo} alt="" />
        </div>
        <span className="username">cenengzhou</span>
        <span className="e-icons e-align-left e-large"></span>
      </div>
    </AppBarComponent>
  );
}
export default Header;