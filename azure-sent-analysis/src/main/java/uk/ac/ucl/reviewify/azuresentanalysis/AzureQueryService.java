package uk.ac.ucl.reviewify.azuresentanalysis;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.AzureError;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.AzureReply;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.ReviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.UnreviewedDocument;

@Component
public class AzureQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureQueryService.class);

    private static final ParameterizedTypeReference<Map<String, List<UnreviewedDocument>>> QUERY_TYPE = new ParameterizedTypeReference<>() {
    };

    private static final String BASE = "https://westeurope.api.cognitive.microsoft.com/text/analytics/v2.0";
    private static final String KEY = "<PUT KEY HERE>";
    private static final String KEY_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String ENDPOINT = "/sentiment";

    private final RestTemplate restTemplate;

    @Autowired
    public AzureQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    List<ReviewedDocument> queryAzureFor(List<UnreviewedDocument> documentsToReview) {
        if (documentsToReview.size() <= 100) {
            return queryPart(documentsToReview);
        }

        LOGGER.info("Queried analysis of more than 100 documents. Splitting requests by batches of 100.");

        final List<UnreviewedDocument> remaining = new LinkedList<>(documentsToReview);
        final List<List<UnreviewedDocument>> azureInputs = new ArrayList<>(documentsToReview.size() / 100 + 1);

        while (!remaining.isEmpty()) {
            final List<UnreviewedDocument> subset = new ArrayList<>(100);
            for (int i = 0; i < 100; i++) {
                if (!remaining.isEmpty()) {
                    subset.add(remaining.remove(0));
                }
            }
            azureInputs.add(subset);
        }

        LOGGER.info("Will need {} queries to go through whole dataset.", azureInputs.size());

        final List<ReviewedDocument> analyzedDocuments = IntStream.range(0, azureInputs.size()).mapToObj(queryId -> {
            final List<UnreviewedDocument> subsetToReview = azureInputs.get(queryId);
            LOGGER.info("\t -> Query {}/{} for {} documents", queryId, azureInputs.size(), subsetToReview.size());
            return queryPart(subsetToReview);
        }).flatMap(List::stream).collect(Collectors.toList());

        LOGGER.info("Done analyzing {} documents! Result size : {}", documentsToReview.size(), analyzedDocuments.size());

        return analyzedDocuments;
    }

    private List<ReviewedDocument> queryPart(List<UnreviewedDocument> documentsToReview) {
        final Map<String, List<UnreviewedDocument>> docs = new HashMap<>();
        docs.put("documents", documentsToReview);

        final RequestEntity<Map<String, List<UnreviewedDocument>>> requestData = new RequestEntity<>(
                docs,
                new LinkedMultiValueMap<>() {{
                    put(KEY_HEADER, List.of(KEY));
                }},
                HttpMethod.POST,
                URI.create(BASE + ENDPOINT),
                QUERY_TYPE.getType()
        );

        final ResponseEntity<AzureReply> response = restTemplate.exchange(
                requestData,
                AzureReply.class
        );
        final AzureReply reply = response.getBody();

        assert reply != null;

        reply.getErrors().ifPresent(errors -> LOGGER.warn(
                "Errors during analysis! {}",
                errors.stream().map(AzureError::getMessage).collect(Collectors.toSet())
        ));

        return reply.getDocuments();
    }

}
