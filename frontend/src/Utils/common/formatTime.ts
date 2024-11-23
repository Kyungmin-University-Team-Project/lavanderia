import { format, addHours} from 'date-fns';

export const formatTime = (createdDate: string): string => {
    try {
        const date = new Date(createdDate);

        // 기본 KST 변환 및 차이 계산
        const kstDate = addHours(date, 9); // UTC → KST 변환
        const now = new Date();
        const diffInSeconds = Math.floor((now.getTime() - kstDate.getTime()) / 1000);

        if (diffInSeconds < 60) return `${diffInSeconds}초 전`;
        if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}분 전`;
        if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}시간 전`;
        if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)}일 전`;
        return format(kstDate, "yyyy년 MM월 dd일");
    } catch (error) {
        console.error("날짜 포맷팅 오류:", error);
        return "날짜 오류";
    }
};
