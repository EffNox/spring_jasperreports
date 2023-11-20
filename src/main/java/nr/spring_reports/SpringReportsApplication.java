package nr.spring_reports;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import nr.spring_reports.clients.UserClient;

@SpringBootApplication
public class SpringReportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReportsApplication.class, args);
	}

	@Bean
	UserClient client(WebClient.Builder builder) {
		var webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();

		return HttpServiceProxyFactory
				.builder(WebClientAdapter.forClient(webClient))
				.build()
				.createClient(UserClient.class);
	}

	// @Bean
	CommandLineRunner init(UserClient userClient) {
		return args -> {
			userClient
					.allUsers()
					.forEach(System.out::println);
		};
	}

}
