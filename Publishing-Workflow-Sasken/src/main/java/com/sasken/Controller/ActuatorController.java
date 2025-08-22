package com.sasken.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Repository.BlogPostRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/actuator/blogcraft")
@Tag(name = "Monitoring", description = "Application monitoring and statistics endpoints")
public class ActuatorController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @GetMapping("/stats")
    @Operation(
        summary = "Get blog statistics",
        description = "Retrieves comprehensive statistics about blog posts including counts by status"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(value = """
                    {
                      "totalPosts": 25,
                      "draftPosts": 8,
                      "reviewPosts": 5,
                      "approvedPosts": 7,
                      "publishedPosts": 5,
                      "status": "healthy"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getBlogStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<BlogPost> allPosts = blogPostRepository.findAll();
            
            long totalPosts = allPosts.size();
            long draftPosts = allPosts.stream().filter(p -> p.getStatus() == PostStatus.DRAFT).count();
            long reviewPosts = allPosts.stream().filter(p -> p.getStatus() == PostStatus.REVIEW).count();
            long approvedPosts = allPosts.stream().filter(p -> p.getStatus() == PostStatus.APPROVED).count();
            long publishedPosts = allPosts.stream().filter(p -> p.getStatus() == PostStatus.PUBLISHED).count();
            
            stats.put("totalPosts", totalPosts);
            stats.put("draftPosts", draftPosts);
            stats.put("reviewPosts", reviewPosts);
            stats.put("approvedPosts", approvedPosts);
            stats.put("publishedPosts", publishedPosts);
            stats.put("status", "healthy");
            
        } catch (Exception e) {
            stats.put("status", "error");
            stats.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(
        summary = "Get application health",
        description = "Checks the health status of the application and database connectivity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Health check completed",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(value = """
                    {
                      "status": "UP",
                      "database": "Connected",
                      "service": "BlogCraft Publishing Workflow"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Health check failed")
    })
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Test database connectivity
            blogPostRepository.count();
            
            health.put("status", "UP");
            health.put("database", "Connected");
            health.put("service", "BlogCraft Publishing Workflow");
            
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("database", "Disconnected");
            health.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }
} 