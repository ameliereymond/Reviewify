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
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.AnalyzedReview;
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.ImmutableAnalyzedReview;
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.NonAnalyzedReview;

@Component
public class SentimentAnalysisPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentimentAnalysisPipeline.class);

    private static final Scanner INPUT_QUERY = new Scanner(System.in);

    private final DatasetFolderService datasetFolderService;
    private final ReviewSetReader reviewSetReader;
    private final AzureQueryService azureQueryService;
    private final ReviewSetWriter reviewSetWriter;

    public SentimentAnalysisPipeline(
            DatasetFolderService datasetFolderService,
            ReviewSetReader reviewSetReader,
            AzureQueryService azureQueryService,
            ReviewSetWriter reviewSetWriter
    ) {
        this.datasetFolderService = datasetFolderService;
        this.reviewSetReader = reviewSetReader;
        this.azureQueryService = azureQueryService;
        this.reviewSetWriter = reviewSetWriter;
    }

    void analyzeDatasets() throws IOException {
        LOGGER.info("Starting analysis!");

        final List<Path> datasets = datasetFolderService.findDatasets();
        LOGGER.info("Found the following datasets : {}", datasets);

        int done = 0;
        final int total = datasets.size();

        for (Path dataset : datasets) {
            final List<AnalyzedReview> analyzedReviews = analyzeDatasetAtPath(dataset);

            LOGGER.info("\t -> Will write analyzed dataset in {}", datasetFolderService.getDatasetFolder().toAbsolutePath());

            final String outSetName = analyzedReviews.get(0).getMarketplace() + "_analyzed.tsv";
            final Path outputFile = reviewSetWriter.writeReviewSet(outSetName, analyzedReviews);

            LOGGER.info("\t -> Wrote analyzed dataset in {}", outputFile.toAbsolutePath());
            LOGGER.info("\t -> {}/{} done.", ++done, total);
        }

        LOGGER.info("Finished analyzing and saving datasets!");
    }

    private List<AnalyzedReview> analyzeDatasetAtPath(Path dataset) throws IOException {
        LOGGER.info("Dataset at [{}]", dataset.toAbsolutePath());

        final List<NonAnalyzedReview> datasetReviews = reviewSetReader.readSet(dataset);
        final String marketplace = datasetReviews.get(0).getMarketplace();

        LOGGER.info("\t -> Marketplace is : {}", marketplace);
        LOGGER.info("\t -> Review count : {}", datasetReviews.size());

        final List<UnreviewedDocument> azureInputDocuments = IntStream
                .range(0, datasetReviews.size())
                .mapToObj(id -> convertToAzure(id, datasetReviews.get(id)))
                .collect(Collectors.toList());
        LOGGER.info("\t -> Formatted {} reviews for Azure analysis", azureInputDocuments.size());

        requestUserConfirmation("querying Azure");

        LOGGER.info("\t -> User confirmed Azure query. Starting...");
        final List<ReviewedDocument> azureAnalyzedDocuments = azureQueryService.queryAzureFor(azureInputDocuments);

        LOGGER.info("\t -> Azure query done! Will import sentiment analysis results in dataset");
        final List<AnalyzedReview> analyzedReviews = mergeWithAzure(datasetReviews, azureAnalyzedDocuments);
        LOGGER.info("\t -> Imported azure analysis successfully!");

        return analyzedReviews;
    }

    private void requestUserConfirmation(final String message) {
        LOGGER.info("\t -> Requesting user validation before {} :", message);

        String userInput;
        do {
            System.out.println("Please confirm (y) : ");
            userInput = INPUT_QUERY.next();
        } while (!"y".equals(userInput));
    }

    private UnreviewedDocument convertToAzure(final int id, final NonAnalyzedReview review) {
        String language = review.getMarketplace().toLowerCase();
        if (language.equals("us") || language.equals("uk")) {
            language = "en";
        }

        return ImmutableUnreviewedDocument.builder().id(id).language(language).text(review.getReviewBody()).build();
    }

    private List<AnalyzedReview> mergeWithAzure(final List<NonAnalyzedReview> originalDataset, List<ReviewedDocument> azureAnalyzedDocuments) {
        if (!(azureAnalyzedDocuments.size() == originalDataset.size())) {
            LOGGER.warn(
                    "Disparity between original review count and Azure-analyzed ones! {} vs {}",
                    originalDataset.size(),
                    azureAnalyzedDocuments.size()
            );
        }

        final List<AnalyzedReview> analyzedReviews = azureAnalyzedDocuments.stream().map(azureAnalyzed -> {
            final NonAnalyzedReview mapping = originalDataset.get(azureAnalyzed.getId());
            return ImmutableAnalyzedReview
                    .builder()
                    .from(mapping)
                    .sentimentAnalysis(azureAnalyzed.getScore())
                    .build();
        }).collect(Collectors.toList());

        if (!(analyzedReviews.size() == originalDataset.size())) {
            LOGGER.warn(
                    "Disparity between original review count and merge result with azure-analyzed ones! {} vs {}",
                    originalDataset.size(),
                    azureAnalyzedDocuments.size()
            );
        }

        return analyzedReviews;
    }

}
