export interface Comment {
  id: number; // 댓글 고유 ID
  author: string; // 작성자 이름
  content: string; // 댓글 내용
  avatar: string; // 작성자 프로필 이미지 URL
  createdDate: string; // 댓글 작성 시간 (ISO 포맷)
}

export interface Post {
  communityId: number;
  createdDate:string;
  memberId: string;
  title: string;
  content: string;
  image:string
  category: string;
  avatar: string
}
