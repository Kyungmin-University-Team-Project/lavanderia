package com.kyungmin.lavanderia.community.repository;

import com.kyungmin.lavanderia.community.data.entity.CommunityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommCommentRepository extends JpaRepository<CommunityComment, Long> {

    Page<CommunityComment> findByCommunityCommunityId(Long commentId, Pageable pageable);

}
