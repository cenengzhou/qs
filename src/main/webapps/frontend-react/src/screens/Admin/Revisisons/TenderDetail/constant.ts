import { TenderDetailList } from '../../../../services'

export const selectQuery = [
  'tender[jobNo]',
  'tender[packageNo]',
  'tender[vendorNo]',
  'sequenceNo',
  'description',
  'quantity',
  'rateBudget',
  'amountBudget',
  'rateSubcontract',
  'amountSubcontract',
  'amountForeign',
  'objectCode',
  'subsidiaryCode',
  'lineType',
  'unit',
  'billItem'
]
const getKey = (Key: string) => {
  const theKey: Type = {
    d: 'sequenceNo',
    e: 'description',
    f: 'quantity',
    g: 'rateBudget',
    h: 'amountBudget',
    i: 'rateSubcontract',
    j: 'amountSubcontract',
    k: 'amountForeign',
    l: 'objectCode',
    m: 'subsidiaryCode',
    n: 'lineType',
    o: 'unit',
    p: 'billItem'
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
  [key: string]: keyof TenderDetailList
}
