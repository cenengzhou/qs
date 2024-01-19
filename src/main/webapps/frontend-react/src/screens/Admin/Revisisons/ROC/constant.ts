import { RocAdmin } from '../../../../services'

export const selectQuery = [
  'itemNo',
  'projectRef',
  'rocCategory',
  'classification',
  'impact',
  'description',
  'rocOwner',
  'openDate',
  'closedDate',
  'status',
  'systemStatus'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'itemNo',
    b: 'projectRef',
    c: 'rocCategory',
    d: 'classification',
    e: 'impact',
    f: 'description',
    g: 'rocOwner',
    h: 'openDate',
    i: 'closedDate',
    j: 'status',
    k: 'systemStatus'
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
  [key: string]: keyof RocAdmin
}
