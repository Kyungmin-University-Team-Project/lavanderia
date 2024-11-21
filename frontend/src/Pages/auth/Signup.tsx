import React, {useState, useRef, useEffect} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import axios from 'axios';
import Logo from "../../Components/common/Logo";
import {API_URL} from "../../Api/api";
import axiosInstance from "../../Utils/axios/axiosInstance";

interface FormData {
    memberName: string;
    memberPhone: string;
    memberEmail: string;
    memberId: string;
    memberPwd: string;
    confirmPassword: string;
    agreeMarketingYn: string;
    memberBirth: string;
}

const fieldNames: (keyof FormData)[] = ['memberName', 'memberPhone', 'memberEmail', 'memberId', 'memberPwd', 'confirmPassword', 'memberBirth'];

const Signup = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const [formData, setFormData] = useState<FormData>({
        memberName: '',
        memberPhone: '010 ',
        memberEmail: '',
        memberId: '',
        memberPwd: '',
        confirmPassword: '',
        agreeMarketingYn: location.state?.agreeMarketingYn ? "Y" : "N",
        memberBirth: ''
    });
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [isFocused, setIsFocused] = useState<{ [key: string]: boolean }>({});
    const [step, setStep] = useState(1);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [inputCode, setInputCode] = useState('');
    const inputRefs = useRef<Array<HTMLInputElement | null>>([]);
    const [isCooldown, setIsCooldown] = useState(false);
    const [cooldownTime, setCooldownTime] = useState(0);


    useEffect(() => {
        const currentInput = inputRefs.current[step - 1];
        if (currentInput) {
            currentInput.focus();
            currentInput.scrollIntoView({behavior: 'smooth'});
        }
    }, [step]);

    const getErrorMessage = (id: keyof FormData) => {
        switch (id) {
            case 'memberName':
                return '이름을 입력해주세요';
            case 'memberPhone':
                return '휴대폰 번호를 입력해주세요';
            case 'memberEmail':
                return '이메일을 입력해주세요';
            case 'memberId':
                return '아이디를 입력해주세요';
            case 'memberPwd':
                return '비밀번호를 입력해주세요';
            case 'confirmPassword':
                return '비밀번호 확인을 입력해주세요';
            case 'memberBirth':
                return '생년월일을 입력해주세요 YYYY-MM-DD';
            default:
                return '';
        }
    };

    const getInvalidMessage = (id: keyof FormData) => {
        switch (id) {
            case 'memberName':
                return '유효한 이름을 입력해주세요';
            case 'memberPhone':
                return '유효한 휴대폰 번호를 입력해주세요';
            case 'memberEmail':
                return '유효한 이메일을 입력해주세요';
            case 'memberId':
                return '유효한 아이디를 입력해주세요';
            case 'memberPwd':
                return '비밀번호는 최소 8자 이상이어야 해요';
            case 'confirmPassword':
                return '비밀번호가 일치하지 않습니다';
            case 'memberBirth':
                return '유효한 생년월일을 입력해주세요';
            default:
                return '';
        }
    };

    const validateInput = (id: keyof FormData, value: string) => {
        const newErrors: { [key: string]: string } = {...errors};
        const patterns: { [key in keyof FormData]?: RegExp } = {
            memberName: /^[가-힣]{2,}$/,
            memberPhone: /^010 \d{4} \d{4}$/,
            memberEmail: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
            memberId: /^[a-zA-Z0-9]{4,}$/,
            memberBirth: /^(19|20)\d{2}-(0\d|1[0-2])-(0\d|[12]\d|3[01])$/
        };

        if (!value.trim()) {
            newErrors[id] = getErrorMessage(id);
        } else if (patterns[id] && !patterns[id]!.test(value.trim())) {
            newErrors[id] = getInvalidMessage(id);
        } else if (id === 'memberPwd' && value.length < 8) {
            newErrors.memberPwd = getInvalidMessage(id);
        } else if (id === 'confirmPassword' && value !== formData.memberPwd) {
            newErrors.confirmPassword = getInvalidMessage(id);
        } else {
            delete newErrors[id];
        }

        setErrors(newErrors);

        return Object.keys(newErrors).length === 0;
    };

    const handleNextStep = () => {
        const currentStepId = fieldNames[step - 1];

        if (validateInput(currentStepId, formData[currentStepId])) {
            if (step === fieldNames.length) {
                setIsModalOpen(true)
            } else {
                setStep(step + 1);
            }
        } else {
            inputRefs.current[step - 1]?.focus();
        }
    };


    useEffect(() => {
        if (cooldownTime > 0) {
            const timer = setInterval(() => {
                setCooldownTime((prev) => prev - 1);
            }, 1000);

            return () => clearInterval(timer); // Cleanup interval
        } else {
            setIsCooldown(false);
        }
    }, [cooldownTime]);

    const handleSendEmailCode = async () => {
        if (isCooldown) return; // Prevent multiple clicks during cooldown

        try {
            const response = await axiosInstance.post(`${API_URL}/send-signup-code`, formData.memberEmail);
            console.log('Email sent successfully:', response.data);

            alert('인증 코드가 이메일로 전송되었습니다.');

            setIsCooldown(true);
            setCooldownTime(30);
        } catch (error) {
            console.error('Error sending email:', error);
            alert('이메일 전송 중 문제가 발생했습니다. 다시 시도해주세요.');
        }
    };

    // 회원가입 api
    const handleSubmit = async () => {
        try {
            const cleanedFormData = {
                ...formData,
                memberPhone: formData.memberPhone.split(' ').join(''),
            };

            // 회원가입 로직
            const response = await axios.post(`${API_URL}/signup`, cleanedFormData);
            navigate('/'); // 홈으로 네비게이트

        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.error('Axios Error:', error.message);
                if (error.response) {
                    console.error('Server responded with:', error.response.data);
                }
            } else {
                console.error('Unexpected Error:', error);
            }
        }
    };

    const handleVerifyEmail = async () => {
        console.log(inputCode, formData.memberEmail,)
        try {
            const response = await axiosInstance.post(`${API_URL}/verify-signup-code`, {
                email: formData.memberEmail,
                token: inputCode,
            });

            console.log('Verification success:', response.data);

            alert('이메일 인증이 완료되었습니다!');
            navigate('/login'); // 로그인 페이지로 이동

        } catch (error) {
            console.error('Verification error:', error);

            if (axios.isAxiosError(error) && error.response) {
                alert(error.response.data.message || '인증 코드가 잘못되었습니다. 다시 시도해주세요.');
            } else {
                alert('알 수 없는 오류가 발생했습니다. 다시 시도해주세요.');
            }
        }
    };


    const handlePhoneChange = (value: string) => {
        // Remove any non-digit characters
        const cleaned = value.replace(/\D/g, '');

        let formatted: string;
        if (cleaned.length <= 3) {
            formatted = cleaned;
        } else if (cleaned.length <= 7) {
            formatted = `${cleaned.slice(0, 3)} ${cleaned.slice(3)}`;
        } else {
            formatted = `${cleaned.slice(0, 3)} ${cleaned.slice(3, 7)} ${cleaned.slice(7, 11)}`;
        }

        setFormData({...formData, memberPhone: formatted});
        validateInput('memberPhone', formatted);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = e.target;
        if (id === 'memberPhone') {
            handlePhoneChange(value);
        } else {
            setFormData({...formData, [id]: value});
            validateInput(id as keyof FormData, value);
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            handleNextStep();
        }
    };

    const isButtonDisabled = !!errors[fieldNames[step - 1]];

    return (
        <div className="min-h-screen max-w-2xl m-auto flex flex-col items-center lg: border">
            <div className="flex w-full h-16  justify-center items-center">
                <Logo/>
            </div>

            <div className="mt-5 w-full px-5">
                <p className="flex font-bold text-xl mb-10 text-center">
                    {['이름을 입력해 주세요', '휴대폰 번호를 입력해 주세요', '이메일을 입력해 주세요', '인증코드를 입력해주세요', '아이디를 입력해 주세요', '비밀번호를 입력해 주세요', '비밀번호 확인을 입력해 주세요', '생년월일을 입력해 주세요'][step - 1]}
                </p>
                <div className="flex flex-col gap-4">
                    {fieldNames.slice(0, step).map((field, index) => (
                        <div className="relative transition-all duration-500 ease-in-out mb-2" key={field}>
                            <label
                                className={`absolute left-0 transition-all duration-200 ${isFocused[field] || formData[field] ? '-top-6 text-sm text-gray-500' : 'top-3 text-lg text-gray-400 font-bold'}`}
                                htmlFor={field}
                            >
                                {getErrorMessage(field)}
                            </label>
                            <input
                                className={`w-full border-b pl-0 font-bold text-xl border-0 border-gray-300 outline-none focus:ring-0 focus:border-gray-300 mb-2 ${errors[field] ? 'border-red-500' : ''}`}
                                type={field.includes('memberPwd') || field.includes('confirmPassword') ? 'password' : 'text'}
                                id={field}
                                ref={(el) => (inputRefs.current[index] = el)}
                                value={formData[field]}
                                onFocus={() => {
                                    setIsFocused({...isFocused, [field]: true});
                                    inputRefs.current[index]?.scrollIntoView({behavior: 'smooth'});
                                }}
                                onBlur={() => setIsFocused({...isFocused, [field]: false})}
                                onChange={handleChange}
                                onKeyDown={handleKeyDown}
                            />
                            {errors[field] && <p className="text-red-500 text-sm">{errors[field]}</p>}
                        </div>
                    ))}
                </div>

                {isModalOpen && (
                    <div
                        className="absolute top-0 left-0 flex justify-center w-full h-full bg-black bg-opacity-50">
                        <div
                            className="relative flex flex-col justify-between w-[400px] h-[300px] top-[250px] bg-white rounded-lg shadow-lg p-6">
                            <div>
                                <h2 className="text-2xl font-bold text-center mb-4">이메일 인증</h2>
                                <span className="flex justify-center mb-6">코드 보내기를 눌러 이메일 인증을 진행해주세요</span>
                                <input
                                    type="text"
                                    value={inputCode}
                                    onChange={(e) => setInputCode(e.target.value)}
                                    placeholder="인증 코드 입력"
                                    className="w-full p-2 border rounded mb-4"
                                />
                            </div>

                            <div className="flex gap-4 justify-between mt-4">
                                <button
                                    onClick={handleSendEmailCode}
                                    disabled={isCooldown} // Disable button during cooldown
                                    className={`px-4 py-2 flex-1 ${
                                        isCooldown
                                            ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                            : 'bg-gray-200 text-black hover:bg-gray-300'
                                    } rounded transition`}
                                >
                                    {isCooldown ? `재발송 ${cooldownTime}초` : '코드 보내기'}
                                </button>
                                <button
                                    onClick={handleVerifyEmail}
                                    className="px-4 py-2 flex-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
                                >
                                    인증
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                <button
                    onClick={handleNextStep}
                    className={`w-full p-3 bg-blue-500 text-white font-bold rounded ${isButtonDisabled ? 'opacity-50 cursor-not-allowed' : ''}`}
                    disabled={isButtonDisabled}
                >
                    확인
                </button>
            </div>
        </div>
    );
};

export default Signup;
