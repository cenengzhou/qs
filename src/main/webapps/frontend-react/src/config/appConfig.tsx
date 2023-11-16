/* eslint-disable @typescript-eslint/naming-convention */
export interface ENV {
  [key: string]: string[]
}
export const setEnv = () => {
  let env: string = 'PRO'
  const envList: ENV = {}
  envList['DEV'] = ['localhost', 'erpwls11', 'gambpm11', 'gamerp11:7207', 'dev']
  envList['UAT'] = ['erpwls12', 'gambpm12', 'gamerp11:7208', 'uat']
  envList['PRO'] = ['erpwls01', 'erpwls02']
  for (const key in envList) {
    envList[key].forEach((e: string) => {
      if (
        window.location.host.indexOf(e) >= 0 ||
        window.location.toString().toUpperCase().indexOf(key) >= 0
      ) {
        env = key
      }
    })
  }
  //cleanupLocalStore();
  return env
}
