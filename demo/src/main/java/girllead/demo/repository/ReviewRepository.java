package girllead.demo.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import girllead.demo.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByVendorId(Long vendorId);
    
    Page<Review> findByVendorId(Long vendorId, Pageable pageable);
    
    List<Review> findByUserId(Long userId);
    
    Optional<Review> findByBookingId(Long bookingId);
    
    boolean existsByBookingId(Long bookingId);
    
    @Query("SELECT r FROM Review r WHERE r.vendor.id = :vendorId ORDER BY r.createdAt DESC")
    List<Review> findVendorReviewsOrderByDate(@Param("vendorId") Long vendorId);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Review> findUserReviewsOrderByDate(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Review r WHERE r.vendor.id = :vendorId AND r.rating = :rating")
    List<Review> findByVendorIdAndRating(@Param("vendorId") Long vendorId, @Param("rating") Integer rating);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vendor.id = :vendorId")
    BigDecimal findAverageRatingByVendorId(@Param("vendorId") Long vendorId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.vendor.id = :vendorId")
    long countByVendorId(@Param("vendorId") Long vendorId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.vendor.id = :vendorId AND r.rating = :rating")
    long countByVendorIdAndRating(@Param("vendorId") Long vendorId, @Param("rating") Integer rating);
    
    @Query("SELECT r FROM Review r WHERE r.vendor.id = :vendorId AND r.rating >= :minRating ORDER BY r.createdAt DESC")
    List<Review> findHighRatedReviews(@Param("vendorId") Long vendorId, @Param("minRating") Integer minRating);
    
    @Query("SELECT r FROM Review r WHERE r.comment IS NOT NULL AND r.comment != '' ORDER BY r.createdAt DESC")
    List<Review> findReviewsWithComments(Pageable pageable);
}
