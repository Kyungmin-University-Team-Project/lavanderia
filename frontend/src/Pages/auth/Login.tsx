import axios from "axios";
import React, { useRef, useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../../Context";
import Logo from "../../Components/common/Logo";

const Login = () => {
  const { login } = useContext(AuthContext); // authContext에서 login 함수 가져오기
  const [formData, setFormData] = useState({ memberId: "", memberPwd: "" });
  const [error, setError] = useState("");
  const passwordInputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const [rememberMe, setRememberMe] = useState(false);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault(); // Prevent the default form submission behavior

    const isUsernameValid = formData.memberId.trim() !== "";
    const isPasswordValid = formData.memberPwd.trim() !== "";

    if (!isUsernameValid) {
      setError("아이디를 입력해주세요");
      return;
    }

    if (!isPasswordValid) {
      setError("비밀번호를 입력해주세요");
      return;
    }

    setError("");

    // 서버랑 연결 필요
    if (isUsernameValid && isPasswordValid) {
        try {
          console.log(formData)
            const response = await axios.post("/signin", { memberId: formData.memberId, memberPwd: formData.memberPwd });
            console.log(response.data)
            const accessToken = response.data.token;
            login(accessToken, rememberMe);  // Context의 login 메소드 호출
            navigate('/'); // 로그인 후 홈으로 이동
        } catch (error) {
            setError("아이디 또는 비밀번호가 일치하지 않습니다.");
            console.error("Error:", error);
        }
    }


    // 하드 코딩 테스트 버전
    // 나중에 서버쪽으로 리프레쉬 토큰 넘겨서 재로그인 하는 방식으로 변경하기
    // if (formData.userId === "user" && formData.password === "1234") {
    //   try {
    //     const accessToken = "hardcoded-access-token";
    //     const refreshToken = "hardcoded-refresh-token";
    //
    //     login(accessToken, refreshToken, rememberMe);  // Context의 login 메소드 호출
    //
    //     navigate("/"); // 로그인 후 홈으로 이동
    //   } catch (error) {
    //     setError("로그인 중 오류가 발생했습니다.");
    //     console.error("Error:", error);
    //   }
    // } else {
    //   setError("아이디 또는 비밀번호가 일치하지 않습니다.");
    // }
  }

  const handleCheckboxChange = () => {
    setRememberMe((prevRememberMe) => !prevRememberMe);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value
    }));
    if (value.trim() !== "") {
      setError("");
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      event.preventDefault();
      const { name, value } = event.currentTarget;
      setFormData((prevFormData) => ({
        ...prevFormData,
        [name]: value
      }));
      if (value.trim() !== "") {
        setError("");
      }
      if (name === "userId" && passwordInputRef.current !== null) {
        passwordInputRef.current.focus();
      }
    }
  };

  return (
    <div className="flex h-screen items-center justify-center">
      <div
        className="flex flex-col w-full h-full items-center justify-center gap-4 rounded-lg bg-white text-center border shadow lg:h-[500px] lg:w-[400px] lg:mb-20">
        <div className="flex w-full items-center justify-center lg:h-20">
            <Logo/>
        </div>

        <form className="w-full max-w-xs" onSubmit={handleSubmit}>
          <div className="mb-3 flex w-full flex-col items-center justify-center border rounded">
            <input
              className={`h-12 w-full border ${error.includes("아이디") ? "border-red-400" : "border-gray-300"} p-3 placeholder-gray-400 placeholder:text-base rounded-t`}
              type="text"
              name="memberId"
              placeholder="아이디 입력"
              value={formData.memberId}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
            />
            <input
              className={`h-12 w-full border ${error.includes("비밀번호") || error.includes("일치하지 않습니다.") ? "border-red-400" : "border-gray-300"} p-3 placeholder-gray-400 placeholder:text-base rounded-b`}
              type="password"
              name="memberPwd"
              placeholder="비밀번호 8자~20자"
              value={formData.memberPwd}
              onChange={handleChange}
              ref={passwordInputRef}
            />
          </div>
          {error && (
            <div className="text-red-500 text-sm mb-2">
              {error}
            </div>
          )}

          <button
            type="submit"
            className="mb-5 h-10 w-full rounded border bg-blue-400 text-base font-bold text-white md:h-12 md:text-xl"
          >
            로그인
          </button>

          <label className="mb-5 flex items-center cursor-pointer">
            <input
              className="h-5 w-5 rounded-full border text-blue-500 focus:ring-0 "
              type="checkbox"
              name="remember"
              checked={rememberMe}
              onChange={handleCheckboxChange}
            />
            <span className="ml-2">로그인 상태 유지</span>
          </label>
        </form>

        <div className="mb-5 flex gap-10">
          <button>
            <img
              src="/img/google.png"
              alt="Google"
              className="h-10 rounded-full"
            />
          </button>
          <button>
            <img src="/img/naver.png" alt="Naver" className="h-10 rounded-full" />
          </button>
          <button>
            <img src="/img/kakao.png" alt="Kakao" className="h-10 rounded-full" />
          </button>
        </div>

        <div className="text-sm font-bold text-black-600">
          <Link to="/auth/find">아이디 찾기</Link>
          <span className="mx-2 text-xl text-gray-400">·</span>
          <Link to="/auth/find">비밀번호 찾기</Link>
          <span className="mx-2 text-xl text-gray-400">·</span>
          <Link to="/auth/agreement">회원가입</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
