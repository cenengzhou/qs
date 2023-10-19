import React, { useRef } from 'react'
import { DatePickerComponent, ChangedEventArgs } from '@syncfusion/ej2-react-calendars'

interface Props {
  placeholder?: string
  onChange?: (e: ChangedEventArgs) => void
}

function DatePicker({placeholder, onChange}: Props) {
  const datePickerRef = useRef<DatePickerComponent | null>();

  const handleFocus = () => {
    if (datePickerRef.current) {
      datePickerRef.current.show();
    }
  };

  return (
    <div className="App">
      <DatePickerComponent
        placeholder={placeholder}
        format="yyyy-MM-dd"
        ref={e => datePickerRef.current = e}
        cssClass="e-outline"
        floatLabelType={placeholder ? "Auto" : "Never"}
        onChange={onChange}
        onFocus={handleFocus}
      />
    </div>
  );
}

export default DatePicker
