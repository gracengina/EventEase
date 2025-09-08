package girllead.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import girllead.demo.backendenums.BookingStatus;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "event_date", nullable = false)
    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;
    
    @Column(name = "event_location")
    private String eventLocation;
    
    @Column(name = "event_description", columnDefinition = "TEXT")
    private String eventDescription;
    
    @Column(name = "guest_count")
    private Integer guestCount;
    
    @Column(name = "total_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Total price must be non-negative")
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Many bookings belong to one user (event planner)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    // Many bookings belong to one vendor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnore
    private Vendor vendor;
    
    // One-to-one relationship with Review (optional)
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Review review;
    
    // Add these fields to your Booking entity class:

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @Column(name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;

    // ---- Constructors ----

    public Booking(User user, Vendor vendor, LocalDateTime eventDate) {
        this();
        this.user = user;
        this.vendor = vendor;
        this.eventDate = eventDate;
    }
    
    public Booking(User user, Vendor vendor, LocalDateTime eventDate, String eventLocation, BigDecimal totalPrice) {
        this(user, vendor, eventDate);
        this.eventLocation = eventLocation;
        this.totalPrice = totalPrice;
    }
    
    // ---- JPA Lifecycle Methods ----
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ---- Business Logic Methods ----
    public boolean canBeCancelled() {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED;
    }
    
    public boolean canBeCompleted() {
        return status == BookingStatus.CONFIRMED && eventDate.isBefore(LocalDateTime.now());
    }
    
    public boolean canBeReviewed() {
        return status == BookingStatus.COMPLETED && review == null;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Vendor getVendor() {
        return vendor;
    }
    
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    
    public Review getReview() {
        return review;
    }
    
    public void setReview(Review review) {
        this.review = review;
    }
    // Replace the placeholder methods in your Booking entity with these:

@Column(name = "created_at", nullable = false)
private LocalDateTime createdAt;

// Constructor should initialize createdAt
public Booking() {
    this.bookingDate = LocalDateTime.now();
    this.createdAt = LocalDateTime.now();
}

// Proper getter and setter methods
public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public String getEventType() {
    return eventType;
}

public void setEventType(String eventType) {
    this.eventType = eventType;
}

public String getSpecialRequests() {
    return specialRequests;
}

public void setSpecialRequests(String specialRequests) {
    this.specialRequests = specialRequests;
}

public BigDecimal getBudget() {
    return budget;
}

public void setBudget(BigDecimal budget) {
    this.budget = budget;
}
    
    // ---- Object Methods ----
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", eventDate=" + eventDate +
                ", eventLocation='" + eventLocation + '\'' +
                ", guestCount=" + guestCount +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", bookingDate=" + bookingDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return id != null && id.equals(booking.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setBudget(Object budget) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBudget'");
    }

    public void setEventType(Object eventType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpecialRequests(Object specialRequests2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSpecialRequests'");
    }
}
