import { useEffect, useState } from 'react'

import JobItem from '../../../components/JobItem'
import { setJobDescription, setJobNo } from '../../../redux/appConfigReducer'
import { closeLoading, openLoading } from '../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../redux/notificationReducer'
import { useAppDispatch } from '../../../redux/store'
import { Job, JobListResponse, useGetJobListMutation } from '../../../services'
import './style.scss'

const JobSelect = () => {
  const dispatch = useAppDispatch()
  const [newList, setNewList] = useState<JobListResponse>([])
  const [jobList, setJobList] = useState<JobListResponse>([])
  const [getJobList, { isLoading: getLoading }] = useGetJobListMutation()
  const [page, setPage] = useState(0)

  const getList = async () => {
    try {
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
    if (getLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading])

  return (
    <div className="selete-container">
      <div className="selete-header">
        <div className="row">
          <div id="text" className="e-btn-group">
            <input type="radio" id="left" name="align" value="left" />
            <label className="e-btn" htmlFor="left">
              Left
            </label>
            <input type="radio" id="center" name="align" value="center" />
            <label className="e-btn" htmlFor="center">
              Center
            </label>
            <input type="radio" id="right" name="align" value="right" />
            <label className="e-btn" htmlFor="right">
              Right
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
