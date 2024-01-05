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

export const tableMap = [
  ['A', 'id'],
  ['B', 'jobNo'],
  ['C', 'subcontract.packageNo'],
  ['D', 'sequenceNo'],
  ['E', 'description'],
  ['F', 'remark'],
  ['G', 'objectCode'],
  ['H', 'subsidiaryCode'],
  ['I', 'billItem'],
  ['J', 'unit'],
  ['K', 'lineType'],
  ['L', 'approved'],
  ['M', 'resourceNo'],
  ['N', 'scRate'],
  ['O', 'quantity'],
  ['P', 'amountCumulativeCert'],
  ['Q', 'cumCertifiedQuantity'],
  ['R', 'amountCumulativeWD'],
  ['S', 'cumWorkDoneQuantity'],
  ['T', 'amountPostedCert'],
  ['U', 'postedCertifiedQuantity'],
  ['V', 'amountSubcontractNew'],
  ['W', 'newQuantity'],
  ['X', 'originalQuantity'],
  ['Y', 'toBeApprovedRate'],
  ['Z', 'amountSubcontract'],
  ['AA', 'amountBudget'],
  ['AB', 'lastModifiedDate'],
  ['AC', 'lastModifiedUser'],
  ['AD', 'createdDate'],
  ['AE', 'createdUser'],
  ['AF', 'systemStatus'],
  ['AG', 'typeRecoverable']
]
export const numberMap = [
  [0, 'id'],
  [1, 'jobNo'],
  [2, 'subcontract.packageNo'],
  [3, 'sequenceNo'],
  [4, 'description'],
  [5, 'remark'],
  [6, 'objectCode'],
  [7, 'subsidiaryCode'],
  [8, 'billItem'],
  [9, 'unit'],
  [10, 'lineType'],
  [11, 'approved'],
  [12, 'resourceNo'],
  [13, 'scRate'],
  [14, 'quantity'],
  [15, 'amountCumulativeCert'],
  [16, 'cumCertifiedQuantity'],
  [17, 'amountCumulativeWD'],
  [18, 'cumWorkDoneQuantity'],
  [19, 'amountPostedCert'],
  [20, 'postedCertifiedQuantity'],
  [21, 'amountSubcontractNew'],
  [22, 'newQuantity'],
  [23, 'originalQuantity'],
  [24, 'toBeApprovedRate'],
  [25, 'amountSubcontract'],
  [26, 'amountBudget'],
  [27, 'lastModifiedDate'],
  [28, 'lastModifiedUser'],
  [29, 'createdDate'],
  [30, 'createdUser'],
  [31, 'systemStatus'],
  [32, 'typeRecoverable']
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

export const getAddressIndex = (address: string) => {
  return Number(address.split('!')[1].replace(/[^\d]/g, '')) - 2
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
