package com.kyungmin.lavanderia.member.service;

import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    public void signup(SignupDTO signupDto);

    public void checkMemberId(String memberId);

    public void checkPhoneNumber(String phoneNumber);


}
