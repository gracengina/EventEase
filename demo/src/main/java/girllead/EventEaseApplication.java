package girllead;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EventEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventEaseApplication.class, args);
        System.out.println(" Event Planning Backend Started Successfully!");
        System.out.println(" API Base URL: http://localhost:8080/api");
        System.out.println("Auth Endpoints: /auth/register, /auth/login");
        System.out.println("Vendor Endpoints: /vendors/**");
        System.out.println("Booking Endpoints: /bookings/**");
    }}
