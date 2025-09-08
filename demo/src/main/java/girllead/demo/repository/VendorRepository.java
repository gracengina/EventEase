package girllead.demo.repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import girllead.demo.backendenums.VendorCategory;
import girllead.demo.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    List<Vendor> findByCategory(VendorCategory category);
    
    List<Vendor> findByIsVerifiedTrue();
    
    List<Vendor> findByIsAvailableTrue();
    
    List<Vendor> findByCategoryAndIsVerifiedTrueAndIsAvailableTrue(VendorCategory category);
    
    @Query("SELECT v FROM Vendor v WHERE v.isVerified = true AND v.isAvailable = true " +
           "AND (:category IS NULL OR v.category = :category) " +
           "AND (:location IS NULL OR LOWER(v.serviceLocation) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:minPrice IS NULL OR v.minPrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR v.maxPrice <= :maxPrice) " +
           "AND (:minRating IS NULL OR v.averageRating >= :minRating)")
    Page<Vendor> findVendorsWithFilters(
            @Param("category") VendorCategory category,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRating") BigDecimal minRating,
            Pageable pageable
    );
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.businessName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(v.serviceLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Vendor> searchVendors(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT v FROM Vendor v WHERE v.averageRating >= :rating ORDER BY v.averageRating DESC")
    List<Vendor> findTopRatedVendors(@Param("rating") BigDecimal rating, Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.isVerified = true ORDER BY v.createdAt DESC")
    List<Vendor> findRecentlyJoined(Pageable pageable);
    
    Optional<Vendor> findByUserId(Long userId);
    
    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.category = :category AND v.isVerified = true")
    long countByCategoryAndVerified(@Param("category") VendorCategory category);
    
    @Query("SELECT v FROM Vendor v WHERE v.serviceLocation LIKE %:location% AND v.isAvailable = true")
    List<Vendor> findByServiceLocationContaining(@Param("location") String location);

    static boolean existsByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByEmail'");
    }
}
