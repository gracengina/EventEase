package girllead.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import girllead.demo.backendenums.VendorCategory;

public class VendorResponse {
    
    private Long id;
    private String businessName;
    private String description;
    private VendorCategory category;
    private String serviceLocation;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private String profileImageUrl;
    private List<String> galleryImages;
    private Boolean isVerified;
    private Boolean isAvailable;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;
    
    // User information
    private String ownerName;
    private String email;
    private String phoneNumber;
    
    // ---- Constructors ----
    public VendorResponse() {}
    
    public VendorResponse(Long id, String businessName, VendorCategory category) {
        this.id = id;
        this.businessName = businessName;
        this.category = category;
    }
    
    // ---- Getters and Setters ----
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public VendorCategory getCategory() {
        return category;
    }
    
    public void setCategory(VendorCategory category) {
        this.category = category;
    }
    
    public String getServiceLocation() {
        return serviceLocation;
    }
    
    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }
    
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
    
    public Integer getTotalReviews() {
        return totalReviews;
    }
    
    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public List<String> getGalleryImages() {
        return galleryImages;
    }
    
    public void setGalleryImages(List<String> galleryImages) {
        this.galleryImages = galleryImages;
    }
    
    public Boolean getIsVerified() {
        return isVerified;
    }
    
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public String toString() {
        return "VendorResponse{" +
                "id=" + id +
                ", businessName='" + businessName + '\'' +
                ", category=" + category +
                ", serviceLocation='" + serviceLocation + '\'' +
                ", averageRating=" + averageRating +
                ", totalReviews=" + totalReviews +
                ", isVerified=" + isVerified +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
