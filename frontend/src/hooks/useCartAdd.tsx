import  { useCallback, useEffect, useState } from 'react'
import axiosInstance from '../Utils/axios/axiosInstance'

interface Repair {
  id: number;
  clothesType: string;
  howTo: string;
  detailInfo: string;
  request: string;
  repairCartId: string
  price: number;
  [Key: string]: any;
}

interface Life {
  lifeLaundryCartId: number
  lifeLaundryId: number
  memberId: string
  type: string
  weight: number
  price: number
  [Key: string]: any;
}

type CartItem = Repair | Life;

const UseCartAdd=<T extends CartItem> (featUrl: string, deleteUrl: string, item: 'repairCartId'|'lifeLaundryCartId') => {
    const [data, setData] = useState<T[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<Error | null>(null);

  const fetchData = useCallback(async () => {
    try {
      const response = await axiosInstance.post<T[]>(featUrl);
      setData(response.data);
    } catch (e) {
      setError(e instanceof Error ? e : new Error('An error occurred'));
    } finally {
      setLoading(false);
    }
  }, [featUrl]);


  useEffect(() => {
    fetchData();
  }, [fetchData, loading])


  // 삭제 로직
  const handleRemove = async (id: number) => {
    const itemToRemove = data.find((item) => item.id === id);
    if (!itemToRemove) {
      console.error('아이템 없다 마');
      return;
    }
    try {
      const response = await axiosInstance.post<T>(deleteUrl, { [item]: itemToRemove[item] });
      console.log('Response:', response.data);
      setLoading((prev) => !prev)
      setData((prevData) => prevData.filter((item) => item.id !== id));
    } catch (error) {
      console.error('Error deleting item:', error);
    }
  };

  return {data, loading,error, handleRemove, fetchData}
}

export default UseCartAdd