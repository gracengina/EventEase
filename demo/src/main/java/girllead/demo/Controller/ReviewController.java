package girllead.demo.Controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import girllead.Service.ReviewService;
import girllead.demo.dto.ReviewRequest;
import girllead.demo.model.Review;
import girllead.demo.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    // Public endpoints - no authentication required
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Review>> getVendorReviews(@PathVariable Long vendorId) {
        List<Review> reviews = reviewService.getVendorReviews(vendorId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/vendor/{vendorId}/paginated")
    public ResponseEntity<Page<Review>> getVendorReviewsPaginated(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<Review> reviews = reviewService.getVendorReviewsPaginated(vendorId, page, size);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/vendor/{vendorId}/rating/{rating}")
    public ResponseEntity<List<Review>> getVendorReviewsByRating(
            @PathVariable Long vendorId,
            @PathVariable Integer rating) {
        
        List<Review> reviews = reviewService.getVendorReviewsByRating(vendorId, rating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/vendor/{vendorId}/stats")
    public ResponseEntity<Map<String, Object>> getVendorReviewStats(@PathVariable Long vendorId) {
        BigDecimal averageRating = reviewService.getVendorAverageRating(vendorId);
        long totalReviews = reviewService.getVendorReviewCount(vendorId);
        
        Map<String, Object> stats = Map.of(
                "averageRating", averageRating,
                "totalReviews", totalReviews,
                "fiveStarCount", reviewService.getVendorReviewCountByRating(vendorId, 5),
                "fourStarCount", reviewService.getVendorReviewCountByRating(vendorId, 4),
                "threeStarCount", reviewService.getVendorReviewCountByRating(vendorId, 3),
                "twoStarCount", reviewService.getVendorReviewCountByRating(vendorId, 2),
                "oneStarCount", reviewService.getVendorReviewCountByRating(vendorId, 1)
        );
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/vendor/{vendorId}/high-rated")
    public ResponseEntity<List<Review>> getHighRatedVendorReviews(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "4") Integer minRating) {
        
        List<Review> reviews = reviewService.getHighRatedVendorReviews(vendorId, minRating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<Review>> getRecentReviewsWithComments(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Review> reviews = reviewService.getRecentReviewsWithComments(limit);
        return ResponseEntity.ok(reviews);
    }
    
    // Protected endpoints - authentication required
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Review> createReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        Review review = reviewService.createReview(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request) {
        
        Review review = reviewService.updateReview(id, request);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/my-reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Review>> getMyReviews(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Review> reviews = reviewService.getUserReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Review> getReviewByBookingId(@PathVariable Long bookingId) {
        Review review = reviewService.getReviewByBookingId(bookingId);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/can-review/{bookingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> canReviewBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        boolean canReview = reviewService.canUserReviewBooking(user.getId(), bookingId);
        
        Map<String, Object> response = Map.of(
                "canReview", canReview,
                "message", canReview 
                    ? "You can review this booking" 
                    : "This booking cannot be reviewed (not completed or already reviewed)"
        );
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        
        Map<String, String> response = Map.of(
                "message", "Review deleted successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}
