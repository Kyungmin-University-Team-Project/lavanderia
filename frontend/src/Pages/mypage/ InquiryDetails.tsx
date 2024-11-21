import React, { useEffect, useState } from 'react';
import Pagination from "react-js-pagination";
import axiosInstance from "../../Utils/axios/axiosInstance";
import "./Paging.css"

interface Page {
    page: number;
    size: number;
    sort: string;
}

const InquiryDetails = () => {
    const [listType, setListType] = useState<any[]>([]); // 실제 데이터 타입에 맞게 수정
    const [history, setHistory] = useState<Page>({
        page: 1,
        size: 10,
        sort: "",
    });

    useEffect(() => {
        const fetchList = async () => {
            try {
                const response = await axiosInstance.post('inquiry/list/my', history);
                console.log(response.data)
                // 서버 응답에서 실제 데이터 배열로 설정
                setListType(response.data);
            } catch (e) {
                console.log(e);
            }
        };
        fetchList();
    }, [history]);

    const handleChangePage = (page: number) => {
        setHistory((prev) => ({
            ...prev,
            page: page,
        }));
        console.log(history)
    };


    return (
        <div>
            {
                listType.map((item) => (
                    <ul>
                        <li className='text-sm border mb-2 mt-2 p-2 rounded'>
                            <p className='font-bold mb-2'>
                                {item.inquiryDate}
                            </p>
                            <p>
                                {item.inquiryTitle}
                            </p>
                            <p className='text-red-500 mt-2'>
                                {item.status}
                            </p>
                        </li>
                    </ul>
                ))
            }
            <Pagination
                activePage={history.page}
                itemsCountPerPage={5}
                totalItemsCount={10}
                pageRangeDisplayed={5}
                prevPageText={"<"}
                nextPageText={">"}
                onChange={handleChangePage}
            />
        </div>
    );
};

export default InquiryDetails;
