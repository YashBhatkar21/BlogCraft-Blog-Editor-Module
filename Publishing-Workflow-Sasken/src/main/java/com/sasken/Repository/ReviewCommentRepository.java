package com.sasken.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sasken.Model.ReviewComment;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByPostId(Long postId);
}
