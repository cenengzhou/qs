import { RocDetail } from '../../../../services'

export const selectQuery = [
  'itemNo',
  'year',
  'month',
  'amountBest',
  'amountRealistic',
  'amountWorst',
  'remarks',
  'status',
  'systemStatus'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'itemNo',
    b: 'year',
    c: 'month',
    d: 'amountBest',
    e: 'amountRealistic',
    f: 'amountWorst',
    g: 'remarks',
    h: 'status',
    i: 'systemStatus'
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
  [key: string]: keyof RocDetail
}
