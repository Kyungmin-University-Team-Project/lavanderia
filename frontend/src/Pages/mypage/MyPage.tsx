import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { AuthContext } from '../../Context'
import axiosInstance from '../../Utils/axios/axiosInstance'
import { FaAngleRight } from 'react-icons/fa'
import { API_URL } from "../../Api/api"

interface MemberInfo {
  memberBirth: string
  memberEmail: string
  memberPhone: string
  memberPoint: string
  memberName: string
  memberLevel: string
  memberId: string
  agreeMarketingYn: string
}

interface Profile {
  profileImg: File | null
}

const MyPage = () => {
  const [list, setList] = useState<MemberInfo>({
    memberBirth: '',
    memberEmail: '',
    memberPhone: '',
    memberPoint: '',
    memberName: '',
    memberLevel: '',
    memberId: '',
    agreeMarketingYn: '',
  })
  const { logout } = useContext(AuthContext)
  const [filed, setFile] = useState<Profile>({ profileImg: null })
  const [modalOpen, setModalOpen] = useState(false)

  useEffect(() => {
    const memberPost = async () => {
      try {
        const response = await axiosInstance.post(`${API_URL}/member-info`)
        const data = response.data
        setList(data)
      } catch (e: any) {
        if (e.response) {
          console.log('Response data:', e.response.data)
        }
      }
    }
    memberPost()
  }, [])

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { files, type } = e.target

    if (type === 'file' && files) {
      const selectedFile = files[0]
      setFile({ profileImg: selectedFile })
    }
  }

  const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (!filed.profileImg) {
      console.log('No file selected')
      return
    }

    const newForm = new FormData()
    newForm.append('profileImg', filed.profileImg)

    try {
      const response = await axiosInstance.post('/member-profile', newForm, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })

      console.log(response.data)
      setModalOpen(false) // Close modal after successful upload
    } catch (error) {
      console.error('Error during file upload:', error)
    }
  }

  return (
      <div className="w-full border-b-2 border-l-2 border-r border-gray-50 bg-white">
        <section className="p-4">
          <div className="flex items-center">
            <div className="h-16 w-16 overflow-hidden rounded-full border-b-2">
              <img
                  src="/img/basic.png"
                  alt="Profile"
                  className="h-full w-full object-cover"
              />
            </div>
            <div className="ml-4">
              <p className="text-lg font-semibold">{list.memberName}</p>
              <p className="text-sm text-gray-500">ID: {list.memberId}</p>
              <button onClick={logout} className="text-sm">
                로그아웃
              </button>
            </div>
          </div>
          <div className="mt-4 flex justify-center space-x-4">
            <button
                className="w-full rounded-b border-2 border-solid border-gray-50 p-2 text-sm font-medium hover:underline"
                onClick={() => setModalOpen(true)} // Open modal
            >
              프로필 이미지 변경
            </button>
            <button className="w-full rounded-b border-2 border-solid border-gray-50 p-2 text-sm font-medium hover:underline">
              닉네임 변경
            </button>
          </div>
        </section>

        {modalOpen && (
            <div className="fixed inset-0 z-50 flex justify-center items-center bg-gray-500 bg-opacity-50">
              <div className="bg-white p-6 rounded-md shadow-lg">
                <h2 className="text-lg font-semibold mb-4">프로필 이미지 변경</h2>
                <form onSubmit={handleOnSubmit}>
                  <input
                      type="file"
                      onChange={handleOnChange}
                      accept="image/*"
                      className="mb-4"
                  />
                  <div className="flex space-x-4">
                    <button
                        type="submit"
                        className="px-4 py-2 bg-blue-500 text-white rounded-md"
                    >
                      업로드
                    </button>
                    <button
                        type="button"
                        className="px-4 py-2 bg-gray-300 text-black rounded-md"
                        onClick={() => setModalOpen(false)} // Close modal
                    >
                      취소
                    </button>
                  </div>
                </form>
              </div>
            </div>
        )}

        <section className="p-4">
          <ul className="space-y-4">
            <li className="flex justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="mb-2 text-sm font-bold">회원정보 변경</p>
              <Link to="changeMemberInformation">
                <p className="text-sm text-gray-300 underline">
                  <FaAngleRight />
                </p>
              </Link>
            </li>

            <li className="flex justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="text-sm font-bold">비밀번호 변경</p>
              <Link to="changePassword">
                <p className="text-sm text-gray-300">
                  <FaAngleRight />
                </p>
              </Link>
            </li>
            <li className="flex justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="text-sm font-bold">주문 내역</p>
              <Link to="orderDetails">
                <p className="text-sm text-gray-300">
                  <FaAngleRight />
                </p>
              </Link>
            </li>
            <li className="flex justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="text-sm font-bold">배송지 관리</p>
              <Link to="deliveryAddressManagement">
                <p className="text-sm text-gray-300">
                  <FaAngleRight />
                </p>
              </Link>
            </li>
          </ul>
        </section>
      </div>
  )
}

export default MyPage
