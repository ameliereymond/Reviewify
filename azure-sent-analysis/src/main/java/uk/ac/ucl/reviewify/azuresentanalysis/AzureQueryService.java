package uk.ac.ucl.reviewify.azuresentanalysis;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import uk.ac.ucl.reviewify.azuresentanalysis.types.AzureApiWrapper;
import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocumentAnalysis;

@Component
public class AzureQueryService {

    private static final String BASE = "https://westeurope.api.cognitive.microsoft.com/text/analytics/v2.0";
    private static final String KEY = "6a486812184644159381c49b03c20e5c";
    private static final String KEY_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String ENDPOINT = "/sentiment";

    private final RestTemplate restTemplate;

    @Autowired
    public AzureQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AzureApiWrapper<ReviewDocumentAnalysis> queryAzureFor(List<ReviewDocument> documentsToReview) {
        final AzureApiWrapper<ReviewDocument> body = new AzureApiWrapper<>(documentsToReview);

        RequestEntity<AzureApiWrapper<ReviewDocument>> reqData = new RequestEntity<>(
                body,
                new LinkedMultiValueMap<>() {{
                    put(KEY_HEADER, Collections.singletonList(KEY));
                }},
                HttpMethod.POST,
                URI.create(BASE + ENDPOINT),
                new ParameterizedTypeReference<AzureApiWrapper<ReviewDocumentAnalysis>>() {}.getType()
        );

        final ResponseEntity<AzureApiWrapper<ReviewDocumentAnalysis>> response = restTemplate.exchange(
                reqData,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

}
