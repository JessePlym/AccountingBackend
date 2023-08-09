package p.jesse.accountor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import p.jesse.accountor.utils.CreateTestData;

@SpringBootApplication
public class AccountorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountorApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner commandLineRunner(CreateTestData createTestData) {
		return args -> {
			createTestData.create();
		};
	}
}
