package com.kyungmin.lavanderia.member.service;

import com.kyungmin.lavanderia.member.data.dto.MemberInfoDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    void signup(SignupDTO signupDto);

    void checkMemberId(String memberId);

    void checkPhoneNumber(String phoneNumber);

    void checkEmail(String email);

    MemberInfoDTO memberInfo(String memberId);

    void memberDelete(String memberId);

    void checkSignupCode(String email, String token);
}
