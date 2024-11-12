import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Post } from '../../Typings/community/post';
import { decryptToken } from '../../Utils/auth/crypto';
import {API_URL} from "../../Api/api";

const tabs = [
  { name: '전체', active: true },
  { name: '후기', active: false },
  { name: '패션', active: false },
  { name: '내게시물', active: false }
];

const Community = () => {
  const [activeTab, setActiveTab] = useState('전체');
  const [posts, setPosts] = useState<Post[]>([]);
  const [isScrollingUp, setIsScrollingUp] = useState(false);
  const navigate = useNavigate();
  let lastScrollTop = 0;

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get(`${API_URL}/community/`);

        setPosts(response.data);
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchPosts();
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollTop = window.scrollY || document.documentElement.scrollTop;
      if (currentScrollTop > lastScrollTop) {
        // Scrolling Down
        setIsScrollingUp(false);
      } else {
        // Scrolling Up
        setIsScrollingUp(true);
      }
      lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop; // For Mobile or negative scrolling
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handlePostClick = (post: Post) => {
    navigate(`/community/${post.communityId}`, { state: { post } });
  };

  const handleWritePost = () => {
    navigate('/community/write'); // Assuming you have a route for writing posts
  };

  return (
    <div className="max-w-2xl mx-auto h-full">

      {/* Header */}

      <header className="w-full px-4 sticky top-[116px] z-10  backdrop-blur-sm">
        <div className="flex gap-4 p-3">
          {tabs.map(tab => (
            <button
              key={tab.name}
              className={`px-4 py-2 rounded-full ${activeTab === tab.name ? 'bg-black text-white' : 'bg-gray-200 text-gray-600'}`}
              onClick={() => setActiveTab(tab.name)}
            >
              {tab.name}
            </button>
          ))}
        </div>
      </header>

      {/* Posts */}

      <div className="space-y-4 px-4">
        {posts.map(post => (
          <button
            key={post.communityId}
            className="bg-white p-4 w-full mt-3 rounded shadow-lg hover:bg-gray-100 cursor-pointer text-left"
            onClick={() => handlePostClick(post)}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                handlePostClick(post);
              }
            }}
          >
            <div className="flex items-center mb-2">
              <div className="bg-gray-200 w-10 h-10 rounded-full"></div>
              <div className="ml-3">
                <div className="font-bold">{post.memberId}</div>
                <div className="text-gray-500 text-sm">{post.category} - {new Date(post.createdAt).toLocaleDateString()}</div>
              </div>
            </div>
            <div className="mb-2 font-bold text-xl">{post.title}</div>
            {post.image && (
              <div className="mb-2">
                <img src={post.image} alt={post.title} className="m-auto w-full h-auto rounded" />
              </div>
            )}
            <div className="mb-2">{post.content}</div>
            <div className="text-gray-500 text-sm">조회수: {post.viewCount}</div>
          </button>
        ))}
      </div>

      <footer className="flex justify-center items-center w-full h-20 bg-red-400">
        <span className="text-3xl">감지시 피드 불러오기</span>
      </footer>

      <button
        className={`fixed bottom-0 left-1/2 transform -translate-x-1/2 bg-black text-white rounded-full p-4 shadow-lg hover:bg-gray-600 transition-transform ${
          isScrollingUp ? '-translate-y-10' : 'translate-y-full'
        }`}
        onClick={handleWritePost}
        style={{ transition: 'transform 0.3s ease-in-out' }}
      >
        글 작성
      </button>
    </div>
  );
};

export default Community;
