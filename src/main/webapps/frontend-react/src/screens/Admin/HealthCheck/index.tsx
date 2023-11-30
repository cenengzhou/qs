/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from 'react'

import {
  ButtonComponent,
  CheckBoxComponent
} from '@syncfusion/ej2-react-buttons'
import {
  AccumulationChartComponent,
  AccumulationSeriesCollectionDirective,
  AccumulationSeriesDirective,
  PieSeries
} from '@syncfusion/ej2-react-charts'
import {
  ColumnDirective,
  ColumnsDirective,
  GridComponent,
  Page,
  Sort
} from '@syncfusion/ej2-react-grids'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import {
  AccordionComponent,
  AccordionItemDirective,
  AccordionItemsDirective,
  Inject,
  TabComponent,
  TabItemDirective,
  TabItemsDirective
} from '@syncfusion/ej2-react-navigations'

import data from './data'
import './style.css'
import styles from './style.module.scss'
import classNames from 'classnames'

const HealthCheck = () => {
  const headerText = [
    { text: 'Full Check', iconCss: 'e-icons e-table-of-content' },
    { text: 'System', iconCss: 'e-icons e-list-box' },
    { text: 'Database', iconCss: 'e-icons e-list-unordered-3' }
  ]
  return (
    <div className={styles.control_pane}>
      <div
        className={classNames(
          ['col-xs-12 col-sn-12 col-lg-6 col-md-6'],
          styles.control_pane_button
        )}
      >
        <ButtonComponent cssClass="e-info">Open in New Window</ButtonComponent>
      </div>
      <div className={styles.tab_control_section}>
        <TabComponent>
          <TabItemsDirective>
            <TabItemDirective
              header={headerText[0]}
              content={TabFull}
            ></TabItemDirective>
            <TabItemDirective
              header={headerText[1]}
              content={TabSystem}
            ></TabItemDirective>
            <TabItemDirective
              header={headerText[2]}
              content={TabDatabase}
            ></TabItemDirective>
          </TabItemsDirective>
        </TabComponent>
      </div>
    </div>
  )
}

// ------------------------------- THE TAB --------------------------------------
const TabFull = () => {
  return (
    <>
      <div className={styles['e-tab-content']}>
        <div className="col-md-8">
          <Acrdn title="Full Check" content={AcrdnContentFullCheck} />
        </div>
        <div className="col-md-4">
          <div className={styles['acrdn']}>
            <Acrdn title="REST" content={AcrdnContentRest} />
            <div className={styles['acrdn-widget']}>
              <div>
                <span className={styles['acrdn-widget-time']}>0:00:00</span>
                <span
                  className={classNames(
                    ['e-icons e-chevron-up-fill-3'],
                    styles['acrdn-widget-icon']
                  )}
                ></span>
              </div>
              <div className={styles['acrdn-widget-text']}>Full Check Time</div>
            </div>
          </div>
        </div>
      </div>
      <div className={styles['e-tab-content__button']}>
        <ButtonComponent cssClass="e-info">Full Check</ButtonComponent>
      </div>
    </>
  )
}

const TabSystem = () => {
  const [isTable, setIsTable] = useState(false)
  const [tableName, setTableName] = useState('')
  return (
    <div>
      {!isTable && (
        <div
          className={classNames([styles['grid-tab-content'], styles['green']])}
        >
          <SystemItem
            color="green"
            title="GFS"
            icon="unlock"
            subtitle="Gammon Security Framework"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
          <SystemItem
            color="green"
            title="JDE"
            icon="nature"
            subtitle="JD Edwards"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
          <SystemItem
            color="green"
            title="AP"
            icon="check"
            subtitle="Approval"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
          <SystemItem
            color="green"
            title="QS"
            icon="settings"
            subtitle="QS"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
        </div>
      )}
      {isTable && (
        <Table title={tableName} isTable={isTable} setIsTable={setIsTable} />
      )}
    </div>
  )
}

const TabDatabase = () => {
  const [isTable, setIsTable] = useState(false)
  const [tableName, setTableName] = useState('')
  return (
    <div>
      {!isTable && (
        <div
          className={classNames([styles['grid-tab-content'], styles['yellow']])}
        >
          <SystemItem
            color="yellow"
            title="PCMSDATA"
            icon="field-settings"
            subtitle="PCMSDATA"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
          <SystemItem
            color="yellow"
            title="ADL"
            icon="list-unordered-2"
            subtitle="ADL"
            setTableName={setTableName}
            setIsTable={setIsTable}
          />
        </div>
      )}
      {isTable && (
        <Table title={tableName} isTable={isTable} setIsTable={setIsTable} />
      )}
    </div>
  )
}

// ------------------------ Tab 《Full Check》 content --------------------------------
const AcrdnContentFullCheck = () => {
  return (
    <div className={styles['full-check-acrdn']}>
      <div className="col-md-6">
        <div className={styles['full-check-acrdn-content']}>
          <div className={styles['full-check-acrdn-content-title']}>System</div>
          <div className={styles['full-check-acrdn-content-chart']}>
            <div style={{ width: '100px', height: '100px' }}>
              <SystemPieChart />
            </div>
            <AcrdnSummary successNumber={0} successPerc={0} failNumber={0} />
          </div>
        </div>
      </div>
      <div className="col-md-6">
        <div className={styles['full-check-acrdn-content']}>
          <div className={styles['full-check-acrdn-content-title']}>
            Database
          </div>
          <div className={styles['full-check-acrdn-content-chart']}>
            <div style={{ width: '100px', height: '100px' }}>
              <DataBasePieChart />
            </div>
            <AcrdnSummary successNumber={0} successPerc={0} failNumber={0} />
          </div>
        </div>
      </div>
    </div>
  )
}
const AcrdnContentRest = () => {
  const [checkBoxAll, setcheckBoxAll] = useState(false)
  const checkBoxChange = () => {
    setcheckBoxAll((p: boolean) => {
      return !p
    })
    if (checkBoxAll === true) {
      console.log(true)
    }
  }
  return (
    <div className={styles['acrd-rest-content']}>
      {!checkBoxAll && (
        <div className={styles['acrd-rest-content__item']}>
          <TextBoxComponent
            placeholder="Limit subtask"
            floatLabelType="Auto"
            cssClass="e-outline"
            value=""
          />
        </div>
      )}
      <div className={styles['acrd-rest-content__item']}>
        <CheckBoxComponent
          checked={checkBoxAll}
          change={checkBoxChange}
          label="Run all subtask"
        />
      </div>
      <div className={styles['acrd-rest-content__item']}>
        <ButtonComponent cssClass="e-info">REST Check</ButtonComponent>
      </div>
    </div>
  )
}

const SystemPieChart = () => {
  return (
    <div className={styles.template}>
      <AccumulationChartComponent
        width="100px"
        height="100px"
        id="system-pie-chart"
        legendSettings={{ visible: false }}
        tooltip={{ enable: true, format: '${point.tooltip}' }}
        enableSmartLabels={true}
        enableBorderOnMouseMove={false}
      >
        <Inject services={[PieSeries]}></Inject>
        <AccumulationSeriesCollectionDirective>
          <AccumulationSeriesDirective
            tooltipMappingName="product"
            dataLabel={{
              visible: true,
              position: 'Outside',
              name: 'r',
              connectorStyle: { length: '10px', type: 'Curve' }
            }}
            type="Pie"
            dataSource={[
              {
                product: 'TV : 30 (12%)',
                percentage: 12,
                r: 'TV, 30 <br>12%'
              },
              {
                product: 'PC : 20 (8%)',
                percentage: 8,
                r: 'PC, 20 <br>8%'
              }
            ]}
            xName="product"
            yName="percentage"
            innerRadius="50%"
          ></AccumulationSeriesDirective>
        </AccumulationSeriesCollectionDirective>
      </AccumulationChartComponent>
    </div>
  )
}
const DataBasePieChart = () => {
  return (
    <div className={styles.template}>
      <AccumulationChartComponent
        width="100px"
        height="100px"
        id="database-pie-chart"
        legendSettings={{ visible: false }}
        tooltip={{ enable: true, format: '${point.tooltip}' }}
        enableSmartLabels={true}
        enableBorderOnMouseMove={false}
      >
        <Inject services={[PieSeries]}></Inject>
        <AccumulationSeriesCollectionDirective>
          <AccumulationSeriesDirective
            tooltipMappingName="product"
            dataLabel={{
              visible: true,
              position: 'Outside',
              name: 'r',
              connectorStyle: { length: '10px', type: 'Curve' }
            }}
            type="Pie"
            dataSource={[
              {
                product: 'TV : 30 (12%)',
                percentage: 12,
                r: 'TV, 30 <br>12%'
              },
              {
                product: 'PC : 20 (8%)',
                percentage: 8,
                r: 'PC, 20 <br>8%'
              }
            ]}
            xName="product"
            yName="percentage"
            innerRadius="50%"
          ></AccumulationSeriesDirective>
        </AccumulationSeriesCollectionDirective>
      </AccumulationChartComponent>
    </div>
  )
}

const Acrdn = ({
  title,
  content
}: {
  title: string
  content: () => JSX.Element
}) => {
  return (
    <div className={styles['acrdn']}>
      <AccordionComponent>
        <AccordionItemsDirective>
          <AccordionItemDirective
            header={`<div class="acrdn-header">${title}</div>`}
            expanded={true}
            content={content}
          />
        </AccordionItemsDirective>
      </AccordionComponent>
    </div>
  )
}

const AcrdnSummary = ({
  successNumber,
  successPerc,
  failNumber
}: {
  successNumber: number
  successPerc: number
  failNumber: number
}) => {
  return (
    <div className={styles['summary']}>
      <div>
        <div className={styles['summary-text']}>Success</div>
        <div>
          <span className={styles['summary-number']}>{successNumber}</span>
          <small className={styles['summary-number-perc']}>
            ({successPerc}%)
          </small>
        </div>
      </div>
      <div>
        <div className={styles['summary-text']}>Fail</div>
        <div>
          <small className={styles['summary-number']}>{failNumber}</small>
        </div>
      </div>
    </div>
  )
}

// -------------------------------- Tab 《System》/《Database》 content ------------------------------------
export interface TableProps {
  orderId: number
  name: string
  result: string
  status: string
  elapseTime: number
}
const Table = ({
  title,
  isTable,
  setIsTable: setIsTable
}: {
  title: string
  isTable: boolean
  setIsTable: (p: boolean) => void
}) => {
  return (
    <div className={styles['form']}>
      <div className={styles['form__title']}>
        <span>Test Case - {title}</span>
        <div
          className={styles['back-icon']}
          onClick={() => setIsTable(!isTable)}
        >
          <span
            className={classNames(
              ['e-icons e-arrow-left'],
              styles['form__title__icon']
            )}
          />
        </div>
      </div>
      <div className={styles['form__content']}>
        <GridComponent
          dataSource={data}
          allowSorting={true}
          allowPaging={true}
          pageSettings={{ pageCount: 5, pageSize: 2, pageSizes: true }}
        >
          <ColumnsDirective>
            <ColumnDirective
              field="orderId"
              headerText="Order ID"
              width="100"
            />
            <ColumnDirective field="name" headerText="Name" width="100" />
            <ColumnDirective field="result" headerText="Result" width="100" />
            <ColumnDirective field="status" headerText="Status" width="100" />
            <ColumnDirective
              field="elapseTime"
              headerText="Elapse Time"
              width="100"
            />
            <ColumnDirective
              field=""
              headerText="Run"
              width="100"
              template={(e: TableProps) => (
                <div>
                  <ButtonComponent
                    cssClass="e-info"
                    onClick={() => console.log(e.elapseTime)}
                  >
                    Run
                  </ButtonComponent>
                </div>
              )}
            />
          </ColumnsDirective>
          <Inject services={[Sort, Page]} />
        </GridComponent>
      </div>
    </div>
  )
}

const SystemItem = ({
  title,
  subtitle,
  icon,
  color,
  setIsTable,
  setTableName
}: {
  title: string
  subtitle: string
  icon: string
  color: string
  setIsTable: (p: boolean) => void
  setTableName: (p: string) => void
}) => {
  return (
    <div className={styles['fill']}>
      <div
        className={classNames([
          styles['grid-tab-content__item'],
          styles[`${color}__item`]
        ])}
        onClick={() => {
          setIsTable(true)
          setTableName(title)
        }}
      >
        <span
          className={classNames(
            `e-icons e-${icon}`,
            styles['grid-tab-content__item__icon']
          )}
        ></span>
        <span className={styles['grid-tab-content__item__text']}>{title}</span>
      </div>
      <span className={styles['fill__text']}>{subtitle}</span>
    </div>
  )
}
export default HealthCheck
