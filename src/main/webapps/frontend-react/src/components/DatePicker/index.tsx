import React, { useRef } from 'react'

import {
  CalendarView,
  ChangedEventArgs,
  DatePickerComponent
} from '@syncfusion/ej2-react-calendars'

interface Props {
  placeholder?: string
  start?: CalendarView
  depth?: CalendarView
  format?: string
  value?: Date | string
  onChange?: (e: ChangedEventArgs) => void
  readonly?: boolean
}

const DatePicker = ({
  placeholder,
  onChange,
  format,
  start,
  depth,
  value,
  readonly
}: Props) => {
  const datePickerRef = useRef<DatePickerComponent>(null)

  const handleFocus = () => {
    datePickerRef.current?.show()
  }

  return (
    <div className="App">
      <DatePickerComponent
        placeholder={placeholder}
        format={format ? format : 'yyyy-MM-dd'}
        start={start ? start : undefined}
        depth={depth ? depth : undefined}
        ref={datePickerRef}
        cssClass="e-outline"
        floatLabelType={placeholder ? 'Auto' : 'Never'}
        value={value ? new Date(value) : undefined}
        onChange={onChange}
        onFocus={handleFocus}
        readonly={readonly}
      />
    </div>
  )
}

export default DatePicker
