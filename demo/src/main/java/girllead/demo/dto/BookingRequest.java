package girllead.demo.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingRequest {
    
    @NotNull(message = "Vendor ID is required")
    private Long vendorId;
    
    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;
    
    private String eventLocation;
    
    private String eventDescription;
    
    @Min(value = 1, message = "Guest count must be at least 1")
    private Integer guestCount;
    
    private BigDecimal totalPrice;
    
    private String specialRequirements;
    
    // ---- Constructors ----
    public BookingRequest() {}
    
    public BookingRequest(Long vendorId, LocalDateTime eventDate) {
        this.vendorId = vendorId;
        this.eventDate = eventDate;
    }
    
    // ---- Getters and Setters ----
    public Long getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
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
    
    public String getSpecialRequirements() {
        return specialRequirements;
    }
    
    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }
    
    @Override
    public String toString() {
        return "BookingRequest{" +
                "vendorId=" + vendorId +
                ", eventDate=" + eventDate +
                ", eventLocation='" + eventLocation + '\'' +
                ", guestCount=" + guestCount +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public Object getEventType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEventType'");
    }

    public Object getSpecialRequests() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpecialRequests'");
    }

    public Object getBudget() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBudget'");
    }
}