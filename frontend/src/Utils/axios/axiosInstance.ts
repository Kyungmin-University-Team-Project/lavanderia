import axios from "axios";
import {API_URL} from "../../Api/api";
import {decryptToken, encryptToken} from "../auth/crypto";

const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {"Content-Type": "application/json"},
    withCredentials: true,
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
    (config) => {
        const encryptedToken = localStorage.getItem("access"); // 암호화된 토큰 가져오기

        if (encryptedToken) {
            const token = decryptToken(encryptedToken); // 복호화
            if (token) {
                config.headers["access"] = token; // 복호화된 토큰을 헤더에 설정
            }
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        // 401 에러 및 재시도 여부 확인
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // 재시도 방지 플래그 설정

            try {
                
                console.log("여긴 오나")
                // 토큰 재발급 요청

                const reissueResponse = await axios.post(`${API_URL}/reissue`, {}, {
                    withCredentials: true, // 쿠키 포함
                });

                const newAccessToken = reissueResponse.data.accessToken;

                console.log(newAccessToken)


                // 새 토큰 저장
                if (newAccessToken) {
                    const encryptedToken = encryptToken(newAccessToken); // 토큰 암호화
                    localStorage.setItem("access", encryptedToken);
                }

                // 원래 요청에 새 토큰 설정 후 재시도
                originalRequest.headers["access"] = newAccessToken;
                return axiosInstance(originalRequest);
            } catch (reissueError) {
                console.error("토큰 재발급 실패:", reissueError);
                // 재발급 실패 시 인증 실패 처리
                return Promise.reject(reissueError);
            }
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
