package com.kyungmin.lavanderia.global.util.email.service.impl;

import com.kyungmin.lavanderia.global.util.email.service.EmailService;
import com.kyungmin.lavanderia.member.exception.EmailAuthenticationFailedEx;
import com.kyungmin.lavanderia.member.exception.EmailSendFailedEx;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender javaMailSender;


    @Override
    public void sendSignupCode(String email) {

        String token = UUID.randomUUID().toString();
        String subject = "Lavanderia 회원가입 이메일 인증";
        String link = "http://localhost:8080/verify-signup-code?email=" + email + "&token=" + token;
        String text = loadEmailTemplate(link);

        System.out.println("email : " + email);
        System.out.println("token : " + token);

        String redisKey = "Email Authentication Code, Member Email : " + email;
        redisTemplate.opsForValue().set(redisKey, token, 5, TimeUnit.MINUTES);

        sendEmailUtil(email, subject, text);
    }

    @Override
    public void checkSignupCode(String email, String token) {

        System.out.println("email : " + email);
        System.out.println("token : " + token);

        String redisKey = "Email Authentication Code, Member Email : " + email;
        String getToken = (String) redisTemplate.opsForValue().get(redisKey);

        System.out.println("getToken : " + getToken);

        if (getToken != null && getToken.equals(token)) {
            redisTemplate.delete(redisKey);
        } else {
            throw new EmailAuthenticationFailedEx(email);
        }
    }

    public void sendEmailUtil(String email, String subject, String text){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(text, true); // 메일 본문 내용
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendFailedEx(email);
        }
    }

    private String loadEmailTemplate(String link) {
        try {
            ClassPathResource resource = new ClassPathResource("/static/verification-email.html");
            String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return String.format(template, link);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}
