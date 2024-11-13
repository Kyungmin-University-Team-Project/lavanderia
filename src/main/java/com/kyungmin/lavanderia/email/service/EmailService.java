package com.kyungmin.lavanderia.email.service;

public interface EmailService {

    void sendSignupCode(String email);

    void checkSignupCode(String email, String token);

    void sendIdPwCode(String email, String type);

    void checkIdPwCode(String email, String token, String type);
}
