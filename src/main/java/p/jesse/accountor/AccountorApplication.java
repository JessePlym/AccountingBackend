package p.jesse.accountor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import p.jesse.accountor.utils.CreateUser;

@SpringBootApplication
public class AccountorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountorApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CreateUser createUser) {
		return args -> {
			createUser.createTestUsers();
		};
	}
}
