package uk.ac.ucl.reviewify.azuresentanalysis;

import static org.springframework.boot.SpringApplication.run;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AzureSentAnalysisApplication {

    private final QueryInterceptor interceptor;

    public AzureSentAnalysisApplication(QueryInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public static void main(String[] args) throws IOException {
        run(AzureSentAnalysisApplication.class, args)
                .getBean(SentimentAnalysisPipeline.class)
                .analyzeDatasets();
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }

}
