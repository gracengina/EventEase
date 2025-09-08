package girllead.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import girllead.demo.backendenums.VendorCategory;

@Entity
@Table(name = "vendors")
public class Vendor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "business_name", nullable = false)
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Category is required")
    private VendorCategory category;
    
    @Column(name = "service_location")
    private String serviceLocation;
    
    @Column(name = "min_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Minimum price must be non-negative")
    private BigDecimal minPrice;
    
    @Column(name = "max_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Maximum price must be non-negative")
    private BigDecimal maxPrice;
    
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;
    
    @Column(name = "total_reviews")
    private Integer totalReviews = 0;
    
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    @ElementCollection
    @CollectionTable(name = "vendor_gallery", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "image_url")
    private List<String> galleryImages = new ArrayList<>();
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // One-to-one relationship with User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    // One-to-many relationship with Reviews
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
    
    // One-to-many relationship with Bookings
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();
    
    // ---- Constructors ----
    public Vendor() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Vendor(String businessName, VendorCategory category, User user) {
        this();
        this.businessName = businessName;
        this.category = category;
        this.user = user;
    }
    
    // ---- JPA Lifecycle Methods ----
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ---- Business Logic Methods ----
    public void updateRating(BigDecimal newRating) {
    if (totalReviews == 0) {
        this.averageRating = newRating;
        this.totalReviews = 1;
    } else {
        BigDecimal totalRatingPoints = averageRating.multiply(BigDecimal.valueOf(totalReviews));
        totalRatingPoints = totalRatingPoints.add(newRating);
        this.totalReviews++;
        this.averageRating = totalRatingPoints.divide(BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP);
    }
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public List<Booking> getBookings() {
        return bookings;
    }
    
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    
    // ---- Object Methods ----
    @Override
    public String toString() {
        return "Vendor{" +
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendor)) return false;
        Vendor vendor = (Vendor) o;
        return id != null && id.equals(vendor.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
