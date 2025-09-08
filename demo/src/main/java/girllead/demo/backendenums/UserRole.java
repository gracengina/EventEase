package girllead.demo.backendenums;

public enum UserRole {
    USER("User"),
    VENDOR("Vendor"),
    ADMIN("Administrator");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static UserRole fromString(String roleStr) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + roleStr);
    }
}
