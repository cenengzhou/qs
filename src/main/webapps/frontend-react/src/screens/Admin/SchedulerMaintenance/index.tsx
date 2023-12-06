import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'

import DatePicker from '../../../components/DatePicker'
import style from './style.module.scss'
import classNames from 'classnames'

const SchedulerMaintenance = () => {
  return (
    <div className={style['container']}>
      <div className="row">
        <Box
          title="cronTriggerMainC"
          description="Cleanup audit table at 0:00 every day"
          jobName="jobDetailAuditHousekeep"
          startTime="2023-11-30 18:17:06 +0800"
          previousFireTime="2023-12-01 00:00:00 +0800"
          nextFireTime="2023-12-02"
          cronExpression="job"
          triggerState={3}
        />
      </div>
      <div className={classNames(['row', style['sticky']])}>
        <ButtonComponent
          cssClass="e-info full-btn e-card-btn-txt"
          iconCss="e-icons e-save"
        >
          Update
        </ButtonComponent>
      </div>
    </div>
  )
}

const Box = ({
  title,
  jobName,
  description,
  startTime,
  previousFireTime,
  nextFireTime,
  cronExpression,
  triggerState
}: {
  title: string
  jobName: string
  description: string
  startTime: string
  previousFireTime: string
  nextFireTime: string
  cronExpression: string
  triggerState: number | string
}) => {
  return (
    <div className="col-sm-12 col-md-4">
      <div className={style['box']}>
        <div className={style['box__header']}>
          <span
            className={classNames([
              'e-icons e-bullet-4',
              style['box__header__icon']
            ])}
          />
          <div className={style['box__header__text']}>{title}</div>
        </div>
        <div className={style['box__body']}>
          <table>
            <tr>
              <th>Job Name:</th>
              <th>{jobName}</th>
            </tr>
            <tr>
              <td>Description:</td>
              <td>
                <div className={style['box__body__description']}>
                  <div>{description}</div>
                  <div>
                    <TooltipComponent
                      opensOn="Click"
                      content={ToolTipContent}
                      position="BottomLeft"
                    >
                      <ButtonComponent cssClass="e-info">
                        View details
                      </ButtonComponent>
                    </TooltipComponent>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td>Start Time:</td>
              <td>{startTime}</td>
            </tr>
            <tr>
              <td>Previous Fire Time:</td>
              <td>{previousFireTime}</td>
            </tr>
            <tr>
              <td>Next Fire Time:</td>
              <td>
                <DatePicker value={nextFireTime} />
              </td>
            </tr>
            <tr>
              <td>Cron Expression:</td>
              <td>
                <TextBoxComponent
                  floatLabelType="Auto"
                  value={cronExpression}
                />
              </td>
            </tr>
            <tr>
              <td>Trigger State:</td>
              <td>
                <DropDownListComponent
                  dataSource={[
                    { text: 'ACQUIRED', value: 1 },
                    { text: 'PAUSED', value: 2 },
                    { text: 'WAITING', value: 3 }
                  ]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  value={triggerState}
                />
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  )
}

const ToolTipContent = () => {
  return (
    <div className={style['toolTip']}>
      <h3>Schedule details</h3>
      <div>content</div>
    </div>
  )
}
export default SchedulerMaintenance
