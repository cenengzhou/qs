/* eslint-disable @typescript-eslint/naming-convention */
import { useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import { ToastComponent } from '@syncfusion/ej2-react-notifications'

const Transit = () => {
  const toast = useRef<ToastComponent>(null)
  const show = () => {
    toast.current?.show({ title: 'Success', cssClass: 'full' })
  }

  return (
    <>
      <div className="admin-container">
        {/* input */}
        <div className="admin-header row">
          <div className="col-lg-6 col-md-6">
            <TextBoxComponent
              placeholder="Job Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value={'19015'}
            />
          </div>
          <div className="col-lg-6 col-md-6">
            <ButtonComponent cssClass="e-info full-btn" onClick={show}>
              Unlock
            </ButtonComponent>
          </div>
        </div>
        <div className="admin-content"></div>
      </div>
      <ToastComponent ref={toast} />
    </>
  )
}

export default Transit
