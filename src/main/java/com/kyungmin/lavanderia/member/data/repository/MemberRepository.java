package com.kyungmin.lavanderia.member.data.repository;

import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    Optional<Member> findByMemberPhone(String phoneNumber);

    Boolean existsByMemberPhone(String phoneNumber);

    Boolean existsByMemberEmail(String email);

    Optional<Member> findMemberByMemberId(String memberId);

    Optional<Member> findMemberByMemberEmail(String email);
};