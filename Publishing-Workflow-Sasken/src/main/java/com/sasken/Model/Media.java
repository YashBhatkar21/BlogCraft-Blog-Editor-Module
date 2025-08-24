package com.sasken.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileUrl;
    private String fileType; // image, video, document
    private String mimeType;
    private Long fileSize;
    
    @Column(length = 1000)
    private String altText;
    private String caption;
    
    @Enumerated(EnumType.STRING)
    private MediaStatus status;
    
    @ManyToOne
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;
    
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
    
    // Image specific fields
    private Integer width;
    private Integer height;
    private String thumbnailPath;
    
    // Video specific fields
    private Integer duration; // in seconds
    private String thumbnailPathVideo;
}
