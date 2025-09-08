package girllead.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import girllead.demo.Exception.ResourceNotFoundException;
import girllead.demo.backendenums.BookingStatus;
import girllead.demo.dto.BookingRequest;
import girllead.demo.dto.BookingResponse;
import girllead.demo.model.Booking;
import girllead.demo.model.User;
import girllead.demo.model.Vendor;
import girllead.demo.repository.BookingRepository;
import girllead.demo.repository.UserRepository;
import girllead.demo.repository.VendorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    public BookingResponse createBooking(Long userId, BookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        // Validate event date is in the future
        if (request.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        
        // Create new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVendor(vendor);
        booking.setEventDate(request.getEventDate());
        booking.setEventLocation(request.getEventLocation());
        booking.setEventType(request.getEventType());
        booking.setGuestCount(request.getGuestCount());
        booking.setSpecialRequests(request.getSpecialRequests());
        booking.setBudget(request.getBudget());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        
        Booking savedBooking = bookingRepository.save(booking);
        return mapToBookingResponse(savedBooking);
    }
    
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapToBookingResponse(booking);
    }
    
    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getUserBookingsByStatus(Long userId, BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getVendorBookings(Long vendorId) {
        List<Booking> bookings = bookingRepository.findByVendorIdOrderByCreatedAtDesc(vendorId);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    // Replace this method in your BookingService:
public List<BookingResponse> getVendorBookingsByStatus(Long vendorId, BookingStatus status) {
    // FIXED: Changed from findByUserIdAndStatusOrderByCreatedAtDesc to findByVendorIdAndStatusOrderByCreatedAtDesc
    List<Booking> bookings = bookingRepository.findByVendorIdAndStatusOrderByCreatedAtDesc(vendorId, status);
    return bookings.stream()
            .map(this::mapToBookingResponse)
            .collect(Collectors.toList());
}
    
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToBookingResponse(updatedBooking);
    }
    
    public BookingResponse confirmBooking(Long bookingId) {
        return updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
    }
    
    public BookingResponse completeBooking(Long bookingId) {
        return updateBookingStatus(bookingId, BookingStatus.COMPLETED);
    }
    
    public BookingResponse cancelBooking(Long bookingId) {
        return updateBookingStatus(bookingId, BookingStatus.CANCELLED);
    }
    
    public List<BookingResponse> getUpcomingBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByEventDateAfterOrderByEventDateAsc(now);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getPastBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByEventDateBeforeOrderByEventDateDesc(now);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingRepository.findByEventDateBetweenOrderByEventDateAsc(startDate, endDate);
        return bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }
    
    public Long countUserBookingsByStatus(Long userId, BookingStatus status) {
        return bookingRepository.countByUserIdAndStatus(userId, status);
    }
    
    public Long countVendorBookingsByStatus(Long vendorId, BookingStatus status) {
        return bookingRepository.countByVendorIdAndStatus(vendorId, status);
    }
    
    // Helper method to map Booking entity to BookingResponse DTO
    private BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setUserName(booking.getUser().getFullName());
        response.setVendorId(booking.getVendor().getId());
        response.setVendorName(booking.getVendor().getBusinessName());
        response.setEventDate(booking.getEventDate());
        response.setEventLocation(booking.getEventLocation());
        response.setEventType(booking.getEventType());
        response.setGuestCount(booking.getGuestCount());
        response.setSpecialRequests(booking.getSpecialRequests());
        response.setBudget(booking.getBudget());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
}