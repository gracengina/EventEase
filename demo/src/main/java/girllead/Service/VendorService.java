package girllead.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import girllead.demo.Exception.ResourceNotFoundException;
import girllead.demo.backendenums.UserRole;
import girllead.demo.backendenums.VendorCategory;
import girllead.demo.dto.VendorRequest;
import girllead.demo.dto.VendorResponse;
import girllead.demo.model.User;
import girllead.demo.model.Vendor;
import girllead.demo.repository.UserRepository;
import girllead.demo.repository.VendorRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendorService {
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public VendorResponse createVendorProfile(Long userId, VendorRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check if user already has a vendor profile
        if (user.getVendor() != null) {
            throw new IllegalArgumentException("User already has a vendor profile");
        }
        
        // Create vendor
        Vendor vendor = new Vendor();
        vendor.setBusinessName(request.getBusinessName());
        vendor.setDescription(request.getDescription());
        vendor.setCategory(request.getCategory());
        vendor.setServiceLocation(request.getServiceLocation());
        vendor.setMinPrice(request.getMinPrice());
        vendor.setMaxPrice(request.getMaxPrice());
        vendor.setProfileImageUrl(request.getProfileImageUrl());
        vendor.setGalleryImages(request.getGalleryImages());
        vendor.setYearsOfExperience(request.getYearsOfExperience());
        vendor.setUser(user);
        
        // Update user role to VENDOR
        user.setRole(UserRole.VENDOR);
        
        // Save vendor and user
        Vendor savedVendor = vendorRepository.save(vendor);
        userRepository.save(user);
        
        return convertToResponse(savedVendor);
    }
    
    public VendorResponse updateVendorProfile(Long vendorId, VendorRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        // Update vendor details
        vendor.setBusinessName(request.getBusinessName());
        vendor.setDescription(request.getDescription());
        vendor.setCategory(request.getCategory());
        vendor.setServiceLocation(request.getServiceLocation());
        vendor.setMinPrice(request.getMinPrice());
        vendor.setMaxPrice(request.getMaxPrice());
        vendor.setProfileImageUrl(request.getProfileImageUrl());
        vendor.setGalleryImages(request.getGalleryImages());
        vendor.setYearsOfExperience(request.getYearsOfExperience());
        
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToResponse(savedVendor);
    }
    
    public VendorResponse getVendorById(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        return convertToResponse(vendor);
    }
    
    public VendorResponse getVendorByUserId(Long userId) {
        Vendor vendor = vendorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor profile not found for user"));
        return convertToResponse(vendor);
    }
    
    public Page<VendorResponse> searchVendors(
            VendorCategory category,
            String location,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minRating,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Vendor> vendors = vendorRepository.findVendorsWithFilters(
                category, location, minPrice, maxPrice, minRating, pageable);
        
        return vendors.map(this::convertToResponse);
    }
    
    public List<VendorResponse> getVendorsByCategory(VendorCategory category) {
        List<Vendor> vendors = vendorRepository.findByCategoryAndIsVerifiedTrueAndIsAvailableTrue(category);
        return vendors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<VendorResponse> getTopRatedVendors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Vendor> vendors = vendorRepository.findTopRatedVendors(BigDecimal.valueOf(4.0), pageable);
        return vendors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<VendorResponse> getRecentlyJoinedVendors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Vendor> vendors = vendorRepository.findRecentlyJoined(pageable);
        return vendors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<VendorResponse> searchVendorsByTerm(String searchTerm) {
        List<Vendor> vendors = vendorRepository.searchVendors(searchTerm);
        return vendors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public VendorResponse verifyVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        vendor.setIsVerified(true);
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToResponse(savedVendor);
    }
    
    public VendorResponse toggleVendorAvailability(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        vendor.setIsAvailable(!vendor.getIsAvailable());
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToResponse(savedVendor);
    }
    
    public VendorCategory[] getAllCategories() {
        return VendorCategory.values();
    }
    
    public long getVendorCountByCategory(VendorCategory category) {
        return vendorRepository.countByCategoryAndVerified(category);
    }
    
    // ---- Private Helper Methods ----
    private VendorResponse convertToResponse(Vendor vendor) {
        VendorResponse response = new VendorResponse();
        response.setId(vendor.getId());
        response.setBusinessName(vendor.getBusinessName());
        response.setDescription(vendor.getDescription());
        response.setCategory(vendor.getCategory());
        response.setServiceLocation(vendor.getServiceLocation());
        response.setMinPrice(vendor.getMinPrice());
        response.setMaxPrice(vendor.getMaxPrice());
        response.setAverageRating(vendor.getAverageRating());
        response.setTotalReviews(vendor.getTotalReviews());
        response.setProfileImageUrl(vendor.getProfileImageUrl());
        response.setGalleryImages(vendor.getGalleryImages());
        response.setIsVerified(vendor.getIsVerified());
        response.setIsAvailable(vendor.getIsAvailable());
        response.setYearsOfExperience(vendor.getYearsOfExperience());
        response.setCreatedAt(vendor.getCreatedAt());
        
        // Include user information
        if (vendor.getUser() != null) {
            User user = vendor.getUser();
            response.setOwnerName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setPhoneNumber(user.getPhoneNumber());
        }
        
        return response;
    }
}
