package com.kyungmin.lavanderia.community.repository;

import com.kyungmin.lavanderia.community.data.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommCommentRepository extends JpaRepository<CommunityComment, Long> {
}
