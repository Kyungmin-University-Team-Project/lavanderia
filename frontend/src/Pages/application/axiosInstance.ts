import axios from 'axios'
import CryptoJS from 'crypto-js'
import {API_URL} from "../../Api/api";

const secretKey = process.env.REACT_APP_CRYPTOJS_KEY

const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: { 'X-Custom-Header': 'foobar' },
  withCredentials: true,
})

axiosInstance.interceptors.response.use(
  (config) => {
    const token = localStorage.getItem('access')

    if (token && secretKey) {
      const bytes = CryptoJS.AES.decrypt(token, secretKey)
      const decryptedToken = bytes.toString(CryptoJS.enc.Utf8)
      config.headers['access'] = decryptedToken
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

export default axiosInstance
