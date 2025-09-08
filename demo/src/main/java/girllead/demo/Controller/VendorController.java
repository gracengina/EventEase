package girllead.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import girllead.Service.VendorService;
import girllead.demo.backendenums.VendorCategory;
import girllead.demo.dto.VendorRequest;
import girllead.demo.dto.VendorResponse;
import girllead.demo.model.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class VendorController {
    
    @Autowired
    private VendorService vendorService;
    
    // Public endpoints - no authentication required
    @GetMapping("/search")
    public ResponseEntity<Page<VendorResponse>> searchVendors(
            @RequestParam(required = false) VendorCategory category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "averageRating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<VendorResponse> vendors = vendorService.searchVendors(
                category, location, minPrice, maxPrice, minRating, page, size, sortBy, sortDir);
        return ResponseEntity.ok(vendors);
    }
    
@GetMapping("/categories")
public ResponseEntity<List<Map<String, Object>>> getAllCategories() {
    List<Map<String, Object>> categories = Arrays.stream(VendorCategory.values())
            .map(category -> {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("name", category.name());
                categoryMap.put("displayName", category.getDisplayName());
                categoryMap.put("count", vendorService.getVendorCountByCategory(category));
                return categoryMap;
            })
            .collect(Collectors.toList());
    
    return ResponseEntity.ok(categories);
}
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<VendorResponse>> getVendorsByCategory(@PathVariable VendorCategory category) {
        List<VendorResponse> vendors = vendorService.getVendorsByCategory(category);
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<VendorResponse>> getTopRatedVendors(@RequestParam(defaultValue = "10") int limit) {
        List<VendorResponse> vendors = vendorService.getTopRatedVendors(limit);
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<VendorResponse>> getRecentlyJoinedVendors(@RequestParam(defaultValue = "10") int limit) {
        List<VendorResponse> vendors = vendorService.getRecentlyJoinedVendors(limit);
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/{id}/public")
    public ResponseEntity<VendorResponse> getVendorPublicProfile(@PathVariable Long id) {
        VendorResponse vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);
    }
    
    @GetMapping("/search/text")
    public ResponseEntity<List<VendorResponse>> searchVendorsByText(@RequestParam String q) {
        List<VendorResponse> vendors = vendorService.searchVendorsByTerm(q);
        return ResponseEntity.ok(vendors);
    }
    
    // Protected endpoints - authentication required
    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VendorResponse> createVendorProfile(
            @Valid @RequestBody VendorRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        VendorResponse vendor = vendorService.createVendorProfile(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendor);
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> updateVendorProfile(
            @Valid @RequestBody VendorRequest request,
            Authentication authentication) {
        
        User user = (User) authentication.getPrincipal();
        VendorResponse existingVendor = vendorService.getVendorByUserId(user.getId());
        VendorResponse updatedVendor = vendorService.updateVendorProfile(existingVendor.getId(), request);
        return ResponseEntity.ok(updatedVendor);
    }
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> getMyVendorProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        VendorResponse vendor = vendorService.getVendorByUserId(user.getId());
        return ResponseEntity.ok(vendor);
    }
    
    @PutMapping("/profile/availability")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> toggleAvailability(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        VendorResponse vendor = vendorService.getVendorByUserId(user.getId());
        VendorResponse updatedVendor = vendorService.toggleVendorAvailability(vendor.getId());
        return ResponseEntity.ok(updatedVendor);
    }
    
    // Admin endpoints
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> verifyVendor(@PathVariable Long id) {
        VendorResponse vendor = vendorService.verifyVendor(id);
        return ResponseEntity.ok(vendor);
    }
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Map<String, Object>> getVendorDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        VendorResponse vendor = vendorService.getVendorByUserId(user.getId());
        
        // Create dashboard data
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("vendor", vendor);
        dashboard.put("isVerified", vendor.getIsVerified());
        dashboard.put("isAvailable", vendor.getIsAvailable());
        dashboard.put("averageRating", vendor.getAverageRating());
        dashboard.put("totalReviews", vendor.getTotalReviews());
        
        return ResponseEntity.ok(dashboard);
    }
}