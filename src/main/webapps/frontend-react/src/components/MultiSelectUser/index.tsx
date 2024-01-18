/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useRef, useState } from 'react'

import {
  MultiSelectComponent,
  RemoveEventArgs,
  SelectEventArgs
} from '@syncfusion/ej2-react-dropdowns'

import { UserInfo } from '../../services'

interface MultiSelectUserProps {
  userList: UserInfo[]
  value: string[]
  placeholder?: string
  selected: (args: string) => void
  removed: (args: string) => void
}

const MultiSelectUser = ({
  userList,
  value,
  placeholder,
  selected,
  removed
}: MultiSelectUserProps) => {
  const msRef = useRef<MultiSelectComponent>(null)
  const [filterUser, setFilterUser] = useState<UserInfo[]>([])
  const [defaultValue, setDefaultValue] = useState<string[]>([])
  const hasInput = useRef<boolean>(false)

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
    init()
    setDefaultValue(value)
  }, [value])

  useEffect(() => {
    if (msRef.current && !hasInput.current) {
      hasInput.current = true
      msRef.current!.element.previousSibling!.addEventListener(
        'input',
        (e: any) => {
          const value = e.target.value
          if (value.length >= 3) {
            if (userList && userList.length > 0) {
              const newUserList: UserInfo[] = userList.filter(
                item =>
                  item.fullName?.toUpperCase().includes(value.toUpperCase()) ||
                  item.username?.toUpperCase().includes(value.toUpperCase())
              )
              setFilterUser(pre => [...pre, ...newUserList])
            }
          } else {
            init()
          }
        },
        true
      )
    }
  }, [msRef.current])

  return (
    <MultiSelectComponent
      ref={msRef}
      dataSource={filterUser}
      fields={{ text: 'fullName', value: 'username' }}
      value={defaultValue}
      floatLabelType="Always"
      itemTemplate={itemTemplate}
      valueTemplate={valueTemplate}
      placeholder={placeholder}
      select={(item: SelectEventArgs) => {
        const username = (item.itemData as UserInfo).username
        if (username && defaultValue.indexOf(username) === -1) {
          setDefaultValue([...defaultValue, username])
          selected([...defaultValue, username].join(';'))
        }
      }}
      removed={(item: RemoveEventArgs) => {
        const oldValue = [...defaultValue]
        oldValue.splice(
          oldValue.findIndex(e => e == (item.itemData as UserInfo)?.username),
          1
        )
        setDefaultValue(oldValue)
        removed(oldValue.join(';'))
      }}
    />
  )
}

export default MultiSelectUser
