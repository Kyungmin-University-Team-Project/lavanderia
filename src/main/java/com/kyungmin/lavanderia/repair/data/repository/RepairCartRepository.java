package com.kyungmin.lavanderia.repair.data.repository;

import com.kyungmin.lavanderia.repair.data.entity.RepairCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairCartRepository extends JpaRepository<RepairCart, Long> {

    @Query("SELECT rc FROM RepairCart rc WHERE rc.member.memberId = :memberId")
    List<RepairCart> findRepairsByMemberId(@Param("memberId") String memberId);
}
