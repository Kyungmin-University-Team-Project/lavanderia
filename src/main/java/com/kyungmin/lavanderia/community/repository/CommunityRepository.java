package com.kyungmin.lavanderia.community.repository;

import com.kyungmin.lavanderia.community.data.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
