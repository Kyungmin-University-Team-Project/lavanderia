package com.kyungmin.lavanderia.laundry.repository;

import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryRepository extends JpaRepository<Laundry, Long> {

}
