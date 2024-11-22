import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { Post, Comment } from '../../Typings/community/post';
import axiosInstance from "../../Utils/axios/axiosInstance";
import ActionBar from "./ActionBar";
import {formatTime} from "../../Utils/common/formatTime";

// TODO: 댓글 조회 기능 추가하기
const commentsData: { [key: number]: Comment[] } = {
  8: [
    {
      id: 1,
      author: 'User19',
      content: '좋아요!',
      avatar: 'https://via.placeholder.com/40',
      createdDate: '2024-11-22T12:46:59.885Z',
    },
    {
      id: 2,
      author: 'User20',
      content: '유익한 정보 감사합니다.',
      avatar: 'https://via.placeholder.com/40',
      createdDate: '2024-11-22T12:50:00.000Z',
    },
  ],
};


const emojiList = ['😊', '😂', '😍', '😎', '👍', '👏', '💖', '🙌', '🔥'];

const PostDetail: React.FC = () => {
  const location = useLocation();
  const post: Post = location.state.post;
  const comments: Comment[] = commentsData[post.communityId] || [];

  const [commentText, setCommentText] = useState('');
  const [isEmojiPickerOpen, setIsEmojiPickerOpen] = useState(false);

  const handleEmojiClick = (emoji: string) => {
    setCommentText((prev) => prev + emoji);
    setIsEmojiPickerOpen(false);
  };

  const handleCommentSubmit = async () => {
    if (commentText.trim()) {
      try {
        await axiosInstance.post(`/community/post/${post.communityId}/comment`, {
          memberId: 'currentUserId', // 실제 사용자 ID
          content: commentText,
          createdDate: new Date().toISOString(),
        });
        setCommentText('');
      } catch (error) {
        console.error('댓글 등록 실패:', error);
      }
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleCommentSubmit();
    }
  };

  return (
      <div className="max-w-2xl mx-auto">
        <div className="bg-white p-6">
          <div className="flex items-center mb-6">
            <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
              <img src={post.avatar || 'https://via.placeholder.com/40'} alt={post.memberId} className="w-full h-full object-cover" />
            </div>
            <div className="ml-4">
              <div className="font-bold text-lg">{post.memberId}</div>
              <div className="text-gray-500 text-sm">{post.category}</div>
            </div>
          </div>

          <div className="mb-4">
            {post.image && <img src={post.image} alt={post.title} className="w-full h-auto rounded-lg mb-4" />}
            <div className="text-gray-700">{post.content}</div>
          </div>

          <ActionBar />

          <div className="mt-6">
            <h3 className="font-bold text-lg mb-4">댓글</h3>
            <div className="space-y-4">
              {comments.map((comment) => (
                  <div key={comment.id} className="flex items-start">
                    <div className="w-10 h-10 rounded-full overflow-hidden bg-gray-200">
                      <img src={comment.avatar} alt={comment.author} className="w-full h-full object-cover" />
                    </div>
                    <div className="ml-3">
                      <div className="font-bold text-sm">{comment.author}</div>
                      <div className="text-gray-700">{comment.content}</div>
                      <div className="text-gray-400 text-xs">{formatTime(comment.createdDate)}</div>
                    </div>
                  </div>
              ))}
            </div>
          </div>

          <div className="mt-6 border-t pt-4 relative">
            <div className="flex items-center space-x-3">
              <button className="text-gray-500" onClick={() => setIsEmojiPickerOpen((prev) => !prev)}>
                😊
              </button>
              <input
                  type="text"
                  className="flex-1 border-none border-transparent focus:border-transparent focus:ring-0"
                  placeholder="댓글 달기..."
                  value={commentText}
                  onChange={(e) => setCommentText(e.target.value)}
                  onKeyDown={handleKeyDown}
              />
              <button
                  className={`text-blue-500 font-semibold disabled:text-gray-400`}
                  disabled={!commentText.trim()}
                  onClick={handleCommentSubmit}
              >
                게시
              </button>
            </div>

            {isEmojiPickerOpen && (
                <div className="absolute bottom-full mb-2 left-0 bg-white border rounded-lg shadow-lg p-3 grid grid-cols-5 gap-2 z-10">
                  {emojiList.map((emoji) => (
                      <button
                          key={emoji}
                          className="text-lg hover:bg-gray-100 rounded"
                          onClick={() => handleEmojiClick(emoji)}
                      >
                        {emoji}
                      </button>
                  ))}
                </div>
            )}
          </div>
        </div>
      </div>
  );
};

export default PostDetail;
