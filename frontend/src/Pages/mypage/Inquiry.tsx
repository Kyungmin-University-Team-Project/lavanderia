import React, { useState} from 'react';
import axiosInstance from "../../Utils/axios/axiosInstance";

interface Insert {
    imageList: (File | null)[];  // Array of File or null
    inquiryRequest: {
        inquiryTitle: string;
        inquiryContent: string;
    };
}
const MAX_TITLE_LENGTH = 25;
const MAX_LENGTH = 500;

const Inquiry = () => {
    const [insert, setInsert] = useState<Insert>({
        imageList: [],
        inquiryRequest: {
            inquiryTitle: '',
            inquiryContent: '',
        }
    });

    const handleOnFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { type, files } = e.target;
        if (type === 'file' && files) {
            const fileArray: (File | null)[] = [];
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                if (file.size > 10 * 1024 * 1024) {
                    alert("파일 크기는 10MB 이하로 업로드해야 합니다.");
                    return;
                }
                if (!["image/jpeg", "image/png", "image/jpg"].includes(file.type)) {
                    alert("허용되지 않는 파일 형식입니다. jpg, jpeg, png 파일만 업로드 가능합니다.");
                    return;
                }
                fileArray.push(file);
            }
            setInsert((prev) => ({
                ...prev,
                imageList: fileArray,
            }));
        }
    };

    const handleOnChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        // 글자 수 제한을 설정해 [name]: name === 'inquiryTitle'
        // 원래는 [name]: value을 사용해야함
            setInsert((prev) => ({
                ...prev,
                inquiryRequest: {
                    ...prev.inquiryRequest,
                    [name]: name === 'inquiryTitle'
                        ? value.slice(0, MAX_TITLE_LENGTH)
                        : value.slice(0, MAX_LENGTH),
                }
            }));
    };

    const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData();

        formData.append('inquiryTitle', insert.inquiryRequest.inquiryTitle);
        formData.append('inquiryContent', insert.inquiryRequest.inquiryContent);

        insert.imageList.forEach(file => {
            if (file) {
                formData.append('imageList', file);
            }
        });
        try {
            const response = await axiosInstance.post('/inquiry/insert', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <form action="" className='p-2' onSubmit={handleOnSubmit}>
                <label htmlFor="inquiryTitle">
                    <p className='text-sm font-bold mb-2'>제목</p>
                    <input
                        className='w-full outline-0 rounded hover:bg border h-6 mb-2'
                        value={insert.inquiryRequest.inquiryTitle}
                        name='inquiryTitle'
                        maxLength={MAX_TITLE_LENGTH}
                        onChange={handleOnChange}
                    />
                </label>
                <p className='mb-2 text-sm font-bold'>{insert.inquiryRequest.inquiryTitle.length}/{MAX_TITLE_LENGTH}</p>

                <p className='mb-2 text-sm font-bold'>내용</p>
                <label htmlFor="inquiryContent">
                    <textarea
                        className='w-full outline-0 rounded border mb-2'
                        name='inquiryContent'
                        maxLength={MAX_LENGTH - 1}
                        onChange={handleOnChange}
                        value={insert.inquiryRequest.inquiryContent}
                    />
                </label>
                <p className='mb-2 text-sm font-semibold'>{insert.inquiryRequest.inquiryContent.length}/{MAX_LENGTH}</p>
                <p className='mb-2 text-sm'>
                    - 문의 내용에 욕설, 인격 침해, 성희롱 등 수치심을 유발하는 <br/>
                    - 표현 또는 이미지(첨부 파일)가 포함되어 있을 경우 운영 정책에 <br/>
                    - 따라 계정 이용이 제한되거나 상담이 중단될 수 있습니다.
                </p>
                <ul className='mb-2 text-sm'>
                    <li>
                       - 옷 파손 및 훼손 문의는 관련있는 파일을 첨부해 주세요.
                    </li>
                    <li>
                       - 최대 10MB 크기의 파일(jpg, jpeg, png)을 첨부할 수 있습니다.
                    </li>
                    <li>
                       - 영상은 영상을 확인할 수 있는 URL을 본문에 기재해 주세요.
                    </li>
                </ul>

                <p className='font-bold mb-2 text-sm'>파일 첨부</p>
                <label htmlFor="fileInput" className="text-xs flex">
                    <input
                        type="file"
                        id="fileInput"
                        multiple
                        onChange={handleOnFileChange}
                    />
                </label>

                <div>
                    <h3 className='mb-2 mt-2 text-sm'>첨부된 파일들:</h3>
                    <ul className='mb-2 text-sm'>
                        {insert.imageList.map((file, index) => (
                            <li key={index}>
                                {file ? file.name : '파일 없음'}
                            </li>
                        ))}
                    </ul>
                </div>

                <button className='w-full mt-2 bg-black text-white
                outline-0 rounded p-2 ' type="submit">제출</button>
            </form>
        </div>
    );
};

export default Inquiry;
