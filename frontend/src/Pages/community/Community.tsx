import axios from 'axios';
import React, {useState, useEffect, useRef} from 'react';
import { useNavigate } from 'react-router-dom';
import { Post } from '../../Typings/community/post';
import { decryptToken } from '../../Utils/auth/crypto';
import {API_URL} from "../../Api/api";
import {ClipLoader} from "react-spinners";
import ActionBar from "./ActionBar";

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
  const lastScrollTop = useRef(0); //

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get(`${API_URL}/community/`);

        
        // TODO: 게시 시간 안보내짐
        console.log(response.data)

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
      if (currentScrollTop > lastScrollTop.current) {
        setIsScrollingUp(false); // 스크롤 다운
      } else {
        setIsScrollingUp(true); // 스크롤 업
      }
      lastScrollTop.current = currentScrollTop <= 0 ? 0 : currentScrollTop;
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handlePostClick = (post: Post) => {
    navigate(`/community/${post.communityId}`, { state: { post } });
  };

  const handleWritePost = () => {
    navigate('/community/write');
  };

  return (
    <div className="max-w-2xl mx-auto h-full">
      <header className="w-full px-4 sticky top-[50px] z-10 backdrop-blur-sm">
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

      {/* TODO: 각 포스트에 대한 슬라이드 기능 추가*/}

      <div className="space-y-4 px-4">
        {posts.map(post => (
          <button
            key={post.communityId}
            className="bg-white w-full mt-3 rounded shadow-lg border-gray-300 cursor-pointer text-left"
            onClick={() => handlePostClick(post)}>
            <div className="flex items-center p-4">
              <div className="bg-gray-200 w-10 h-10 rounded-full"></div>
              <div className="ml-3">
                <div className="font-bold">{post.memberId}</div>
                <div className="text-gray-500 text-sm">{post.category} - {new Date(post.createdAt).toLocaleDateString()}</div>
              </div>
            </div>
            
            {/* TODO: 이미지 여기서 이미지가 여러장일 경우 슬라이드로 보여지도록 수정*/}
            {post.image && (
              <div className="mb-2">
                <img src={post.image} alt={post.title} className="m-auto w-full h-auto" />
              </div>
            )}


            <div className="px-4">
              {/*이부분에 ActionBar*/}
              <ActionBar/>

              {/*게시글 본문 본문은 1줄정도만 작게 보여주고
                나머지는 상세페이지에서 확인하도록*/}
              <div className="mb-2">{post.content}</div>
            </div>
          </button>
        ))}
      </div>

      {/*TODO: 인피니티스크롤 구현하기*/}
      <footer className="flex justify-center items-center w-full h-[200px]">
        <ClipLoader size={40} color={"#8f8f8f"}/>
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
