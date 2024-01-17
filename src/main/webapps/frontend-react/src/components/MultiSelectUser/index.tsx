import React, { useEffect, useState } from 'react'

import {
  FilteringEventArgs,
  MultiSelectComponent,
  RemoveEventArgs,
  SelectEventArgs
} from '@syncfusion/ej2-react-dropdowns'

import { UserInfo } from '../../services'

interface MultiSelectUserProps {
  userList: UserInfo[]
  value: string[]
  selected: (args: string) => void
  removed: (args: string) => void
}

const MultiSelectUser = ({
  userList,
  value,
  selected,
  removed
}: MultiSelectUserProps) => {
  const [filterUser, setFilterUser] = useState<UserInfo[]>()
  const [defaultValue, setDefaultValue] = useState<string[]>([])

  const itemTemplate = (item: UserInfo) => {
    return (
      <div className="user-item">
        <div className="user-item-name">{item.fullName}</div>
        <div className="user-item-email">{item.email}</div>
      </div>
    )
  }

  const valueTemplate = (item: UserInfo) => item.fullName

  const init = () => {
    if (value && value.length > 0) {
      const users: UserInfo[] = []
      value.forEach(e => {
        const user = userList?.find(f => f.username === e)
        if (user) {
          users.push(user)
        }
      })
      setFilterUser(users)
    }
  }

  useEffect(() => {
    console.log('value===', value)
    init()
  }, [value])

  useEffect(() => {
    setDefaultValue(value)
  }, [])

  useEffect(() => {
    console.log('newUserList===', filterUser)
  }, [filterUser])

  return (
    <MultiSelectComponent
      dataSource={filterUser}
      fields={{ text: 'fullName', value: 'username' }}
      allowFiltering={true}
      value={defaultValue}
      itemTemplate={itemTemplate}
      valueTemplate={valueTemplate}
      filterBarPlaceholder="Form"
      select={(item: SelectEventArgs) => {
        const oldValue = value
        selected([...oldValue, (item.itemData as UserInfo)?.username].join(';'))
      }}
      removed={(item: RemoveEventArgs) => {
        const oldValue = value
        oldValue.slice(
          oldValue.findIndex(e => e == (item.itemData as UserInfo)?.username),
          1
        )
        removed(oldValue.join(';'))
      }}
      filtering={(e: FilteringEventArgs) => {
        if (e.text.length >= 3) {
          if (userList && userList.length > 0) {
            const newUserList: UserInfo[] = userList.filter(
              item =>
                item.fullName?.toUpperCase().includes(e.text.toUpperCase()) ||
                item.username?.toUpperCase().includes(e.text.toUpperCase())
            )
            setFilterUser(newUserList)
          }
        }
      }}
    />
  )
}

export default MultiSelectUser
