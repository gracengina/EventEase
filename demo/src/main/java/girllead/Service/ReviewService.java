package girllead.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import girllead.demo.Exception.ResourceNotFoundException;
import girllead.demo.backendenums.BookingStatus;
import girllead.demo.dto.ReviewRequest;
import girllead.demo.model.Booking;
import girllead.demo.model.Review;
import girllead.demo.model.Vendor;
import girllead.demo.repository.BookingRepository;
import girllead.demo.repository.ReviewRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    public Review createReview(Long userId, ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Validate user can review this booking
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only review your own bookings");
        }
        
        // Check if booking is completed
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("You can only review completed bookings");
        }
        
        // Check if review already exists
        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new IllegalArgumentException("Review already exists for this booking");
        }
        
        // Create review
        Review review = new Review();
        review.setUser(booking.getUser());
        review.setVendor(booking.getVendor());
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update vendor's average rating
        updateVendorRating(booking.getVendor());
        
        return savedReview;
    }
    
    public Review updateReview(Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        // Check if review can be edited
        if (!review.canBeEdited()) {
            throw new IllegalArgumentException("Review can only be edited within 7 days of creation");
        }
        
        Integer oldRating = review.getRating();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update vendor rating if rating changed
        if (!oldRating.equals(request.getRating())) {
            updateVendorRating(review.getVendor());
        }
        
        return savedReview;
    }
    
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }
    
    public List<Review> getVendorReviews(Long vendorId) {
        return reviewRepository.findVendorReviewsOrderByDate(vendorId);
    }
    
    public Page<Review> getVendorReviewsPaginated(Long vendorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByVendorId(vendorId, pageable);
    }
    
    public List<Review> getUserReviews(Long userId) {
        return reviewRepository.findUserReviewsOrderByDate(userId);
    }
    
    public Review getReviewByBookingId(Long bookingId) {
        return reviewRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found for this booking"));
    }
    
    public List<Review> getVendorReviewsByRating(Long vendorId, Integer rating) {
        return reviewRepository.findByVendorIdAndRating(vendorId, rating);
    }
    
    public List<Review> getHighRatedVendorReviews(Long vendorId, Integer minRating) {
        return reviewRepository.findHighRatedReviews(vendorId, minRating);
    }
    
    public List<Review> getRecentReviewsWithComments(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return reviewRepository.findReviewsWithComments(pageable);
    }
    
    public BigDecimal getVendorAverageRating(Long vendorId) {
        BigDecimal average = reviewRepository.findAverageRatingByVendorId(vendorId);
        return average != null ? average : BigDecimal.ZERO;
    }
    
    public long getVendorReviewCount(Long vendorId) {
        return reviewRepository.countByVendorId(vendorId);
    }
    
    public long getVendorReviewCountByRating(Long vendorId, Integer rating) {
        return reviewRepository.countByVendorIdAndRating(vendorId, rating);
    }
    
    public boolean canUserReviewBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        return booking.getUser().getId().equals(userId) 
                && booking.getStatus() == BookingStatus.COMPLETED 
                && !reviewRepository.existsByBookingId(bookingId);
    }
    
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        Vendor vendor = review.getVendor();
        reviewRepository.delete(review);
        
        // Update vendor rating after deletion
        updateVendorRating(vendor);
    }
    
    // ---- Private Helper Methods ----
    private void updateVendorRating(Vendor vendor) {
        BigDecimal averageRating = reviewRepository.findAverageRatingByVendorId(vendor.getId());
        long reviewCount = reviewRepository.countByVendorId(vendor.getId());
        
        vendor.setAverageRating(averageRating != null ? averageRating : BigDecimal.ZERO);
        vendor.setTotalReviews((int) reviewCount);
        
        // Note: We don't save the vendor here as it would require VendorRepository
        // This should be handled by the calling service or through a vendor service method
    }
}
