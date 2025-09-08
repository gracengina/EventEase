package girllead.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import girllead.demo.backendenums.VendorCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VendorRequest {
    
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    private String description;
    
    @NotNull(message = "Category is required")
    private VendorCategory category;
    
    private String serviceLocation;
    
    @DecimalMin(value = "0.0", message = "Minimum price must be non-negative")
    private BigDecimal minPrice;
    
    @DecimalMin(value = "0.0", message = "Maximum price must be non-negative")
    private BigDecimal maxPrice;
    
    private String profileImageUrl;
    
    private List<String> galleryImages;
    
    private Integer yearsOfExperience;
    
    // ---- Constructors ----
    public VendorRequest() {}
    
    public VendorRequest(String businessName, VendorCategory category) {
        this.businessName = businessName;
        this.category = category;
    }
    
    // ---- Getters and Setters ----
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
    
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    
    @Override
    public String toString() {
        return "VendorRequest{" +
                "businessName='" + businessName + '\'' +
                ", category=" + category +
                ", serviceLocation='" + serviceLocation + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", yearsOfExperience=" + yearsOfExperience +
                '}';
    }
}
