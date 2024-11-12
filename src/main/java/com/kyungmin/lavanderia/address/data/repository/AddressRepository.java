package com.kyungmin.lavanderia.address.data.repository;

import com.kyungmin.lavanderia.address.data.entity.Address;
import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByMemberIdAndAddressId(Member memberId, int addressId);

    Optional<Address> findByMemberIdAndAddressDefaultYn(Member memberId, char addressDefaultYn);

    List<Address> findAllByMemberId(Member memberId);

    boolean existsByMemberId(Member memberId);

    Address findByMemberId(Member memberId);
}
