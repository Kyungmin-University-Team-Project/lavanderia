package com.kyungmin.lavanderia.member.exception;

public class EmailAuthenticationFailedEx extends RuntimeException{
    public EmailAuthenticationFailedEx() {
        super();
    }

    public EmailAuthenticationFailedEx(String message) {
        super("이메일 인증 실패 : " + message);
    }

    public EmailAuthenticationFailedEx(String message, Throwable cause) {
        super("이메일 인증 실패 : " + message, cause);
    }

    public EmailAuthenticationFailedEx(Throwable cause) {
        super(cause);
    }

    protected EmailAuthenticationFailedEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
