package com.kyungmin.lavanderia.global.util.email.service;

public interface EmailService {

    public void sendSignupCode(String email);

    public void checkSignupCode(String email, String token);

}
