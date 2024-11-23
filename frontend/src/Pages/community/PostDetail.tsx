import React, {useState, useEffect, useRef} from "react";
import {useLocation} from "react-router-dom";
import {Post, Comment} from "../../Typings/community/post";
import axiosInstance from "../../Utils/axios/axiosInstance";
import {formatTime} from "../../Utils/common/formatTime";
import {addHours} from "date-fns";

const emojiList = ["😊", "😂", "😍", "😎", "👍", "👏", "💖", "🙌", "🔥"];

const PostDetail: React.FC = () => {
    const location = useLocation();
    const post: Post = location.state.post;

    const [comments, setComments] = useState<Comment[]>([]);
    const [myComments, setMyComments] = useState<Comment[]>([]);
    const [commentText, setCommentText] = useState("");
    const [isEmojiPickerOpen, setIsEmojiPickerOpen] = useState(false);
    const [expandedComments, setExpandedComments] = useState<Set<number>>(new Set());
    const [currentPage, setCurrentPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);

    const footerRef = useRef<HTMLDivElement | null>(null);
    const currentUsername = localStorage.getItem("username");

    useEffect(() => {
        fetchComments(currentPage);
    }, [currentPage]);

    // 옵저버를 감지할 때마다 페이지 +1
    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting && hasMore && !isLoading) {
                    setCurrentPage((prevPage) => prevPage + 1);
                }
            },
            {threshold: 1.0}
        );

        if (footerRef.current) {
            observer.observe(footerRef.current);
        }

        return () => {
            if (footerRef.current) observer.unobserve(footerRef.current);
        };
    }, [footerRef, hasMore, isLoading]);

    const fetchComments = async (page: number) => {
        if (isLoading || !hasMore) return;
        setIsLoading(true);

        try {
            const response = await axiosInstance.get(`/community/post/${post.communityId}`, {
                params: {
                    page,
                    size: 10,
                    sort: "createdAt,desc",
                },
            });

            const newComments = response.data.content;

            const filteredComments = newComments.filter(
                (comment: Comment) => comment.memberId !== currentUsername
            );
            const myFilteredComments = newComments.filter(
                (comment: Comment) => comment.memberId === currentUsername
            );

            setComments((prevComments) => [...prevComments, ...filteredComments]);
            setMyComments((prevMyComments) => [...prevMyComments, ...myFilteredComments]);
            setHasMore(!response.data.last);
        } catch (error) {
            console.error("댓글 조회 실패:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleCommentSubmit = async () => {
        if (commentText.trim()) {
            try {
                const newCommentPayload = {
                    memberId: currentUsername,
                    content: commentText,
                    createdDate: new Date().toISOString(),
                };

                await axiosInstance.post(
                    `/community/post/${post.communityId}/comment`,
                    newCommentPayload
                );


                const kstNow = addHours(new Date(), -9);

                const newComment: Comment = {
                    commentId: Date.now(), // 임시 고유 ID
                    memberId: currentUsername || "unknown_user",
                    content: commentText,
                    createdDate: `${kstNow}`,
                    profileImg: "default", // 기본 프로필 이미지
                };

                // 클라이언트에서는 항상 "방금 전" 처리
                setMyComments((prevMyComments) => [
                    {...newComment},
                    ...prevMyComments,
                ]);


                setCommentText("");

                const commentContainer = document.querySelector(".scrollable-comment-container");
                if (commentContainer) {
                    commentContainer.scrollTop = 0;
                }
            } catch (error) {
                console.error("댓글 등록 실패:", error);
            }
        }
    };


    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter") {
            e.preventDefault();
            handleCommentSubmit();
        }
    };

    const toggleExpandComment = (commentId: number) => {
        setExpandedComments((prev) => {
            const newSet = new Set(prev);
            if (newSet.has(commentId)) {
                newSet.delete(commentId);
            } else {
                newSet.add(commentId);
            }
            return newSet;
        });
    };

    const handleEmojiClick = (emoji: string) => {
        setCommentText((prev) => prev + emoji);
        setIsEmojiPickerOpen(false);
    };

    return (
        <div className="max-w-2xl mx-auto">
            <div className="bg-white p-6">
                <div className="flex items-center mb-6">
                    <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200">
                        <img
                            src={post.profileImg === "default" ? "/img/user/primaryImage.webp" : post.profileImg}
                            alt={post.memberId}
                            className="w-full h-full object-cover"
                        />
                    </div>
                    <div className="ml-4">
                        <div className="font-bold text-lg">{post.memberId}</div>
                        <div className="text-gray-500 text-sm">{post.category}</div>
                    </div>
                </div>

                <div className="border-b">
                    {post.image && <img src={post.image} alt={post.title} className="w-full h-auto rounded-lg mb-4"/>}
                    <div className="mb-6 text-gray-700">{post.content}</div>
                </div>

                <div className="mt-6">
                    <h3 className="font-bold text-lg mb-4">댓글</h3>
                    <div
                        className="scrollable-comment-container space-y-4 border-gray-200 rounded-lg max-h-[300px] overflow-y-auto">
                        {myComments.concat(comments).length > 0 ? (
                            myComments.concat(comments).map((comment, index) => (
                                <div key={`${comment.commentId}-${index}`} className="flex items-start">
                                    <div className="w-14 h-14 rounded-full overflow-hidden bg-gray-200">
                                        <img
                                            src={
                                                !comment.profileImg || comment.profileImg === "default"
                                                    ? "/img/user/primaryImage.webp"
                                                    : comment.profileImg
                                            }
                                            alt={comment.memberId}
                                            className="w-full h-full object-cover"
                                        />
                                    </div>
                                    <div className="ml-3 flex-1">
                                        <div className="font-bold text-sm">
                                            {comment.memberId}
                                            {comment.memberId === currentUsername && " (내 댓글)"}
                                        </div>
                                        <div
                                            className={`text-gray-700 ${expandedComments.has(comment.commentId) ? "" : "line-clamp-3"}`}>
                                            {comment.content}
                                        </div>
                                        {comment.content.length > 100 && (
                                            <button
                                                className="text-blue-500 text-sm mt-1"
                                                onClick={() => toggleExpandComment(comment.commentId)}
                                            >
                                                {expandedComments.has(comment.commentId) ? "간략히" : "더보기"}
                                            </button>
                                        )}
                                        <div className="text-gray-400 text-xs mt-2">
                                            {comment.createdDate ? formatTime(comment.createdDate) : "방금 전"}
                                        </div>
                                    </div>
                                    {index === myComments.concat(comments).length - 1 && <div ref={footerRef}/>}
                                </div>
                            ))
                        ) : (
                            <div className="text-gray-500 text-center py-4">아직 댓글이 없습니다. 첫 댓글을 작성해 보세요!</div>
                        )}
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
                        <button className="text-blue-500 font-semibold disabled:text-gray-400"
                                disabled={!commentText.trim()} onClick={handleCommentSubmit}>
                            게시
                        </button>
                    </div>

                    {isEmojiPickerOpen && (
                        <div
                            className="absolute bottom-full mb-2 left-0 bg-white border rounded-lg shadow-lg p-3 grid grid-cols-5 gap-2 z-10">
                            {emojiList.map((emoji) => (
                                <button key={emoji} className="text-lg hover:bg-gray-100 rounded"
                                        onClick={() => handleEmojiClick(emoji)}>
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
