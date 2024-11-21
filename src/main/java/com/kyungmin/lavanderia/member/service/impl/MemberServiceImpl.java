package com.kyungmin.lavanderia.member.service.impl;

import com.kyungmin.lavanderia.global.util.GoogleCloudUtils;
import com.kyungmin.lavanderia.email.service.EmailService;
import com.kyungmin.lavanderia.member.data.dto.MemberDTO;
import com.kyungmin.lavanderia.member.data.dto.MemberInfoDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.entity.Role;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import com.kyungmin.lavanderia.member.data.repository.RoleRepository;
import com.kyungmin.lavanderia.member.exception.DuplicateMemberIdEx;
import com.kyungmin.lavanderia.member.exception.DuplicatePhoneNumberEx;
import com.kyungmin.lavanderia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // 회원가입
    @Override
    public void signup(SignupDTO signupDto) {

        // 회원 정보 저장
        Member member = Member.builder()
                .memberId(signupDto.getMemberId())
                .memberPwd(passwordEncoder.encode(signupDto.getMemberPwd())) // 비밀번호 암호화
                .memberName(signupDto.getMemberName())
                .memberEmail(signupDto.getMemberEmail())
                .memberPhone(signupDto.getMemberPhone())
                .agreeMarketingYn(signupDto.getAgreeMarketingYn())
                .memberBirth(signupDto.getMemberBirth())
                .build();

        memberRepository.save(member);

        // 회원 권한 저장
        Role role = Role.builder().
                authorities("ROLE_USER")
                .memberId(member)
                .build();

        roleRepository.save(role);
    }

    // 회원 아이디 중복 체크
    @Override
    public void checkMemberId(String memberId) {
        boolean isExist = memberRepository.existsById(memberId);
        if (isExist) {
            throw new DuplicateMemberIdEx(memberId);
        }
    }

    // 회원 전화번호 중복 체크
    @Override
    public void checkPhoneNumber(String phoneNumber) {

        boolean isExist = memberRepository.existsByMemberPhone(phoneNumber);
        if (isExist) {
            throw new DuplicatePhoneNumberEx(phoneNumber);
        }
    }

    @Override
    public void checkEmail(String email) {
        boolean isExist = memberRepository.existsByMemberEmail(email);
        if (isExist) {
            throw new DuplicatePhoneNumberEx(email);
        }
    }

    // 회원정보 조회
    @Override
    public MemberInfoDTO memberInfo(Member member) {

        return MemberInfoDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberBirth(String.valueOf(member.getMemberBirth()))
                .memberLevel(member.getMemberLevel())
                .memberPoint(member.getMemberPoint())
                .agreeMarketingYn(member.getAgreeMarketingYn())
                .memberProfileImg(member.getMemberProfileImg())
                .build();
    }

    @Override
    public void memberDelete(Member member) {
        memberRepository.delete(member);
    }

    // 이메일 인증
    @Override
    public void checkSignupCode(String email, String token) {
        emailService.checkSignupCode(email, token);
    }

    @Override
    public void updateMemberPwd(Member member, MemberDTO.ChangePasswordDTO request) {
        try {
            Member memberEntity = memberRepository.findById(member.getMemberId()).orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
            if (!passwordEncoder.matches(request.getOldPassword(), memberEntity.getMemberPwd())) {
                throw new UsernameNotFoundException("비밀번호가 일치하지 않습니다.");
            }
            memberEntity.setMemberPwd(passwordEncoder.encode(request.getNewPassword()));
            memberRepository.save(memberEntity);
        } catch (Exception e) {
            throw new UsernameNotFoundException("비밀번호 변경 실패 : " + e.getMessage());
        }
    }

    // 프로필 이미지 업데이트
    @Override
    public void updateMemberProfile(Member member, MultipartFile profileImg) {
        try {
            Member memberEntity = memberRepository.findById(member.getMemberId()).orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
            // profileImg 있는 경우
            if (profileImg != null) {
                // 기본 이미지인 경우 클라우드에 이미지 추가
                if (memberEntity.getMemberProfileImg().equals("default")) {
                    // 이미지 업로드
                    GoogleCloudUtils.uploadImageById(memberEntity.getMemberId(), profileImg);
                    // 이미지 경로 저장
                    memberEntity.setMemberProfileImg(GoogleCloudUtils.getGOOGLE_IMAGE_CLOUD_URL() + member.getMemberId());
                    memberRepository.save(memberEntity);

                // 기존 이미지가 있는 경우 이미지 업데이트
                } else {
                    // 새로운 이미지로 업데이트
                    GoogleCloudUtils.updateImageById(memberEntity.getMemberId(), profileImg);
                }

            // profileImg == null 인 경우 ==> 기본 이미지로 설정
            }else {
                // 기존 이미지 삭제
                GoogleCloudUtils.deleteImageById(memberEntity.getMemberId());

                // 기본 이미지로 변경
                memberEntity.setMemberProfileImg("default");
                memberRepository.save(memberEntity);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("프로필 이미지 업데이트 실패 : " + e.getMessage());
        }
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByMemberEmail(email).orElse(null);
    }

    // 회원 아이디로 회원 조회
    Member findMemberByMemberId(String memberId) {
        Optional<Member> member = memberRepository.findMemberByMemberId(memberId);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException(memberId + "회원을 찾을 수 없습니다.");
        }
        return member.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberEntity = memberRepository.findById(username);

        if (memberEntity.isPresent()) {
            return memberEntity.get();
        }
        return null;
    }
}