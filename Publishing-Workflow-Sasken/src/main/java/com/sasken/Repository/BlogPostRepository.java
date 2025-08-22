package com.sasken.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sasken.Model.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
