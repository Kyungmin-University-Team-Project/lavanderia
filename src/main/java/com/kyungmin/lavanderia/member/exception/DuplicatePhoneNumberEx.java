package com.kyungmin.lavanderia.member.exception;

public class DuplicatePhoneNumberEx extends RuntimeException{

    public DuplicatePhoneNumberEx() {
        super();
    }

    public DuplicatePhoneNumberEx(String message) {
        super(message + " 이 번호는 이미 존재하는 번호입니다. ");
    }

    public DuplicatePhoneNumberEx(String message, Throwable cause) {
        super(message + " 이 번호는 이미 존재하는 번호입니다. ", cause);
    }

    public DuplicatePhoneNumberEx(Throwable cause) {
        super(cause);
    }

    protected DuplicatePhoneNumberEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
