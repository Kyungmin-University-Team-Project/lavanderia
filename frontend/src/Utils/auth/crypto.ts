import CryptoJS from 'crypto-js';

const secretKey = process.env.REACT_APP_CRYPTOJS_KEY;
if (!secretKey) {
  throw new Error('REACT_APP_CRYPTOJS_KEY 환경 변수가 설정되지 않았습니다.');
}

export const encryptToken = (token: string): string => {
  return CryptoJS.AES.encrypt(token, secretKey).toString();
};

export const decryptToken = (encryptedToken: string): string | null => {
  try {
    const bytes = CryptoJS.AES.decrypt(encryptedToken, secretKey);
    const decryptedToken = bytes.toString(CryptoJS.enc.Utf8);
    return decryptedToken;
  } catch (error) {
    console.error('Failed to decrypt token:', error);
    return null;
  }
};
