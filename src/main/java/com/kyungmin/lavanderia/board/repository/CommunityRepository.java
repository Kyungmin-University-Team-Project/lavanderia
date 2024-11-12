package com.kyungmin.lavanderia.board.repository;

import com.kyungmin.lavanderia.board.data.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
