package com.sasken.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sasken.Model.BlogPost;
import com.sasken.Model.Media;
import com.sasken.Service.BlogPostService;
import com.sasken.Service.MediaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/media")
@CrossOrigin("*")
@Tag(name = "Media Management", description = "Media upload and management endpoints")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private BlogPostService blogPostService;

    @PostMapping("/upload")
    @Operation(
        summary = "Upload media file",
        description = "Upload an image, video, or document file for a blog post"
    )
    public ResponseEntity<Media> uploadMedia(
        @RequestPart("file") MultipartFile file,
        @RequestParam("blogPostId") Long blogPostId,
        @RequestParam(value = "altText", required = false) String altText,
        @RequestParam(value = "caption", required = false) String caption
    ) {
        try {
            BlogPost blogPost = blogPostService.getPost(blogPostId);
            Media media = mediaService.uploadMedia(file, blogPost, altText, caption);
            return ResponseEntity.ok(media);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/post/{blogPostId}")
    @Operation(
        summary = "Get media by blog post",
        description = "Retrieve all media files associated with a specific blog post"
    )
    public ResponseEntity<List<Media>> getMediaByBlogPost(
        @Parameter(description = "ID of the blog post", required = true)
        @PathVariable Long blogPostId
    ) {
        List<Media> media = mediaService.getMediaByBlogPost(blogPostId);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/recent/images")
    @Operation(
        summary = "Get recent images",
        description = "Retrieve recently uploaded images"
    )
    public ResponseEntity<List<Media>> getRecentImages() {
        List<Media> images = mediaService.getRecentImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/recent/videos")
    @Operation(
        summary = "Get recent videos",
        description = "Retrieve recently uploaded videos"
    )
    public ResponseEntity<List<Media>> getRecentVideos() {
        List<Media> videos = mediaService.getRecentVideos();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/file/{fileName}")
    @Operation(
        summary = "Download media file",
        description = "Download a media file by its filename"
    )
    public ResponseEntity<Resource> downloadFile(
        @Parameter(description = "Name of the file to download", required = true)
        @PathVariable String fileName
    ) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get("uploads").resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/thumbnail/{fileName}")
    @Operation(
        summary = "Get thumbnail",
        description = "Get a thumbnail image for a media file"
    )
    public ResponseEntity<Resource> getThumbnail(
        @Parameter(description = "Name of the thumbnail file", required = true)
        @PathVariable String fileName
    ) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get("uploads/thumbnails").resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{mediaId}/metadata")
    @Operation(
        summary = "Update media metadata",
        description = "Update alt text and caption for a media file"
    )
    public ResponseEntity<Media> updateMediaMetadata(
        @Parameter(description = "ID of the media file", required = true)
        @PathVariable Long mediaId,
        @RequestParam("altText") String altText,
        @RequestParam("caption") String caption
    ) {
        try {
            Media media = mediaService.updateMediaMetadata(mediaId, altText, caption);
            return ResponseEntity.ok(media);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{mediaId}")
    @Operation(
        summary = "Delete media file",
        description = "Delete a media file and its associated data"
    )
    public ResponseEntity<String> deleteMedia(
        @Parameter(description = "ID of the media file to delete", required = true)
        @PathVariable Long mediaId
    ) {
        try {
            mediaService.deleteMedia(mediaId);
            return ResponseEntity.ok("Media deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to delete media: " + e.getMessage());
        }
    }
}
