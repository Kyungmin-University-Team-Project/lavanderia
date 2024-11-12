import React from 'react'
import { useLocation } from 'react-router-dom'
import { Post, Comment } from '../../Typings/community/post'
import BackButton from "../../Components/common/BackButton";

// 임시 댓글 데이터
const commentsData: { [key: number]: Comment[] } = {
  1: [
    { id: 1, author: 'User1', content: '좋은 글이네요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User2', content: '잘 봤습니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  2: [
    { id: 1, author: 'User3', content: '정말 유익했어요.', avatar: 'https://via.placeholder.com/40' }
  ],
  3: [
    { id: 1, author: 'User4', content: '재밌게 읽었어요.', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User5', content: '감사합니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 3, author: 'User6', content: '잘 봤습니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  4: [
    { id: 1, author: 'User7', content: '좋아요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User8', content: '유익한 정보 감사합니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 3, author: 'User9', content: '잘 봤어요.', avatar: 'https://via.placeholder.com/40' },
    { id: 4, author: 'User10', content: '좋은 글 감사합니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  5: [
    { id: 1, author: 'User11', content: '정말 멋진 글이에요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User12', content: '감사합니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  6: [
    { id: 1, author: 'User13', content: '맛있어 보여요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User14', content: '좋은 정보 감사합니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 3, author: 'User15', content: '잘 읽었어요.', avatar: 'https://via.placeholder.com/40' },
    { id: 4, author: 'User16', content: '유익해요.', avatar: 'https://via.placeholder.com/40' },
    { id: 5, author: 'User17', content: '감사해요.', avatar: 'https://via.placeholder.com/40' }
  ],
  7: [
    { id: 1, author: 'User18', content: '추천합니다!', avatar: 'https://via.placeholder.com/40' }
  ],
  8: [
    { id: 1, author: 'User19', content: '좋아요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User20', content: '유익한 정보 감사합니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  9: [
    { id: 1, author: 'User21', content: '정말 멋진 글이에요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User22', content: '감사합니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 3, author: 'User23', content: '잘 봤습니다.', avatar: 'https://via.placeholder.com/40' }
  ],
  10: [
    { id: 1, author: 'User24', content: '좋은 글이네요!', avatar: 'https://via.placeholder.com/40' },
    { id: 2, author: 'User25', content: '잘 봤습니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 3, author: 'User26', content: '감사합니다.', avatar: 'https://via.placeholder.com/40' },
    { id: 4, author: 'User27', content: '좋아요!', avatar: 'https://via.placeholder.com/40' }
  ]
}

const PostDetail: React.FC = () => {
  const location = useLocation()
  const post: Post = location.state.post
  const comments: Comment[] = commentsData[post.communityId] || []

  if (!post) {
    return <div>Post not found</div>
  }

  return (
    <div className="max-w-2xl mx-auto h-full">
      <div className="bg-white p-6">
        <BackButton/>
        <div className="flex items-center mb-4">
          <div className="bg-gray-200 w-12 h-12 rounded-full overflow-hidden">
            <img src={post.avatar} alt={post.memberId} className="w-full h-full object-cover" />
          </div>
          <div className="ml-3">
            <div className="font-bold text-lg">{post.memberId}</div>
            <div className="text-gray-500 text-sm">{post.category}</div>
          </div>
        </div>
        <div className="mb-4">
          <div className="mb-2 font-bold text-2xl">{post.title}</div>
          <img src={post.image} alt={post.title} className="w-full h-auto rounded mb-4" />
          <div className="mb-2">{post.content}</div>
          <div className="text-gray-500 text-sm">조회수: {post.viewCount}</div>
        </div>

        {/* 댓글 섹션 */}
        <div className="bg-gray-100 p-4 rounded mt-4">
          <h2 className="font-bold mb-4">댓글</h2>
          {comments.map(comment => (
            <div key={comment.id} className="flex items-start mb-4">
              <div className="w-10 h-10 rounded-full overflow-hidden">
                <img src={comment.avatar} alt={comment.author} className="w-full h-full object-cover" />
              </div>
              <div className="ml-3">
                <div className="font-bold">{comment.author}</div>
                <div>{comment.content}</div>
              </div>
            </div>
          ))}
        </div>

        {/* 댓글 작성하기 */}
        <div className="mt-6">
          <h3 className="font-bold mb-2">댓글을 작성해주세요</h3>
          <textarea
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows={4}
            placeholder="댓글을 작성하세요..."
          />
          <button className="mt-2 bg-blue-500 text-white px-4 py-2 rounded-lg">댓글 작성</button>
        </div>
      </div>
    </div>
  )
}

export default PostDetail
