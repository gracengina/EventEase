package girllead.demo.backendenums;
public enum VendorCategory {
    CATERING("Catering"),
    DECORATION("Decoration"),
    PHOTOGRAPHY("Photography"),
    VIDEOGRAPHY("Videography"),
    DJ_MUSIC("DJ & Music"),
    VENUE("Venue"),
    LIGHTING("Lighting"),
    SOUND("Sound System"),
    TRANSPORT("Transport"),
    SECURITY("Security"),
    PLANNING("Event Planning"),
    FLOWERS("Flowers"),
    ENTERTAINMENT("Entertainment"),
    EQUIPMENT_RENTAL("Equipment Rental"),
    OTHER("Other");
    
    private final String displayName;
    
    VendorCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static VendorCategory fromString(String categoryStr) {
        for (VendorCategory category : VendorCategory.values()) {
            if (category.name().equalsIgnoreCase(categoryStr)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown vendor category: " + categoryStr);
    }
}
