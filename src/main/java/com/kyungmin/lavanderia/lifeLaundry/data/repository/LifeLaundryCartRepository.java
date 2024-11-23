package com.kyungmin.lavanderia.lifeLaundry.data.repository;

import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundryCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeLaundryCartRepository extends JpaRepository<LifeLaundryCart, Long> {

}
