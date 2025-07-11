package sync.fit.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiAcademiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAcademiaApplication.class, args);
	}

}
