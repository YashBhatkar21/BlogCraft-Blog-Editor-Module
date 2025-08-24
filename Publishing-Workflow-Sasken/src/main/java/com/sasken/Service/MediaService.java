package com.sasken.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sasken.Model.BlogPost;
import com.sasken.Model.Media;
import com.sasken.Model.MediaStatus;
import com.sasken.Repository.MediaRepository;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Slf4j
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.max-size:10485760}") // 10MB default
    private long maxFileSize;

    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
    private static final String[] ALLOWED_VIDEO_TYPES = {"video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv"};
    private static final String[] ALLOWED_DOCUMENT_TYPES = {"application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

    public Media uploadMedia(MultipartFile file, BlogPost blogPost, String altText, String caption) throws IOException {
        // Validate file
        validateFile(file);
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + fileExtension;
        
        // Determine file type
        String fileType = determineFileType(file.getContentType());
        
        // Save file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        // Create media entity
        Media media = Media.builder()
                .fileName(fileName)
                .originalFileName(originalFileName)
                .filePath(filePath.toString())
                .fileUrl("/media/" + fileName)
                .fileType(fileType)
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .altText(altText)
                .caption(caption)
                .blogPost(blogPost)
                .status(MediaStatus.UPLOADING)
                .uploadedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Process media based on type
        if ("image".equals(fileType)) {
            processImage(media, filePath);
        } else if ("video".equals(fileType)) {
            processVideo(media, filePath);
        }

        media.setStatus(MediaStatus.READY);
        return mediaRepository.save(media);
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new IOException("File size exceeds maximum allowed size");
        }
        
        String contentType = file.getContentType();
        boolean isValidType = false;
        
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        for (String allowedType : ALLOWED_VIDEO_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        for (String allowedType : ALLOWED_DOCUMENT_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        if (!isValidType) {
            throw new IOException("File type not allowed");
        }
    }

    private String determineFileType(String contentType) {
        for (String imageType : ALLOWED_IMAGE_TYPES) {
            if (imageType.equals(contentType)) {
                return "image";
            }
        }
        
        for (String videoType : ALLOWED_VIDEO_TYPES) {
            if (videoType.equals(contentType)) {
                return "video";
            }
        }
        
        return "document";
    }

    private void processImage(Media media, Path filePath) throws IOException {
        try {
            // Create thumbnail
            Path thumbnailPath = Paths.get(uploadDir, "thumbnails");
            if (!Files.exists(thumbnailPath)) {
                Files.createDirectories(thumbnailPath);
            }
            
            String thumbnailFileName = "thumb_" + media.getFileName();
            Path thumbnailFilePath = thumbnailPath.resolve(thumbnailFileName);
            
            Thumbnails.of(filePath.toFile())
                    .size(300, 300)
                    .keepAspectRatio(true)
                    .toFile(thumbnailFilePath.toFile());
            
            media.setThumbnailPath("/media/thumbnails/" + thumbnailFileName);
            
            // Get image dimensions
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(filePath.toFile());
            media.setWidth(img.getWidth());
            media.setHeight(img.getHeight());
            
        } catch (Exception e) {
            log.error("Error processing image: " + e.getMessage());
            media.setStatus(MediaStatus.FAILED);
        }
    }

    private void processVideo(Media media, Path filePath) {
        // For video processing, you might want to use FFmpeg or similar
        // For now, we'll just set basic properties
        media.setStatus(MediaStatus.READY);
        // TODO: Implement video thumbnail generation and duration extraction
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public List<Media> getMediaByBlogPost(Long blogPostId) {
        return mediaRepository.findByBlogPostId(blogPostId);
    }

    public List<Media> getRecentImages() {
        return mediaRepository.findRecentImages();
    }

    public List<Media> getRecentVideos() {
        return mediaRepository.findRecentVideos();
    }

    public void deleteMedia(Long mediaId) throws IOException {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IOException("Media not found"));
        
        // Delete physical file
        Path filePath = Paths.get(media.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        
        // Delete thumbnail if exists
        if (media.getThumbnailPath() != null) {
            Path thumbnailPath = Paths.get(uploadDir, "thumbnails", media.getFileName().replaceFirst("^", "thumb_"));
            if (Files.exists(thumbnailPath)) {
                Files.delete(thumbnailPath);
            }
        }
        
        mediaRepository.delete(media);
    }

    public Media updateMediaMetadata(Long mediaId, String altText, String caption) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        
        media.setAltText(altText);
        media.setCaption(caption);
        media.setUpdatedAt(LocalDateTime.now());
        
        return mediaRepository.save(media);
    }
}
