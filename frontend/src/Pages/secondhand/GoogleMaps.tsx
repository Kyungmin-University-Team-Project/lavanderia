import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { calculateDistance } from './GoogleHaversine'

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
                    // 위치 주소 확인
                    const location = data.results[3].formatted_address
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
                    console.log(region, 'asdasdas')
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

  return (
    <div className="m-auto max-w-6xl p-5">
      <h1>사용자 위치 기반 주소 정보</h1>
      <p>{list}</p>
      {trades.length > 0 ? (
        trades.map((trade) => (
          <p key={trade.id}>{trade.subregion} 주소 맞다!</p>
        ))
      ) : (
        <p>주소가 틀려요 위치 정보를 동의했나요?</p>
      )}
    </div>
  )
}

export default GoogleMaps
