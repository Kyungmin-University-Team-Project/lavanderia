import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { AuthContext } from '../../Context'
import axiosInstance from '../../Utils/axios/axiosInstance'
import { FaAngleRight } from 'react-icons/fa'
import {API_URL} from "../../Api/api";

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

  useEffect(() => {
    const memberPost = async () => {
      try {
        const response = await axiosInstance.post(`${API_URL}/member-info`);
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

  return (
      <div className=" w-full  border-b-2 border-l-2 border-r border-gray-50 bg-white">
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
            <button className="w-full rounded-b border-2 border-solid border-gray-50  p-2 text-sm font-medium hover:underline">
              프로필 이미지 변경
            </button>
            <button className="w-full rounded-b border-2  border-solid border-gray-50 p-2  text-sm font-medium hover:underline">
              닉네임 변경
            </button>
          </div>
        </section>

        <section className="p-4">
          <ul className="space-y-4">
            <li className="flex  justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="mb-2 text-sm font-bold  ">회원정보 변경</p>
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
              <p className="text-sm font-bold">취소 내역</p>
              <Link to="cancellationDetails">
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
            <li className="flex justify-between border-b-2 border-solid border-gray-50 pb-4">
              <p className="text-sm font-bold">결제 관리</p>
              <Link to="paymentManagement">
                <p className="text-sm text-gray-300">
                  <FaAngleRight />
                </p>
              </Link>
            </li>
            <li className="flex justify-between">
              <p className=" text-sm font-bold">알림 설정</p>
              <Link to="notificationSettings">
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