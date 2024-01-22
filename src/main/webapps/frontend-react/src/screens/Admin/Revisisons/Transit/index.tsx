import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { InputEventArgs, TextBoxComponent } from '@syncfusion/ej2-react-inputs'

import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import { useUnlockTransitAdminMutation } from '../../../../services'

const Transit = () => {
  const dispatch = useAppDispatch()
  const [jobNumber, setJobNumber] = useState<string>()
  const [unlockTransitAdmin, { isLoading }] = useUnlockTransitAdminMutation()

  const update = () => {
    try {
      if (jobNumber) {
        unlockTransitAdmin({ jobNumber })
          .unwrap()
          .then(() => {
            showTotas('Success', 'Transit updated.')
          })
          .catch(() => {
            showTotas('Fail', 'Failed to fetch')
          })
      }
    } catch (error) {
      showTotas('Fail', 'Fail')
    }
  }
  const showTotas = (mode: 'Fail' | 'Success' | 'Warn', msg?: string) => {
    dispatch(
      setNotificationVisible({
        visible: true,
        mode: mode,
        content: msg
      })
    )
  }

  useEffect(() => {
    if (isLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading])

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
              value={jobNumber}
              input={(value: InputEventArgs) => setJobNumber(value.value)}
            />
          </div>
          <div className="col-lg-6 col-md-6">
            <ButtonComponent
              cssClass="e-info full-btn"
              onClick={update}
              disabled={jobNumber?.length == 5 ? false : true}
            >
              Unlock
            </ButtonComponent>
          </div>
        </div>
        <div className="admin-content"></div>
      </div>
    </>
  )
}

export default Transit
