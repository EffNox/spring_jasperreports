package nr.spring_reports.clients;

import java.util.List;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {

    @GetExchange("users")
    List<UserPlaceholder> allUsers();

    record UserPlaceholder(
            int id,
            String name,
            String username,
            String email,
            Address address,
            String phone,
            String website
    // Company company
    ) {}

    record Address(String street, String suite, String city, String zipcode, Geo geo) {}

    record Geo(String lat, String lng) {}

    record Company(String name, String catchPhrase, String bs) {}
}
