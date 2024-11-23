package com.kyungmin.lavanderia.lifeLaundry.data.repository;

import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeLaundryRepository extends JpaRepository<LifeLaundry, Long> {
}
