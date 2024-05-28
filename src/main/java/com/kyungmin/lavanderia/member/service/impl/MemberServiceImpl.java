package com.kyungmin.lavanderia.member.service.impl;

import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.data.entity.AddressEntity;
import com.kyungmin.lavanderia.member.data.entity.MemberEntity;
import com.kyungmin.lavanderia.member.data.repository.AddressRepository;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import com.kyungmin.lavanderia.member.exception.DuplicateMemberIdEx;
import com.kyungmin.lavanderia.member.exception.DuplicatePhoneNumberEx;
import com.kyungmin.lavanderia.member.exception.EmailAuthenticationFailedEx;
import com.kyungmin.lavanderia.member.exception.EmailSendFailedEx;
import com.kyungmin.lavanderia.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    @Override
    public void signup(SignupDTO signupDto) {

        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(signupDto.getMemberId())
                .memberPwd(passwordEncoder.encode(signupDto.getMemberPwd()))
                .memberName(signupDto.getMemberName())
                .memberEmail(signupDto.getMemberEmail())
                .memberPhone(signupDto.getMemberPhone())
                .memberBirthday(signupDto.getMemberBirth())
                .agreeMarketingYn(signupDto.getAgreeMarketingYn())
                .build();

        AddressEntity addressEntity = AddressEntity.builder()
                .memberId(signupDto.getMemberId())
                .address(signupDto.getAddress())
                .detailAddress(signupDto.getDetailAddress())
                .receiver(signupDto.getMemberId())
                .phone(signupDto.getMemberPhone())
                .build();

        memberRepository.save(memberEntity);
        addressRepository.save(addressEntity);
    }

    @Override
    public void checkMemberId(String memberId) {

        boolean isExist = memberRepository.existsById(memberId);

        if (isExist) {
            throw new DuplicateMemberIdEx(memberId);
        }
    }

    @Override
    public void checkPhoneNumber(String phoneNumber) {

        boolean isExist = memberRepository.existsByMemberPhone(phoneNumber);

        if (isExist) {
            throw new DuplicatePhoneNumberEx(phoneNumber);
        }
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> memberEntity = memberRepository.findById(username);

        if (memberEntity.isPresent()) {
            return memberEntity.get();
        }
        return null;
    }
}
