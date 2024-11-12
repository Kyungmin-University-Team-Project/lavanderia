// tokenService.ts
// axios는 default export로 제공되기 때문에 단일로 가져와야함
import axios from "axios";
import {API_URL} from "../../Api/api";

/**
 * 토큰을 사용하여 접근을 인증하고, 만료되거나 유효하지 않은 경우 토큰을 재발급합니다.
 * @param {string} accessToken 인증에 사용될 액세스 토큰.
 * @returns {Promise} 응답 데이터를 해결하거나 에러로 거부하는 프로미스를 반환합니다.
 */

export const authenticateAccess = async (accessToken: string): Promise<any> => {
  try {
    const response = await axios.get("", {
      headers: {
        access: accessToken,
      },
    });
    // 액세스토큰 인증 성공시 반환값
    console.log("액세스토큰 인증 성공")
    return response.data;
  } catch (error: any) {
    console.log(error)
    if (error.response && error.response.statusText === "Unauthorized") {
      try {
        const reissueResponse = await axios.post(`${API_URL}/reissue`);

        // 리프레쉬토큰 재발급 성공시 반환값
        return reissueResponse.data;
      } catch (reissueError: any) {
        throw reissueError; // 호출자에게 오류를 전파합니다.
      }
    } else {
      throw error; // 다른 오류를 호출자에게 전파합니다.
    }
  }
};
