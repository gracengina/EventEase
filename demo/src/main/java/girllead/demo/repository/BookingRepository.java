package girllead.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import girllead.demo.backendenums.BookingStatus;
import girllead.demo.model.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByVendorId(Long vendorId);
    
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    
    List<Booking> findByVendorIdAndStatus(Long vendorId, BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.bookingDate DESC")
    List<Booking> findUserBookingsOrderByDate(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.vendor.id = :vendorId ORDER BY b.eventDate ASC")
    List<Booking> findVendorBookingsOrderByEventDate(@Param("vendorId") Long vendorId);
    
    @Query("SELECT b FROM Booking b WHERE b.eventDate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT b FROM Booking b WHERE b.vendor.id = :vendorId AND b.eventDate BETWEEN :startDate AND :endDate")
    List<Booking> findVendorBookingsBetweenDates(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.vendor.id = :vendorId AND b.status = :status")
    long countByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.eventDate < :currentDate AND b.status = 'CONFIRMED'")
    List<Booking> findPastConfirmedBookings(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT b FROM Booking b WHERE b.eventDate > :currentDate AND b.status IN ('PENDING', 'CONFIRMED')")
    List<Booking> findUpcomingBookings(@Param("currentDate") LocalDateTime currentDate);
    
    boolean existsByVendorIdAndEventDateBetween(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, BookingStatus status);

    List<Booking> findByVendorIdOrderByCreatedAtDesc(Long vendorId);

    List<Booking> findByEventDateBeforeOrderByEventDateDesc(LocalDateTime now);

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByEventDateAfterOrderByEventDateAsc(LocalDateTime now);

    List<Booking> findByEventDateBetweenOrderByEventDateAsc(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findByVendorIdAndStatusOrderByCreatedAtDesc(Long vendorId, BookingStatus status);
}