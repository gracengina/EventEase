package girllead.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    private String comment;
    
    // ---- Constructors ----
    public ReviewRequest() {}
    
    public ReviewRequest(Long bookingId, Integer rating) {
        this.bookingId = bookingId;
        this.rating = rating;
    }
    
    public ReviewRequest(Long bookingId, Integer rating, String comment) {
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
    }
    
    // ---- Getters and Setters ----
    public Long getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public String toString() {
        return "ReviewRequest{" +
                "bookingId=" + bookingId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}