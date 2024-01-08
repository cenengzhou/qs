import { DialogComponent } from '@syncfusion/ej2-react-popups'

import {
  selectContent,
  selectIsVisible,
  selectMode,
  setNotificationVisible
} from '../../redux/notificationReducer'
import { useAppDispatch, useAppSelector } from '../../redux/store'
import style from './style.module.scss'
import classNames from 'classnames'

const NotificationModal = () => {
  const visible = useAppSelector(selectIsVisible)
  const mode = useAppSelector(selectMode)
  const content = useAppSelector(selectContent)

  const dispatch = useAppDispatch()

  const dialogClose = () => {
    dispatch(setNotificationVisible(false))
  }

  return (
    <div
      className={classNames([
        style.modal,
        { [style['success_modal']]: mode === 'Success' },
        { [style['fail_modal']]: mode === 'Fail' },
        { [style['warn_modal']]: mode === 'Warn' }
      ])}
      id="dialog-target"
    >
      <DialogComponent
        width="600px"
        showCloseIcon={true}
        isModal={true}
        header={() => header(mode)}
        target="#dialog-target"
        visible={visible}
        close={dialogClose}
      >
        <div className={style['modal__content']}>{content}</div>
      </DialogComponent>
    </div>
  )
}

function header(mode?: string) {
  return <div className={style['modal__header']}>{mode}</div>
}

export default NotificationModal
