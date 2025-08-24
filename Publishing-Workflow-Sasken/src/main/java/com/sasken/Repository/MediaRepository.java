package com.sasken.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sasken.Model.Media;
import com.sasken.Model.MediaStatus;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByBlogPostId(Long blogPostId);
    
    List<Media> findByFileType(String fileType);
    
    List<Media> findByStatus(MediaStatus status);
    
    @Query("SELECT m FROM Media m WHERE m.blogPost.id = :postId AND m.fileType = :fileType")
    List<Media> findByBlogPostIdAndFileType(@Param("postId") Long postId, @Param("fileType") String fileType);
    
    Optional<Media> findByFileName(String fileName);
    
    @Query("SELECT m FROM Media m WHERE m.fileType = 'image' AND m.status = 'READY' ORDER BY m.uploadedAt DESC")
    List<Media> findRecentImages();
    
    @Query("SELECT m FROM Media m WHERE m.fileType = 'video' AND m.status = 'READY' ORDER BY m.uploadedAt DESC")
    List<Media> findRecentVideos();
}
