import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { calculateDistance } from './GoogleHaversine'
import { Link } from 'react-router-dom'
import { FaLocationDot } from 'react-icons/fa6'

interface Item {
  id: number
  name: string
  description: string
  price: number
  city: string
  region: string
  subregion?: string // 선택적 속성
  imgUrl: string
}

interface AddressComponent {
  long_name: string
  short_name: string
  types: string[]
}

const GoogleMaps = () => {
  const [list, setList] = useState('')
  const [trades, setTrades] = useState<Item[]>([])
  const [currentPosition, setCurrentPosition] = useState<{
    latitude: number
    longitude: number
  } | null>(null)
  const [previousPosition, setPreviousPosition] = useState<{
    latitude: number
    longitude: number
  } | null>(null)
  const API_KEY = process.env.REACT_APP_GOOGLE_MAPS_API_KEY // 발급받은 Google API 키

  useEffect(() => {
          navigator.geolocation.getCurrentPosition(
              (position) => {
                const getLocation = async () => {
                  const { latitude, longitude } = position.coords
                  setCurrentPosition({ latitude, longitude })
                  try {
                    // 위치 정보 요청
                    const response = await axios.get(
                        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&language=ko&region=ko&key=${API_KEY}`,
                    )
                    const data = response.data
                    console.log(data, 'datata')
                    // 위치 주소 확인
                    const location = data.results[4].formatted_address
                    // 여기가 주소를 확인하는 곳
                    console.log(location)
                    setList(location)
                    // Mock 데이터 요청
                    const usdTradeResponse = await axios.get('/mock/usdTrade.json')
                    const usdData = usdTradeResponse.data

                    const addressComponents =
                        response.data.results[0].address_components

                    const searchAddress = (
                        components: AddressComponent[],
                        type: string,
                    ): AddressComponent | undefined => {
                      return components.find((components) =>
                          components.types.includes(type),
                      )
                    }

                    const region = searchAddress(addressComponents, 'political')

                    // 동네 필터링
                    const filteredData = usdData.filter((value: Item) => {
                      return value.subregion === region?.long_name.trim()
                    })
                    setTrades(filteredData)
                    if (previousPosition) {
                      // 거리 계산
                      const distance = calculateDistance(
                          previousPosition.latitude,
                          previousPosition.longitude,
                          latitude,
                          longitude,
                      )
                      if (distance >= 2) {
                        alert('2km 이상 이동하여 위치를 다시 가져옵니다.')
                        setPreviousPosition({ latitude, longitude })
                        // 위치 기반 데이터 필터링
                        console.log(filteredData, '필터된 데이터')
                        setTrades(filteredData)
                      }
                    } else {
                      setPreviousPosition({ latitude, longitude })
                    }
                  } catch (e) {
                    console.log(e)
                  }
                }
                getLocation()
              },
              (error) => {
                  console.error(error);
              },
              {
                enableHighAccuracy: true, // 정확한 위치 정보 요청
                timeout: 10000, // 10초 안에 위치 정보 가져오기
                maximumAge: 0, // 캐시된 위치 정보 사용하지 않기
              },
          )
  }, [previousPosition]) // 존재하는 데이터 변경될 떄만 실행

  const getImageUrl = (imageUrl: string) => {
    try {
      return require(`../../Assets/Img/home/useTrade/${imageUrl}`);
    } catch (error) {
      console.error('Error loading image:', error);
      return '';
    }
  };

  return (
    <div className="m-auto max-w-6xl p-5">
      <h1>사용자 위치 기반 주소 정보</h1>
      <div className="flex items-center">
        <FaLocationDot className="text-red-600 mr-2" />
        <span>{list}</span>
      </div>
      {trades && trades.length > 0 ? (
        <ul className="grid grid-cols-1 sm:grid-cols-1 lg:grid-cols-2 gap-4 mt-4">
          {trades.map((trade) => (
            <li key={trade.id} className="rounded-lg p-4 border">
              <Link to={`/secondhand/${trade.id}`} className="block">
                <img
                  src={getImageUrl(trade.imgUrl)}
                  alt={trade.name}
                  className="w-full object-cover rounded-lg mb-2"
                />
                <h2 className="text-sm font-semibold">{trade.name}</h2>
                <p className="text-base font-extralight">{trade.description}</p>
                <p className="text-lg font-bold">{trade.price.toLocaleString()}원</p>
                <p className="text-sm mb-1">
                  {trade.city} {trade.region} {trade.subregion}
                </p>
                <p className="text-sm text-gray-500">
                  관심 77 · 찜 78 · 채팅 84
                </p>
              </Link>
            </li>
          ))}
        </ul>
      ) : (
        <p>주소가 틀려요 위치 정보를 동의했나요?</p>
      )}

    </div>
  )
}

export default GoogleMaps
