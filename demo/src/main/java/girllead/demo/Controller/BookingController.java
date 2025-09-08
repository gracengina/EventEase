package girllead.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import girllead.Service.BookingService;
import girllead.Service.VendorService;
import girllead.demo.backendenums.BookingStatus;
import girllead.demo.dto.BookingRequest;
import girllead.demo.dto.BookingResponse;
import girllead.demo.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
@PreAuthorize("isAuthenticated()")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private VendorService vendorService;
    
    // User booking endpoints
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        BookingResponse booking = bookingService.createBooking(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
    
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<BookingResponse> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/my-bookings/status/{status}")
    public ResponseEntity<List<BookingResponse>> getMyBookingsByStatus(
            @PathVariable BookingStatus status,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        List<BookingResponse> bookings = bookingService.getUserBookingsByStatus(user.getId(), status);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        BookingResponse booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(booking);
    }
    
    // Vendor booking endpoints
    @GetMapping("/vendor-bookings")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<BookingResponse>> getVendorBookings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        var vendor = vendorService.getVendorByUserId(user.getId());
        List<BookingResponse> bookings = bookingService.getVendorBookings(vendor.getId());
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/vendor-bookings/status/{status}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<BookingResponse>> getVendorBookingsByStatus(
            @PathVariable BookingStatus status,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        var vendor = vendorService.getVendorByUserId(user.getId());
        List<BookingResponse> bookings = bookingService.getVendorBookingsByStatus(vendor.getId(), status);
        return ResponseEntity.ok(bookings);
    }
    
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        BookingResponse booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(booking);
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Long id) {
        BookingResponse booking = bookingService.completeBooking(id);
        return ResponseEntity.ok(booking);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        BookingStatus status = BookingStatus.valueOf(request.get("status"));
        BookingResponse booking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(booking);
    }
    
    // Analytics and reporting endpoints
    @GetMapping("/upcoming")
    public ResponseEntity<List<BookingResponse>> getUpcomingBookings() {
        List<BookingResponse> bookings = bookingService.getUpcomingBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/past")
    public ResponseEntity<List<BookingResponse>> getPastBookings() {
        List<BookingResponse> bookings = bookingService.getPastBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<BookingResponse>> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<BookingResponse> bookings = bookingService.getBookingsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/stats/user")
    public ResponseEntity<Map<String, Object>> getUserBookingStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> stats = Map.of(
                "totalBookings", bookingService.countUserBookingsByStatus(user.getId(), null),
                "pendingBookings", bookingService.countUserBookingsByStatus(user.getId(), BookingStatus.PENDING),
                "confirmedBookings", bookingService.countUserBookingsByStatus(user.getId(), BookingStatus.CONFIRMED),
                "completedBookings", bookingService.countUserBookingsByStatus(user.getId(), BookingStatus.COMPLETED),
                "cancelledBookings", bookingService.countUserBookingsByStatus(user.getId(), BookingStatus.CANCELLED)
        );
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/vendor")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, Object>> getVendorBookingStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        var vendor = vendorService.getVendorByUserId(user.getId());
        
        Map<String, Object> stats = Map.of(
                "totalBookings", bookingService.countVendorBookingsByStatus(vendor.getId(), null),
                "pendingBookings", bookingService.countVendorBookingsByStatus(vendor.getId(), BookingStatus.PENDING),
                "confirmedBookings", bookingService.countVendorBookingsByStatus(vendor.getId(), BookingStatus.CONFIRMED),
                "completedBookings", bookingService.countVendorBookingsByStatus(vendor.getId(), BookingStatus.COMPLETED),
                "cancelledBookings", bookingService.countVendorBookingsByStatus(vendor.getId(), BookingStatus.CANCELLED)
        );
        
        return ResponseEntity.ok(stats);
    }
}
