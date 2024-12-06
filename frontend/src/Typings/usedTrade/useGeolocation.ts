import { useEffect, useState } from 'react';
import { calculateDistance } from '../../Typings/usedTrade/calculateDistance';

export default function useGeolocation() {
    // 위치 정보 상태를 저장
    const [location, setLocation] = useState<{ latitude: number | null; longitude: number | null }>({
        latitude: null,
        longitude: null,
    });
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        // 브라우저가 지리적 위치 기능을 지원하지 않는다면 에러를 던짐
        if (!navigator.geolocation) {
            setError('Geolocation is not supported');
            return;
        }

        let lastLocation: { latitude: number; longitude: number } | null = null;

        // 500미터 이상일 시 다시 위치 조회
        const handleSuccess = (position: GeolocationPosition) => {
            const { latitude, longitude } = position.coords;

            if (lastLocation) {
                const distance = calculateDistance(
                    lastLocation.latitude,
                    lastLocation.longitude,
                    latitude,
                    longitude
                );

                if (distance < 500) {
                    return;
                }
            }

            lastLocation = { latitude, longitude };
            setLocation({ latitude, longitude });
        };

        const handleError = (error: GeolocationPositionError) => {
            setError(error.message);
        };

        // 정확도 향상 및, 10초 제한 시간, 캐시된 위치
        const options = {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0 // Do not use a cached position
        };

        // 위치 감시
        const watchId = navigator.geolocation.watchPosition(handleSuccess, handleError, options);

        return () => {
            navigator.geolocation.clearWatch(watchId);
        };
    }, []);

    return { location, error };
}
