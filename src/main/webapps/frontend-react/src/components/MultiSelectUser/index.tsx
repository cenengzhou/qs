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
  const [noRecordsText, setNoRecordsText] = useState<string>('No records found')

  const itemTemplate = (item: UserInfo) => {
    return (
      <div
        className={
          defaultValue.includes(item.username!) ? 'multi-selected' : ''
        }
      >
        <div>{item.fullName}</div>
        <div>{item.email}</div>
      </div>
    )
  }

  const valueTemplate = (item: UserInfo) => item.fullName

  const init = (values: string[]) => {
    setNoRecordsText('Please enter at least three letters')
    if (values && values.length > 0) {
      const users: UserInfo[] = []
      values.forEach(e => {
        const user = userList?.find(f => f.username === e)
        if (user) {
          users.push(user)
        }
      })
      setFilterUser(users)
    } else {
      setFilterUser([])
    }
  }

  useEffect(() => {
    init(value)
    setDefaultValue(value)
  }, [value])

  const onFocus = () => {
    msRef.current!.element.previousSibling!.addEventListener(
      'input',
      onInput,
      false
    )
  }

  const onBlur = () => {
    msRef.current!.element.previousSibling!.removeEventListener(
      'input',
      onInput,
      false
    )
  }

  const onInput = (e: any) => {
    const value = e.target.value
    init(defaultValue)
    if (value.length >= 3) {
      setNoRecordsText('No records found')
      if (userList && userList.length > 0) {
        const newUserList: UserInfo[] = userList.filter(
          item => item.fullName?.toUpperCase().includes(value.toUpperCase())
        )
        if (newUserList.length > 0) {
          setFilterUser(pre => [...pre, ...newUserList])
        }
      }
    }
  }

  return (
    <MultiSelectComponent
      ref={msRef}
      dataSource={filterUser}
      fields={{ text: 'fullName', value: 'username' }}
      value={defaultValue}
      floatLabelType="Always"
      itemTemplate={itemTemplate}
      noRecordsTemplate={() => <div>{noRecordsText}</div>}
      valueTemplate={valueTemplate}
      placeholder={placeholder}
      select={(item: SelectEventArgs) => {
        const username = (item.itemData as UserInfo).username
        if (username && defaultValue.indexOf(username) === -1) {
          setDefaultValue([...defaultValue, username])
          selected([...defaultValue, username].join(';'))
        }
      }}
      focus={onFocus}
      blur={onBlur}
      removed={(item: RemoveEventArgs) => {
        const newValue = [...defaultValue]
        newValue.splice(
          newValue.findIndex(e => e == (item.itemData as UserInfo)?.username),
          1
        )
        setDefaultValue(newValue)
        removed(newValue.join(';'))
      }}
    />
  )
}

export default MultiSelectUser
