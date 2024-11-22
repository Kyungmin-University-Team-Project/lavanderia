import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Post } from '../../Typings/community/post';
import { API_URL } from "../../Api/api";
import { ClipLoader } from "react-spinners";
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
  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태
  const [hasMore, setHasMore] = useState(true); // 더 로드할 데이터가 있는지 여부
  const footerRef = useRef<HTMLDivElement | null>(null); // footer ref
  const navigate = useNavigate();
  const lastScrollTop = useRef(0);

  const fetchPosts = async (page: number) => {
    if (isLoading || !hasMore) return; // 이미 로딩 중이거나 데이터가 없으면 중단
    setIsLoading(true);

    try {
      const response = await axios.get(`${API_URL}/community/`, {
        params: {
          page,
          size: 5, // 페이지당 게시글 수
          sort: 'createdAt,desc', // 내림차순 정렬
        },
      });

      const newPosts = response.data.content;

      setPosts(prevPosts => [...prevPosts, ...newPosts]); // 기존 데이터에 추가
      setHasMore(!response.data.last); // 마지막 페이지인지 확인
    } catch (error) {
      console.error('Error fetching posts:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts(currentPage); // 초기 데이터 로드
  }, [currentPage]);

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollTop = window.scrollY || document.documentElement.scrollTop;
      setIsScrollingUp(currentScrollTop < lastScrollTop.current);
      lastScrollTop.current = currentScrollTop <= 0 ? 0 : currentScrollTop;
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    const observer = new IntersectionObserver(
        entries => {
          if (entries[0].isIntersecting && hasMore) {
            setCurrentPage(prevPage => prevPage + 1); // 페이지 증가
          }
        },
        { threshold: 0.5 } // 절반 보일 때 트리거
    );

    if (footerRef.current) {
      observer.observe(footerRef.current);
    }

    return () => {
      if (footerRef.current) observer.unobserve(footerRef.current);
    };
  }, [footerRef, hasMore]);

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

        <div className="space-y-4 px-4">
          {posts.map(post => (
              <div
                  key={post.communityId}
                  className="bg-white w-full mt-3 rounded shadow-lg border-gray-300 cursor-pointer text-left"
                  onClick={() => handlePostClick(post)}
              >
                <div className="flex items-center p-4">
                  <div className="bg-gray-200 w-10 h-10 rounded-full"></div>
                  <div className="ml-3">
                    <div className="font-bold">{post.memberId}</div>
                    <div className="text-gray-500 text-sm">{post.category} - {new Date(post.createdAt).toLocaleDateString()}</div>
                  </div>
                </div>

                {post.image && (
                    <div className="mb-2">
                      <img src={post.image} alt={post.title} className="m-auto w-full h-auto" />
                    </div>
                )}

                <div className="px-4">
                  <ActionBar />
                  <div className="mb-2">{post.content}</div>
                </div>
              </div>
          ))}
        </div>

        <footer
            className="flex justify-center items-center w-full h-[200px]"
            ref={footerRef}
        >
          {isLoading && <ClipLoader size={40} color={"#6e6e6e"} />}
          {!hasMore && <div className="text-gray-500">더 이상 데이터가 없습니다.</div>}
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
