import axios from 'axios';

interface AddressComponent {
    address_name: string;
    region_1depth_name: string;
    region_2depth_name: string;
    region_3depth_name: string;
}

interface KakaoGeocodeResult {
    documents: {
        address: AddressComponent;
    }[];
}

const reverseGeocode = async (latitude: number, longitude: number, apiKey: string): Promise<{ city: string; region: string; subregion: string }> => {
    if (!apiKey) {
        throw new Error('Kakao Maps API key is missing.');
    }

    try {
        const response = await axios.get<KakaoGeocodeResult>(
          `https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${longitude}&y=${latitude}`,
          {
              headers: {
                  Authorization: `KakaoAK ${apiKey}`
              }
          }
        );

        if (!response.data.documents || response.data.documents.length === 0) {
            console.error('not Fonid')
        }

        let city = 'Unknown City';
        let region = 'Unknown Region';
        let subregion = 'Unknown Subregion';

        // 결과 배열을 순회하며 필요한 주소 구성 요소 찾기
        for (const document of response.data.documents) {
            const address = document.address;
            console.log("Address Component:", address);

            if (address.region_1depth_name) {
                region = address.region_1depth_name;
            }
            if (address.region_2depth_name) {
                city = address.region_2depth_name;
            }
            if (address.region_3depth_name) {
                subregion = address.region_3depth_name;
            }

            // 모든 구성 요소를 찾았으면 순회 중단
            if (region !== 'Unknown Region' && city !== 'Unknown City' && subregion !== 'Unknown Subregion') {
                break;
            }
        }

        console.log(`Parsed Address: city=${city}, region=${region}, subregion=${subregion}`);
        return { city, region, subregion };
    } catch (error) {
        console.error("Full error:", error);
        if (axios.isAxiosError(error)) {
            console.error("Axios error response data:", error.response?.data);
        } else {
            console.error("Unexpected error:", error);
        }
        throw new Error('Failed to fetch geocode data');
    }
};

export default reverseGeocode;
