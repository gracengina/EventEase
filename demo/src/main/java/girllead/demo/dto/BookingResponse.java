package girllead.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import girllead.demo.backendenums.BookingStatus;

public class BookingResponse {
    
    private Long id;
    private LocalDateTime eventDate;
    private String eventLocation;
    private String eventDescription;
    private Integer guestCount;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private String specialRequirements;
    private LocalDateTime bookingDate;
    private LocalDateTime updatedAt;
    
    // User information
    private Long userId;
    private String userFullName;
    private String userEmail;
    
    // Vendor information
    private Long vendorId;
    private String vendorBusinessName;
    private String vendorCategory;
    private String vendorProfileImage;
    
    // Review information (if exists)
    private Boolean hasReview;
    private Integer reviewRating;
    
    // ---- Constructors ----
    public BookingResponse() {}
    
    public BookingResponse(Long id, LocalDateTime eventDate, BookingStatus status) {
        this.id = id;
        this.eventDate = eventDate;
        this.status = status;
    }
    
    // ---- Getters and Setters ----
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }
    
    public String getEventLocation() {
        return eventLocation;
    }
    
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    
    public String getEventDescription() {
        return eventDescription;
    }
    
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    
    public Integer getGuestCount() {
        return guestCount;
    }
    
    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public String getSpecialRequirements() {
        return specialRequirements;
    }
    
    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserFullName() {
        return userFullName;
    }
    
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public Long getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    
    public String getVendorBusinessName() {
        return vendorBusinessName;
    }
    
    public void setVendorBusinessName(String vendorBusinessName) {
        this.vendorBusinessName = vendorBusinessName;
    }
    
    public String getVendorCategory() {
        return vendorCategory;
    }
    
    public void setVendorCategory(String vendorCategory) {
        this.vendorCategory = vendorCategory;
    }
    
    public String getVendorProfileImage() {
        return vendorProfileImage;
    }
    
    public void setVendorProfileImage(String vendorProfileImage) {
        this.vendorProfileImage = vendorProfileImage;
    }
    
    public Boolean getHasReview() {
        return hasReview;
    }
    
    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
    }
    
    public Integer getReviewRating() {
        return reviewRating;
    }
    
    public void setReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }
    
    @Override
    public String toString() {
        return "BookingResponse{" +
                "id=" + id +
                ", eventDate=" + eventDate +
                ", eventLocation='" + eventLocation + '\'' +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", vendorBusinessName='" + vendorBusinessName + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }

    public void setUserName(String fullName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUserName'");
    }

    public void setVendorName(String businessName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVendorName'");
    }

    public void setSpecialRequests(Object specialRequests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSpecialRequests'");
    }

    public void setEventType(Object eventType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEventType'");
    }

    public void setBudget(Object budget) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBudget'");
    }

    public void setCreatedAt(Object createdAt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreatedAt'");
    }
}
