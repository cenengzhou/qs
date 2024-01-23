import { InputEventArgs } from '@syncfusion/ej2-react-inputs'

export const validateJobNo = (value: InputEventArgs) => {
  if (value.value && regex.test(value.value)) {
    value.container?.classList.add('e-success')
    value.container?.classList.remove('e-error')
  } else if (value.value && !regex.test(value.value)) {
    value.container?.classList.remove('e-success')
    value.container?.classList.add('e-error')
  } else {
    value.container?.classList.remove('e-error')
    value.container?.classList.remove('e-success')
  }
}

export const regex = /^(\d{5})$/g

export const getAddressIndex = (address: string) => {
  return Number(address.split('!')[1].replace(/[^\d]/g, '')) - 2
}

export const textBoxValidation = (value: InputEventArgs, len?: number) => {
  if (
    value.value &&
    ((len && value.value.length == len) || (!len && value.value.length > 0))
  ) {
    value.container!.classList.add('e-success')
  } else {
    value.container!.classList.remove('e-success')
  }
}
