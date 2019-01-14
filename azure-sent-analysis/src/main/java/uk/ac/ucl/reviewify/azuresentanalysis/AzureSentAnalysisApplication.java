package uk.ac.ucl.reviewify.azuresentanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
=======
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
>>>>>>> f446316... Add java small app to reduce sets because python sucks

@SpringBootApplication
public class AzureSentAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzureSentAnalysisApplication.class, args);
	}

<<<<<<< HEAD
}

=======
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
>>>>>>> f446316... Add java small app to reduce sets because python sucks
