export interface Post {
    communityId: number; // 커뮤니티 ID
    createdDate: string; // 게시물 생성 날짜
    memberId: string; // 작성자 ID
    title: string; // 게시물 제목
    content: string; // 게시물 내용
    image: string; // 이미지 URL
    category: string; // 카테고리
    viewCount: number; // 조회수
    profileImg: string // 프로필 이미지
}

export interface Comment {
    commentId: number; // 댓글 고유 ID
    content: string; // 댓글 내용
    createdDate: string; // 댓글 생성 날짜
    memberId: string; // 작성자 ID
    profileImg: string; // 작성자 프로필 이미지
}
