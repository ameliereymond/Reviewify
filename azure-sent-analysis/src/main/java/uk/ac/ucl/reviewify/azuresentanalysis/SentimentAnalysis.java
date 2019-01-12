package uk.ac.ucl.reviewify.azuresentanalysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.ImmutableUnreviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.ReviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.UnreviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.NonAnalyzedReview;

@Component
public class SentimentAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentimentAnalysis.class);

    private static final Scanner INPUT_QUERY = new Scanner(System.in);

    private final DatasetFolderService datasetFolderService;
    private final ReviewSetReader reviewSetReader;
    private final AzureQueryService azureQueryService;

    public SentimentAnalysis(
            DatasetFolderService datasetFolderService,
            ReviewSetReader reviewSetReader,
            AzureQueryService azureQueryService
    ) {
        this.datasetFolderService = datasetFolderService;
        this.reviewSetReader = reviewSetReader;
        this.azureQueryService = azureQueryService;
    }

    void analyzeDatasets() throws IOException {
        LOGGER.info("Starting analysis!");

        final List<Path> datasets = datasetFolderService.findDatasets();
        LOGGER.info("Found the following datasets : {}", datasets);

        for (Path dataset : datasets) {
            analyzeDatasetAtPath(dataset);
        }

        LOGGER.info("Finished analyzing datasets!");
    }

    private void analyzeDatasetAtPath(Path dataset) throws IOException {
        LOGGER.info("Dataset at {}", dataset);

        final List<NonAnalyzedReview> toAnalyze = reviewSetReader.readSet(dataset);
        final String marketplace = toAnalyze.get(0).getMarketplace();

        LOGGER.info("\t -> Marketplace is : {}", marketplace);
        LOGGER.info("\t -> Review count : {}", toAnalyze.size());

        final List<UnreviewedDocument> azureInputDocuments = IntStream
                .range(0, toAnalyze.size())
                .mapToObj(id -> convertToAzure(id, toAnalyze.get(id)))
                .collect(Collectors.toList());
        LOGGER.info("\t -> Formatted {} reviews for Azure analysis", azureInputDocuments.size());

        requestUserConfirmation("Confirm querying Azure");

        LOGGER.info("\t -> User confirmed Azure query. Starting...");
        final List<ReviewedDocument> reviewedDocuments = azureQueryService.queryAzureFor(azureInputDocuments);

        LOGGER.info("\t -> Azure query done! Will import sentiment analysis results in dataset");
    }

    private void requestUserConfirmation(final String message) {
        LOGGER.info("\t -> {} (y/n) :", message);

        String userInput;
        do {
            userInput = INPUT_QUERY.next();
        } while (!"y".equals(userInput));
    }

    private UnreviewedDocument convertToAzure(final int id, final NonAnalyzedReview review) {
        return ImmutableUnreviewedDocument.builder().id(id).language(review.getMarketplace()).text(review.getReviewBody()).build();
    }

}
