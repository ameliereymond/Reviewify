package uk.ac.ucl.reviewify.azuresentanalysis;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocumentAnalysis;

@Component
public class AzureQueryService {

    private static final ParameterizedTypeReference<Map<String, List<ReviewDocument>>> QUERY_TYPE = new ParameterizedTypeReference<>() {
    };
    private static final ParameterizedTypeReference<Map<String, List<ReviewDocumentAnalysis>>> REPLY_TYPE = new ParameterizedTypeReference<>() {
    };

    private static final String BASE = "https://westeurope.api.cognitive.microsoft.com/text/analytics/v2.0";
    private static final String KEY = "6a486812184644159381c49b03c20e5c";
    private static final String KEY_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String ENDPOINT = "/sentiment";

    private final RestTemplate restTemplate;

    @Autowired
    public AzureQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    List<ReviewDocumentAnalysis> queryAzureFor(List<ReviewDocument> documentsToReview) {
        final Map<String, List<ReviewDocument>> docs = new HashMap<>();
        docs.put("documents", documentsToReview);

        final RequestEntity<Map<String, List<ReviewDocument>>> requestData = new RequestEntity<>(
                docs,
                new LinkedMultiValueMap<>() {{
                    put(KEY_HEADER, List.of(KEY));
                }},
                HttpMethod.POST,
                URI.create(BASE + ENDPOINT),
                QUERY_TYPE.getType()
        );

        final ResponseEntity<Map<String, List<ReviewDocumentAnalysis>>> response = restTemplate.exchange(
                requestData,
                REPLY_TYPE
        );

        return Objects.requireNonNull(response.getBody()).get("documents");
    }

}
