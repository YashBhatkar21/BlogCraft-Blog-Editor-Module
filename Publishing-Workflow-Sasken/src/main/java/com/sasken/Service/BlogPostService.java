package com.sasken.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Repository.BlogPostRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepo;

    public BlogPost createDraft(BlogPost post) {
        post.setStatus(PostStatus.DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        // Ensure authorName is not null
        if (post.getAuthorName() == null || post.getAuthorName().trim().isEmpty()) {
            post.setAuthorName("Anonymous");
        }
        return blogPostRepo.save(post);
    }

    public BlogPost changeStatus(Long postId, PostStatus newStatus, Long userId) {
        BlogPost post = blogPostRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        PostStatus currentStatus = post.getStatus();

        if (isValidTransition(currentStatus, newStatus)) {
            post.setStatus(newStatus);
            post.setUpdatedAt(LocalDateTime.now());
            return blogPostRepo.save(post);
        } else {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private boolean isValidTransition(PostStatus current, PostStatus next) {
    return (current == PostStatus.DRAFT && next == PostStatus.REVIEW) ||
           (current == PostStatus.REVIEW && (next == PostStatus.APPROVED || next == PostStatus.DRAFT)) ||
           (current == PostStatus.APPROVED && (next == PostStatus.PUBLISHED || next == PostStatus.REVIEW)); // <-- ADD THIS
    }


    public List<BlogPost> getAllPosts() {
        return blogPostRepo.findAll();
    }

    public BlogPost getPost(Long postId) {
        return blogPostRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    public void deletePost(Long postId) {
        blogPostRepo.deleteById(postId);
    }

    public BlogPost updatePost(Long postId, BlogPost updatedPost) {
    BlogPost existing = blogPostRepo.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found"));

    existing.setTitle(updatedPost.getTitle());
    existing.setContent(updatedPost.getContent());
    // Update author name if provided
    if (updatedPost.getAuthorName() != null && !updatedPost.getAuthorName().trim().isEmpty()) {
        existing.setAuthorName(updatedPost.getAuthorName());
    }
    existing.setUpdatedAt(LocalDateTime.now());

    return blogPostRepo.save(existing);
}


}
