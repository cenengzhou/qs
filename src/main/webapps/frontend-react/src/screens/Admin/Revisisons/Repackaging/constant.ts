import { Repackaging } from '../../../../services'

export const selectQuery = [
  'id',
  'packageNo',
  'resourceDescription',
  'objectCode',
  'subsidiaryCode',
  'amountBudget',
  'quantity',
  'currIVAmount',
  'postedIVAmount',
  'rate',
  'unit',
  'excludeDefect',
  'excludeLevy',
  'forIvRollbackOnly',
  'finalized',
  'resourceType'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'id',
    b: 'packageNo',
    c: 'resourceDescription',
    d: 'objectCode',
    e: 'subsidiaryCode',
    f: 'amountBudget',
    g: 'quantity',
    h: 'currIVAmount',
    i: 'postedIVAmount',
    j: 'rate',
    k: 'unit',
    l: 'excludeDefect',
    m: 'excludeLevy',
    n: 'forIvRollbackOnly',
    o: 'finalized',
    p: 'resourceType'
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
  [key: string]: keyof Repackaging
}
