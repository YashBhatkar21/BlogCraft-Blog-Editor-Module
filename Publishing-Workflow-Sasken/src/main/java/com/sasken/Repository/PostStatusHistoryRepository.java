package com.sasken.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sasken.Model.PostStatusHistory;

public interface PostStatusHistoryRepository extends JpaRepository<PostStatusHistory, Long> {
    List<PostStatusHistory> findByPostId(Long postId);
}
