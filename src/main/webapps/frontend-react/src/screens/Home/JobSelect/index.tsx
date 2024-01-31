import { useEffect, useRef, useState } from 'react'

import {
  faCity,
  faDigging,
  faDollarSign,
  faGasPump,
  faRoadBarrier,
  faTableCells
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { InputEventArgs, TextBoxComponent } from '@syncfusion/ej2-react-inputs'

import JobItem from '../../../components/JobItem'
import { setJobDescription, setJobNo } from '../../../redux/appConfigReducer'
import { closeLoading, openLoading } from '../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../redux/notificationReducer'
import { useAppDispatch } from '../../../redux/store'
import {
  Job,
  JobListResponse,
  useGetJobListMutation,
  useObtainCacheKeyMutation
} from '../../../services'
import './style.scss'
import CryptoJS from 'crypto-js'

const JobSelect = () => {
  const dispatch = useAppDispatch()
  const [newList, setNewList] = useState<JobListResponse>([])
  const [jobList, setJobList] = useState<JobListResponse>([])
  const [getCacheKey, { isLoading: cacheKeyLoading }] =
    useObtainCacheKeyMutation()
  const [getJobList, { isLoading: getLoading }] = useGetJobListMutation()
  const [page, setPage] = useState(0)
  const [currentBtn, setCurrentBtn] = useState<string>('ALL')
  const btnList = [
    { name: 'BDG', style: 'bdg', icon: faCity },
    { name: 'CVL', style: 'cvl', icon: faRoadBarrier },
    { name: 'E&M', style: 'em', icon: faGasPump },
    { name: 'FDN', style: 'fdn', icon: faDigging },
    { name: 'SGP', style: 'sgp', icon: faDollarSign },
    { name: 'ALL', style: 'all', icon: faTableCells }
  ]
  const isStart = useRef(false)

  const getLocalList = async (type: boolean, cacheKey: string) => {
    const item = type == true ? 'COMPLETED_JOB_LIST' : 'ONGOING_JOB_LIST'
    const localJobList = localStorage.getItem(item)
    let list: Job[] = []
    try {
      list = JSON.parse(
        CryptoJS.AES.decrypt(localJobList || '', cacheKey).toString(
          CryptoJS.enc.Utf8
        )
      )
    } catch (error) {
      localStorage.removeItem(item)
      list = await getJobList(type).unwrap()
      localStorage.setItem(
        item,
        CryptoJS.AES.encrypt(JSON.stringify(list), cacheKey).toString()
      )
    }
    return list
  }

  const getList = async () => {
    try {
      const completedCacheKey = await getCacheKey('COMPLETED_JOB_LIST').unwrap()
      const onGingCacheKey = await getCacheKey('ONGOING_JOB_LIST').unwrap()
      const completedJobList = await getLocalList(true, completedCacheKey)
      const ongoingJobList = await getLocalList(false, onGingCacheKey)
      const list = [...completedJobList, ...ongoingJobList].sort((a, b) =>
        b.jobNo!.localeCompare(a.jobNo!)
      )
      setNewList(list)
    } catch (error) {
      dispatch(
        setNotificationVisible({
          visible: true,
          mode: 'Fail',
          content: 'Failed to fetch'
        })
      )
    }
  }

  const handleSearch = (event: InputEventArgs) => {
    console.log('event====', event)
    const searchText = event.value
    if (searchText && searchText?.length > 0) {
      setJobList([...newList.filter(item => item.jobNo!.includes(searchText))])
    }
  }

  useEffect(() => {
    const num = 200
    if (page > 0 && (page - 1) * num < newList.length) {
      setJobList(pre => [
        ...pre,
        ...newList.slice((page - 1) * num, page * num)
      ])
      setTimeout(() => {
        setPage(page + 1)
      }, 500)
    }
  }, [page])

  useEffect(() => {
    if (newList.length > 0 && page == 0) {
      setPage(1)
    }
  }, [newList])

  useEffect(() => {
    if (!isStart.current) {
      isStart.current = true
      dispatch(setJobNo(''))
      dispatch(setJobDescription(''))
      getList()
    }
  }, [])

  useEffect(() => {
    if (cacheKeyLoading || getLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading, cacheKeyLoading])

  return (
    <div className="selete-container">
      <div className="selete-header row">
        <div className="row col-xl-4 col-md-6 col-sm-12">
          <div className="e-btn-group">
            {btnList.map(item => (
              <ButtonComponent
                cssClass={currentBtn == item.name ? item.style : ''}
                onClick={() => setCurrentBtn(item.name)}
              >
                <FontAwesomeIcon icon={item.icon} className="icon" />
                {item.name}
              </ButtonComponent>
            ))}
          </div>
        </div>
        <div className="row col-xl-4 col-md-6 col-sm-12 justify-content-center">
          <TextBoxComponent
            placeholder="Search Job here..."
            floatLabelType="Auto"
            input={handleSearch}
          />
        </div>
      </div>
      <div className="selete-body row">
        {jobList.map((job: Job) => (
          <JobItem key={job.jobNo} job={job} />
        ))}
      </div>
    </div>
  )
}

export default JobSelect
