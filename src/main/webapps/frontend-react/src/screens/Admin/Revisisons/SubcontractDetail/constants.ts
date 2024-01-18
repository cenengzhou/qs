import { SCDetail } from '../../../../services'

export const selectQuery = [
  'id',
  'jobNo',
  'subcontract.packageNo',
  'sequenceNo',
  'description',
  'remark',
  'objectCode',
  'subsidiaryCode',
  'billItem',
  'unit',
  'lineType',
  'approved',
  'resourceNo',
  'scRate',
  'quantity',
  'amountCumulativeCert',
  'cumCertifiedQuantity',
  'amountCumulativeWD',
  'cumWorkDoneQuantity',
  'amountPostedCert',
  'postedCertifiedQuantity',
  'amountSubcontractNew',
  'newQuantity',
  'originalQuantity',
  'toBeApprovedRate',
  'amountSubcontract',
  'amountBudget',
  'lastModifiedDate',
  'lastModifiedUser',
  'createdDate',
  'createdUser',
  'systemStatus',
  'typeRecoverable'
]

const getKey = (Key: string) => {
  const theKey: ScDetailType = {
    a: 'id',
    b: 'jobNo',
    d: 'sequenceNo',
    e: 'description',
    f: 'remark',
    g: 'objectCode',
    h: 'subsidiaryCode',
    i: 'billItem',
    j: 'unit',
    k: 'lineType',
    l: 'approved',
    m: 'resourceNo',
    n: 'scRate',
    o: 'quantity',
    p: 'amountCumulativeCert',
    q: 'cumCertifiedQuantity',
    r: 'amountCumulativeWD',
    s: 'cumWorkDoneQuantity',
    t: 'amountPostedCert',
    u: 'postedCertifiedQuantity',
    v: 'amountSubcontractNew',
    w: 'newQuantity',
    x: 'originalQuantity',
    y: 'toBeApprovedRate',
    z: 'amountSubcontract',
    aa: 'amountBudget',
    ab: 'lastModifiedDate',
    ac: 'lastModifiedUser',
    ad: 'createdDate',
    ae: 'createdUser',
    af: 'systemStatus',
    ag: 'typeRecoverable'
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

interface ScDetailType {
  [key: string]: keyof SCDetail
}
