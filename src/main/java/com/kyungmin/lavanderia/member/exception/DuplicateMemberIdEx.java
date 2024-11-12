package com.kyungmin.lavanderia.member.exception;

public class DuplicateMemberIdEx extends RuntimeException{
    public DuplicateMemberIdEx() {
        super();
    }

    public DuplicateMemberIdEx(String message) {
        super("이미 가입된 아이디 입니다 : " + message);
    }

    public DuplicateMemberIdEx(String message, Throwable cause) {
        super("이미 가입된 아이디 입니다 : " + message, cause);
    }

    public DuplicateMemberIdEx(Throwable cause) {
        super(cause);
    }

    protected DuplicateMemberIdEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
