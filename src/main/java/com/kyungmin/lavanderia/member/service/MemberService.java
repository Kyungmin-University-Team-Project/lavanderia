package com.kyungmin.lavanderia.member.service;

import com.kyungmin.lavanderia.member.data.dto.MemberDTO;
import com.kyungmin.lavanderia.member.data.dto.MemberInfoDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService extends UserDetailsService {

    void signup(SignupDTO signupDto);

    void checkMemberId(String memberId);

    void checkPhoneNumber(String phoneNumber);

    void checkEmail(String email);

    MemberInfoDTO memberInfo(Member member);

    void memberDelete(Member member);

    void checkSignupCode(String email, String token);

    void updateMemberPwd(Member member, MemberDTO.ChangePasswordDTO request);

    // 프로필 이미지 업데이트
    void updateMemberProfile(Member member, MultipartFile profileImg);

    // email로 회원 찾기
    Member findMemberByEmail(String email);
}
