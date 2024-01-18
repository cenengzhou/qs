import { ADDENDUM } from '../../../../services'

export const selectQuery = [
  'id',
  'description',
  'remarks',
  'codeObject',
  'codeSubsidiary',
  'codeObjectForDaywork',
  'bpi',
  'unit',
  'typeVo',
  'idResourceSummary',
  'idHeaderRef',
  'noSubcontractChargedRef',
  'rateAddendum',
  'rateBudget',
  'quantity',
  'amtAddendum',
  'amtBudget',
  'dateLastModified',
  'usernameLastModified',
  'dateCreated',
  'usernameCreated',
  'typeRecoverable'
]

const getKey = (Key: string) => {
  const theKey: Type = {
    a: 'id',
    b: 'description',
    c: 'remarks',
    d: 'codeObject',
    e: 'codeSubsidiary',
    f: 'codeObjectForDaywork',
    g: 'bpi',
    h: 'unit',
    i: 'typeVo',
    j: 'idResourceSummary',
    k: 'idHeaderRef',
    l: 'noSubcontractChargedRef',
    m: 'rateAddendum',
    n: 'rateBudget',
    o: 'quantity',
    p: 'amtAddendum',
    q: 'amtBudget',
    r: 'dateLastModified',
    s: 'usernameLastModified',
    t: 'dateCreated',
    u: 'usernameCreated',
    v: 'typeRecoverable'
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
  [key: string]: keyof ADDENDUM
}
