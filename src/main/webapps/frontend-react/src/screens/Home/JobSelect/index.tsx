import { useEffect, useState } from 'react'

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

  const getList = async () => {
    try {
      const completedCacheKey = await getCacheKey('COMPLETED_JOB_LIST').unwrap()
      const onGingCacheKey = await getCacheKey('ONGOING_JOB_LIST').unwrap()
      const localCompletedJobList = CryptoJS.AES.decrypt(
        localStorage.getItem('COMPLETED_JOB_LIST') || '',
        completedCacheKey
      )
      const localOngoingJobList = CryptoJS.AES.decrypt(
        localStorage.getItem('ONGOING_JOB_LIST') || '',
        onGingCacheKey
      )
      console.log(localCompletedJobList, localOngoingJobList)
      const completedJobList = await getJobList(true).unwrap()
      const ongoingJobList = await getJobList(false).unwrap()
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
    dispatch(setJobNo(''))
    dispatch(setJobDescription(''))
    getList()
  }, [])

  useEffect(() => {
    if (cacheKeyLoading || getLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading])

  return (
    <div className="selete-container">
      <div className="selete-header row">
        <div className="row col-xl-4 col-md-6 col-sm-12">
          <div className="e-btn-group">
            <ButtonComponent cssClass="bgd">
              <FontAwesomeIcon icon={faCity} className="icon" />
              BGD
            </ButtonComponent>
            <input type="radio" id="BGD" name="align" value="BGD" />
            <label className="e-btn bgd" htmlFor="BGD">
              <FontAwesomeIcon icon={faCity} className="icon" />
              BGD
            </label>
            <input type="radio" id="CVL" name="align" value="CVL" />
            <label className="e-btn cvl" htmlFor="CVL">
              <FontAwesomeIcon icon={faRoadBarrier} className="icon" />
              CVL
            </label>
            <input type="radio" id="E&M" name="align" value="E&M" />
            <label className="e-btn em" htmlFor="E&M">
              <FontAwesomeIcon icon={faGasPump} className="icon" />
              E&M
            </label>
            <input type="radio" id="FDN" name="align" value="FDN" />
            <label className="e-btn fdn" htmlFor="FDN">
              <FontAwesomeIcon icon={faDigging} className="icon" />
              FDN
            </label>
            <input type="radio" id="SGP" name="align" value="SGP" />
            <label className="e-btn sgp" htmlFor="SGP">
              <FontAwesomeIcon icon={faDollarSign} className="icon" />
              SGP
            </label>
            <input type="radio" id="ALL" name="align" value="ALL" />
            <label className="e-btn all" htmlFor="ALL">
              <FontAwesomeIcon icon={faTableCells} className="icon" />
              ALL
            </label>
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
