package girllead.demo.dto;

import girllead.demo.backendenums.UserRole;

public class AuthResponse {
    
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private boolean isVendor;
    private Long vendorId; // If user is a vendor
    
    // ---- Constructors ----
    public AuthResponse() {}
    
    public AuthResponse(String token, Long userId, String username, String email, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isVendor = role == UserRole.VENDOR;
    }
    
    public AuthResponse(String token, Long userId, String username, String email, String fullName, UserRole role) {
        this(token, userId, username, email, role);
        this.fullName = fullName;
    }
    
    // ---- Getters and Setters ----
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
        this.isVendor = role == UserRole.VENDOR;
    }
    
    public boolean isVendor() {
        return isVendor;
    }
    
    public void setVendor(boolean vendor) {
        isVendor = vendor;
    }
    
    public Long getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", isVendor=" + isVendor +
                ", vendorId=" + vendorId +
                '}';
    }
}