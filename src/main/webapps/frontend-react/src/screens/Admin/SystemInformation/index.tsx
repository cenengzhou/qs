/* eslint-disable @typescript-eslint/no-explicit-any */
import { ListBoxComponent } from '@syncfusion/ej2-react-dropdowns'

import './style.css'

// const data = {
//   versionDate: '2023-10-06',
//   revision: 'QS2.0',
//   deployEnvironment: 'LOC',
//   jdeUrl: 'http://dvjde/DV910/',
//   apUrl: 'http://gamerp11:7101/spring-ws/',
//   gsfUrl: 'http://gamcnc11:8083/WS/AccessControl.svc/json',
//   wfUrl: 'http://localhost:8207/ws/'
// }

const dataSource: any[] = [
  {
    key: 'versionDate',
    text: 'Deployment Date',
    value: '2023-10-06',
    iconCss: 'e-work-week'
  },
  {
    key: 'revision',
    text: 'Revision',
    value: 'QS2.0',
    iconCss: 'e-conditional-formatting'
  },
  {
    key: 'deployEnvironment',
    text: 'Environment',
    value: 'LOC',
    iconCss: 'e-annotation-edit'
  },
  {
    key: 'jdeUrl',
    text: 'JDE',
    value: 'http://dvjde/DV910/',
    iconCss: 'e-nature'
  },
  {
    key: 'apUrl',
    text: 'AP',
    value: 'http://gamerp11:7101/spring-ws/',
    iconCss: 'e-check'
  },
  {
    key: 'gsfUrl',
    text: 'GSF',
    value: 'http://gamcnc11:8083/WS/AccessControl.svc/json',
    iconCss: 'e-unlock'
  },
  {
    key: 'wfUrl',
    text: 'WF',
    value: 'http://localhost:8207/ws/',
    iconCss: 'e-settings'
  }
]
const SystemInformation = () => {
  return (
    <div className="control-pane">
      <div className="control-section">
        <div className="template-listbox-control">
          <div className="top_title">System Information</div>
          <ListBoxComponent
            dataSource={dataSource}
            itemTemplate='<div class="list-wrapper"><span class="list-wrapper__left"><span class="e-icons ${iconCss}"> </span><span>${text}</span></span><span class="list-wrapper__right">${value}</span></div>'
          />
        </div>
      </div>
    </div>
  )
}

export default SystemInformation
