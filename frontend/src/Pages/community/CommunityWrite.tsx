import React, { useState, ChangeEvent, FormEvent } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { decryptToken } from '../../Utils/auth/crypto'
import {API_URL} from "../../Api/api";

const CommunityWrite = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [category, setCategory] = useState('');
  const [image, setImage] = useState<File | null>(null);
  const navigate = useNavigate();

  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setImage(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('category', category);
    if (image) {
      formData.append('image', image);
    }

    try {
      const encryptedAccess = localStorage.getItem('access');
      if (!encryptedAccess) {
        throw new Error('Access token not found');
      }

      const access = decryptToken(encryptedAccess);
      await axios.post(`${API_URL}/community/save`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          access: access,
        },
      });

      navigate('/community');
    } catch (error) {
      console.error('Error saving post:', error);
    }
  };

  return (
    <div className="max-w-2xl mx-auto h-full p-4">
      <h1 className="text-2xl font-bold mb-4">글 작성</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700">
            제목
          </label>
          <input
            type="text"
            id="title"
            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="content" className="block text-sm font-medium text-gray-700">
            내용
          </label>
          <textarea
            id="content"
            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          ></textarea>
        </div>
        <div>
          <label htmlFor="category" className="block text-sm font-medium text-gray-700">
            카테고리
          </label>
          <select
            id="category"
            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
          >
            <option value="">카테고리를 선택하세요</option>
            <option value="후기">후기</option>
            <option value="패션">패션</option>
          </select>
        </div>
        <div>
          <label htmlFor="image" className="block text-sm font-medium text-gray-700">
            이미지
          </label>
          <input
            type="file"
            id="image"
            className="mt-1 block w-full text-sm text-gray-500"
            accept="image/*"
            onChange={handleImageChange}
          />
        </div>
        <div>
          <button
            type="submit"
            className="w-full inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            저장
          </button>
        </div>
      </form>
    </div>
  );
};

export default CommunityWrite;
