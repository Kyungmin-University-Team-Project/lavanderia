import React, { useState, MouseEvent, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AgreementJoin } from "../../Typings/auth/Agreements";
import Logo from "../../Components/common/Logo";

const Agreement = () => {
    const [allIsChecked, setAllIsChecked] = useState(false);
    const [agreement, setAgreement] = useState<AgreementJoin[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch("/mock/agreements.json")
          .then((response) => response.json())
          .then((data) => {
              setAgreement(
                data.map((agreement: any) => ({
                    ...agreement,
                    checked: false,
                })),
              );
          })
          .catch((error) => console.error("Error fetching agreements:", error));
    }, []);

    useEffect(() => {
        if (allIsChecked) {
            window.scrollTo({
                top: document.body.scrollHeight,
                behavior: "smooth",
            });
        }
    }, [allIsChecked]);

    const onSingleCheck = (e: MouseEvent<HTMLInputElement>) => {
        const targetValue = (e.target as HTMLInputElement).value;
        const updatedAgreement = agreement.map((item) =>
          targetValue === item.value ? { ...item, checked: !item.checked } : item,
        );
        setAgreement(updatedAgreement);

        setAllIsChecked(
          updatedAgreement
            .filter((item) => item.value !== "check4")
            .every((item) => item.checked),
        );
    };

    const onAllCheck = (e: MouseEvent<HTMLInputElement>) => {
        const isChecked = (e.target as HTMLInputElement).checked;
        setAllIsChecked(isChecked);
        setAgreement(agreement.map((item) => ({ ...item, checked: isChecked })));
    };

    const isButtonDisabled = !agreement
      .filter((item) => item.value !== "check4")
      .every((item) => item.checked);

    const handleSubmit = () => {
        const marketingConsent = agreement.find(item => item.value === "check4")?.checked ? "Y" : "N";
        console.log(marketingConsent);

        navigate("/auth/signup", { state: { agreeMarketingYn: marketingConsent } });
    };

    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-100 p-4">
          <form
            className="w-full max-w-xl rounded-md border bg-white p-6 shadow sm:p-6 md:w-auto md:max-w-xl lg:p-6"
            onSubmit={(e) => {
                e.preventDefault();
                handleSubmit();
            }}
          >
              <div className="mb-5 flex w-full items-center border-b border-gray-400 pb-2">
                  <Logo/>
              </div>
              <div className="mb-4">
                  <label className="flex items-center">
                      <input
                        type="checkbox"
                        onClick={onAllCheck}
                        checked={allIsChecked}
                        value="all"
                        name="agreement"
                        className="h-5 w-5 border-gray-300 text-blue-600 focus:ring-blue-500 cursor-pointer"
                      />
                      <span className="ml-2 text-sm text-gray-700">모두 동의합니다.</span>
                  </label>
                  <p className="mt-2 text-sm text-gray-600">
                      실명 인증된 아이디로 가입, 위치기반서비스 이용약관(선택),
                      이벤트・혜택 정보 수신(선택) 동의를 포함합니다.
                  </p>
              </div>
              {agreement.map((item) => (
                <div key={item.value} className="mb-4">
                    <label className="block">
                        <div className="flex items-center">
                            <input
                              type="checkbox"
                              onClick={onSingleCheck}
                              value={item.value}
                              checked={item.checked}
                              name={item.name}
                              className="h-5 w-5 border-gray-300 text-blue-600 focus:ring-blue-500 cursor-pointer"
                            />
                            <span className="ml-2 text-sm font-medium text-gray-700">
                  {item.children}
                </span>
                        </div>
                        <div className="mt-2 w-full">
                <textarea
                  rows={10}
                  cols={50}
                  className="mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                  defaultValue={item.text}
                />
                        </div>
                    </label>
                </div>
              ))}
              <button
                type="submit"
                disabled={isButtonDisabled}
                className={`mt-4 w-full rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 ${
                  isButtonDisabled ? "cursor-not-allowed opacity-50" : ""
                }`}
              >
                  동의하고 진행하기
              </button>
          </form>
      </div>
    );
};

export default Agreement;
