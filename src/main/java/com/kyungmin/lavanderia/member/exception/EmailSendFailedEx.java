package com.kyungmin.lavanderia.member.exception;

public class EmailSendFailedEx extends RuntimeException{
    public EmailSendFailedEx() {
        super();
    }

    public EmailSendFailedEx(String message) {
        super("이메일 전송 실패" + message);
    }

    public EmailSendFailedEx(String message, Throwable cause) {
        super("이메일 전송 실패" + message, cause);
    }

    public EmailSendFailedEx(Throwable cause) {
        super(cause);
    }

    protected EmailSendFailedEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
