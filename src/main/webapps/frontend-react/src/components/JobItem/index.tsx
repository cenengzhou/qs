import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import {
  faCity,
  faDigging,
  faDollarSign,
  faGasPump,
  faHelmetSafety,
  faRoadBarrier
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import { setJobDescription, setJobNo } from '../../redux/appConfigReducer'
import { RootState, useAppSelector } from '../../redux/store'
import { Job } from '../../services'
import './style.scss'

interface Props {
  job: Job
}

const JobItem = ({ job }: Props) => {
  const { jobNo } = useAppSelector((state: RootState) => state.appConfig)
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const onClick = (jon: Job) => {
    dispatch(setJobNo(jon.jobNo))
    dispatch(setJobDescription(jon.description))
  }

  useEffect(() => {
    if (jobNo) {
      navigate('/home/job/dashboard')
    }
  }, [jobNo])

  return (
    <div className="job-item-container col-xl-3 col-md-4 col-sm-6">
      <div
        className={`job-item ${
          job.division == 'BDG'
            ? 'bg-primary'
            : job.division == 'CVL'
            ? 'bg-orange'
            : job.division == 'E&M'
            ? 'bg-success'
            : job.division == 'FDN'
            ? 'bg-inverse'
            : job.division == 'SGP'
            ? 'bg-purple'
            : 'bg-primary'
        }`}
        onClick={() => onClick(job)}
      >
        {job.completionStatus !== '1' && (
          <span className="badge">Completed</span>
        )}
        <FontAwesomeIcon
          icon={
            job.division == 'BDG'
              ? faCity
              : job.division == 'CVL'
              ? faRoadBarrier
              : job.division == 'E&M'
              ? faGasPump
              : job.division == 'FDN'
              ? faDigging
              : job.division == 'SGP'
              ? faDollarSign
              : faHelmetSafety
          }
          size="3x"
          className="left"
        />
        <div className="right">
          <div>{job.jobNo}</div>
          <div>{job.description}</div>
        </div>
      </div>
    </div>
  )
}

export default JobItem
