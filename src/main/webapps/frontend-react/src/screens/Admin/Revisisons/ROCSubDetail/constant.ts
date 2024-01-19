import { RocSubDetail } from '../../../../services'

export const selectQuery = [
  'itemNo',
  'description',
  'amountBest',
  'amountRealistic',
  'amountWorst',
  'year',
  'month',
  'hyperlink',
  'remarks',
  'systemStatus'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'itemNo',
    b: 'description',
    c: 'amountBest',
    d: 'amountRealistic',
    e: 'amountWorst',
    f: 'amountWorst',
    g: 'year',
    h: 'month',
    i: 'hyperlink',
    j: 'remarks',
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
  [key: string]: keyof RocSubDetail
}
