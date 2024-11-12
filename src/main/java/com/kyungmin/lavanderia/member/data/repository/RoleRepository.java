package com.kyungmin.lavanderia.member.data.repository;

import com.kyungmin.lavanderia.member.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
