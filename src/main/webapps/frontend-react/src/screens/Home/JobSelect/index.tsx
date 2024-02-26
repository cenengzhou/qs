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
  const [start, setStart] = useState<boolean>(false)
  const [searchText, setSearchText] = useState<string>()
  const [currentBtn, setCurrentBtn] = useState<string>('ALL')
  const [onGoing, setOnGoing] = useState<boolean>(false)
  const page = useRef<number>(1)
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

  const btnSearch = (division: string) => {
    setCurrentBtn(division)
  }

  const handleSearch = (event: InputEventArgs) => {
    setSearchText(event.value)
  }

  const searchOnGoing = () => {
    setOnGoing(!onGoing)
  }

  useEffect(() => {
    setTimeout(() => {
      search(currentBtn, searchText, onGoing)
    }, 300)
  }, [currentBtn, searchText, onGoing])

  const search = (
    division: string,
    searchText: string = '',
    onGoing: boolean = false
  ) => {
    let list = [...newList]
    if (division !== 'ALL') {
      list = list.filter(item => item.division == division)
    }
    if (searchText && searchText.length > 0) {
      list = list.filter(
        item =>
          item.jobNo?.includes(searchText) ||
          item.division?.includes(searchText) ||
          item.description?.includes(searchText)
      )
    }
    if (onGoing) {
      list = list.filter(item => item.completionStatus == '1')
    }
    setJobList(list)
  }

  useEffect(() => {
    if (start) {
      const num = 200
      if ((page.current - 1) * num < newList.length) {
        setJobList(pre => [
          ...pre,
          ...newList.slice((page.current - 1) * num, page.current * num)
        ])
        page.current = page.current + 1
      }
    }
  }, [jobList, start])

  useEffect(() => {
    if (newList.length > 0 && start == false) {
      setStart(true)
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
                key={item.name}
                cssClass={currentBtn == item.name ? item.style : ''}
                onClick={() => btnSearch(item.name)}
              >
                <FontAwesomeIcon icon={item.icon} className="icon" />
                {item.name}
              </ButtonComponent>
            ))}
          </div>
        </div>
        <div className="row col-xl-4 col-md-6 col-sm-12 justify-content-center">
          <div className="col-7">
            <TextBoxComponent
              placeholder="Search Job here..."
              floatLabelType="Auto"
              cssClass="e-outline"
              input={handleSearch}
            />
          </div>
          <div
            className={'row col-5 switch-search ' + (onGoing ? 'on' : '')}
            onClick={() => searchOnGoing()}
          >
            <div className="col-12">
              <ButtonComponent cssClass="e-info">
                Include completed
              </ButtonComponent>
            </div>
            <div className="col-12">
              <ButtonComponent cssClass="e-warning">
                Only on going
              </ButtonComponent>
            </div>
          </div>
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
