import { DialogComponent } from '@syncfusion/ej2-react-popups'

import style from './style.module.scss'

interface NotificationModalProps {
  visible: boolean
  mode?: 'Fail' | 'Success' | 'Warn'
  content: string
  dialogClose: () => void
}

const NotificationModal = ({
  visible,
  mode = 'Success',
  content,
  dialogClose
}: NotificationModalProps) => {
  return (
    <div className={style.modal} id="dialog-target">
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

function header(mode?: 'Fail' | 'Success' | 'Warn') {
  return <div className={style['modal__header']}>{mode}</div>
}

export default NotificationModal
