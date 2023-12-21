import { useEffect } from 'react'

import { hideSpinner, showSpinner } from '@syncfusion/ej2-react-popups'

import { selectIsLoading } from '../../redux/loadingReducer'
import { useAppSelector } from '../../redux/store'

const Loading = () => {
  const isLoading = useAppSelector(selectIsLoading)

  useEffect(() => {
    const root = document.getElementById('root')!
    if (isLoading) {
      showSpinner(root)
    } else {
      hideSpinner(root)
    }
  }, [isLoading])
  return <></>
}

export default Loading
