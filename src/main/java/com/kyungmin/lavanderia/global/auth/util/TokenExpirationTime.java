package com.kyungmin.lavanderia.global.auth.util;

public class TokenExpirationTime {
    public static final Long ACCESS_TIME = 3600000L;   // 1시간 (1시간 = 60분 * 60초 * 1000밀리초)
    public static final Long REFRESH_TIME = 86400000L; // 1일 (1일 = 24시간 * 60분 * 60초 * 1000밀리초)

}
