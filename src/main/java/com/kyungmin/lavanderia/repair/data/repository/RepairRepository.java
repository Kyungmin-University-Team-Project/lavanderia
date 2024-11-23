package com.kyungmin.lavanderia.repair.data.repository;

import com.kyungmin.lavanderia.repair.data.entity.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
}
