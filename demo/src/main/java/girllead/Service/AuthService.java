package girllead.Service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import girllead.demo.Security.JwtService;
import girllead.demo.backendenums.UserRole;
import girllead.demo.dto.AuthResponse;
import girllead.demo.dto.LoginRequest;
import girllead.demo.dto.RegisterRequest;
import girllead.demo.model.User;
import girllead.demo.repository.UserRepository;


@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public AuthResponse login(LoginRequest request) {
        try {
            // Validate input
            System.out.print("Validating login Request");
            validateLoginRequest(request);
            
            // Authenticate user
            System.out.print("Authenticating");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername().trim(),
                            request.getPassword()
                    )
            );
            
            // Get user details
            System.out.print("Placing in database");
            User user = userRepository.findByUsername(request.getUsername().trim())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));
            
            // Generate token
            String token = jwtService.generateToken(user);
            
            // Create response
            AuthResponse response = new AuthResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole()
            );
            
            // If user is a vendor, include vendor ID
            if (user.getVendor() != null) {
                response.setVendorId(user.getVendor().getId());
            }
            
            return response;
            
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Additional validation rules can be added here
        if (request.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
    }

    public AuthResponse register(RegisterRequest request) throws BadRequestException {
        try {
            System.out.println("=== REGISTRATION DEBUG START ===");
            System.out.println("1. Starting registration for user: " + request.getUsername());
            
            // Validate registration request
            System.out.println("2. Validating registration request...");
            validateRegisterRequest(request);
            System.out.println("   ✓ Validation passed");
            
            // Check if username already exists
            System.out.println("3. Checking if username exists: " + request.getUsername());
            if (usernameExists(request.getUsername())) {
                throw new BadRequestException("Username already exists");
            }
            System.out.println("   ✓ Username " + request.getUsername() + " is available");
            
            // Check if email already exists
            System.out.println("4. Checking if email exists: " + request.getEmail());
            if (emailExists(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            System.out.println("   ✓ Email is available");

            System.out.println("5. Creating user object...");
            // Create new user
            User user = new User();
            user.setUsername(request.getUsername().trim());
            user.setEmail(request.getEmail().trim().toLowerCase());
            user.setFirstName(request.getFirstName() != null ? request.getFirstName().trim() : null);
            user.setLastName(request.getLastName() != null ? request.getLastName().trim() : null);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole() != null ? request.getRole() : UserRole.USER);
            user.setIsActive(true);
            user.setPhoneNumber(request.getPhoneNumber());
            System.out.println("   ✓ User object created");
            
            System.out.println("6. About to save user to database...");
            // Save user
            user = userRepository.save(user);
            System.out.println("   ✓ User saved successfully with ID: " + user.getId());

            System.out.println("7. Generating JWT token...");
            // Generate token
            String token = jwtService.generateToken(user);
            System.out.println("   ✓ JWT token generated successfully");
            
            System.out.println("8. Creating response object...");
            // Create response
            AuthResponse response = new AuthResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole()
            );
            System.out.println("   ✓ Response object created");
            
            System.out.println("9. Registration completed successfully!");
            System.out.println("=== REGISTRATION DEBUG END ===");
            return response;
            
        } catch (Exception e) {
            System.err.println("=== REGISTRATION ERROR ===");
            System.err.println("Exception type: " + e.getClass().getSimpleName());
            System.err.println("Exception message: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();
            System.err.println("=== REGISTRATION ERROR END ===");
            
            // Re-throw the original exception
            if (e instanceof BadRequestException) {
                throw (BadRequestException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Registration failed: " + e.getMessage(), e);
            }
        }
    }
    
    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }
        
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Check for at least first name or last name
        if ((request.getFirstName() == null || request.getFirstName().trim().isEmpty()) &&
            (request.getLastName() == null || request.getLastName().trim().isEmpty())) {
            throw new IllegalArgumentException("At least first name or last name is required");
        }
        
        // Additional validation
        if (request.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        
        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return false;
            }
            
            // Use JwtService to validate the token
            String username = jwtService.extractUsername(token);
            if (username == null) {
                return false;
            }
            
            // Check if user exists and token is not expired
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return false;
            }
            
            return jwtService.isTokenValid(token, user);
            
        } catch (Exception e) {
            return false;
        }
    }

    public boolean usernameExists(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return false;
            }
            boolean exists = userRepository.findByUsername(username.trim()).isPresent();
            System.out.println("   Username exists check: " + username + " = " + exists);
            return exists;
        } catch (Exception e) {
            System.err.println("   Error checking username exists: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean emailExists(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            boolean exists = userRepository.findByEmail(email.trim().toLowerCase()).isPresent();
            System.out.println("   Email exists check: " + email + " = " + exists);
            return exists;
        } catch (Exception e) {
            System.err.println("   Error checking email exists: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}