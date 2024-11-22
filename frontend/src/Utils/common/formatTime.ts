// 게시글 작성 시간계산 (한국 시간 기준)
export const formatTime = (createdDate: string): string => {
    const now = new Date();
    const created = new Date(new Date(createdDate).getTime() + 9 * 60 * 60 * 1000); // UTC -> KST 변환
    const diffInSeconds = Math.floor((now.getTime() - created.getTime()) / 1000); // 차이를 초 단위로 계산

    const diffInMinutes = Math.floor(diffInSeconds / 60);
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);
    const diffInMonths = Math.floor(diffInDays / 30);

    if (diffInSeconds < 60) return `${diffInSeconds}초 전`;
    if (diffInMinutes < 60) return `${diffInMinutes}분 전`;
    if (diffInHours < 24) return `${diffInHours}시간 전`;
    if (diffInDays < 30) return `${diffInDays}일 전`;
    return `${diffInMonths}달 전`;
};
