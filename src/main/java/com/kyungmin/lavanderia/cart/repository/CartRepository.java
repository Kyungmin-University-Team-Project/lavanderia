package com.kyungmin.lavanderia.cart.repository;

import com.kyungmin.lavanderia.cart.data.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
