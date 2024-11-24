package com.kyungmin.lavanderia.cart.repository;

import com.kyungmin.lavanderia.cart.data.entity.LaundryCart;
import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaundryCartRepository extends JpaRepository<LaundryCart, Long> {
    List<LaundryCart> findAllByMember(Member member);

    void deleteAllByLaundry(List<Laundry> laundryList);
}
