import { GLOBALPARAMETER } from '../../../../constants/global'
import { ScStandardTerms } from '../../../../services'

export function getPaymentTerm(): string {
  const data: Array<string> = []
  GLOBALPARAMETER.paymentTerms.forEach(item => {
    data.push(`${item.id} - ${item.value}`)
  })
  return data.join(',')
}

export function getRetentionTerm(): string {
  const data: Array<string> = []
  GLOBALPARAMETER.retentionTerms.forEach(item => {
    data.push(item.value)
  })
  return data.join(',')
}
export const selectQuery = [
  'id',
  'company',
  'formOfSubcontract',
  'scPaymentTerm',
  'scMaxRetentionPercent',
  'scInterimRetentionPercent',
  'scMOSRetentionPercent',
  'retentionType'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'company',
    b: 'formOfSubcontract',
    c: 'scPaymentTerm',
    d: 'scMaxRetentionPercent',
    e: 'scInterimRetentionPercent',
    f: 'scMOSRetentionPercent',
    g: 'retentionType'
  }
  return theKey[Key]
}

export const getAddressKey = (address: string) => {
  return getKey(
    address
      .split('!')[1]
      .replace(/[^a-zA-Z]/g, '')
      .toLowerCase()
  )
}

interface Type {
  [key: string]: keyof ScStandardTerms
}
