package uk.ac.ucl.reviewify.azuresentanalysis;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.ucl.reviewify.azuresentanalysis.types.full.AnalyzedReview;

@Component
public class ReviewSetWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewSetWriter.class);

    private static final String HEADER = "marketplace\tcustomer_id\treview_id\tproduct_id\tproduct_parent\tproduct_title\tproduct_category\tstar_rating"
                                         + "\thelpful_votes\ttotal_votes\tverified_purchase\treview_headline\treview_body\treview_date\tsentiment_score\n";

    private final DatasetFolderService datasetFolderService;

    public ReviewSetWriter(DatasetFolderService datasetFolderService) {
        this.datasetFolderService = datasetFolderService;
    }

    void writeReviewSet(final String datasetFileName, List<AnalyzedReview> analyzedReviews) throws IOException {
        final Path datasetFolder = datasetFolderService.getDatasetFolder();
        final Path outputFile = datasetFolder.resolve(datasetFileName);
        Files.deleteIfExists(outputFile);
        Files.createFile(outputFile);

        try (BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(outputFile, CREATE))) {
            output.write(HEADER.getBytes(UTF_8));
            for (AnalyzedReview analyzedReview : analyzedReviews) {
                final String reviewLine = tabifyReview(analyzedReview) + "\n";
                output.write(reviewLine.getBytes(UTF_8));
            }
        }

        LOGGER.info("Wrote set to : {}", outputFile.toAbsolutePath().toString());
    }

    private String tabifyReview(final AnalyzedReview review) {
        final List<String> parts = new ArrayList<>(15);

        parts.add(review.getMarketplace());
        parts.add(review.getCustomerId());
        parts.add(review.getReviewId());
        parts.add(review.getProductId());
        parts.add(review.getProductParent());
        parts.add(review.getProductTitle());
        parts.add(review.getProductCategory());
        parts.add(review.getStarRating());
        parts.add(review.getHelpfulVotes());
        parts.add(review.getTotalVotes());
        parts.add(review.getVerifiedPurchase());
        parts.add(review.getReviewHeadline());
        parts.add(review.getReviewBody());
        parts.add(review.getReviewDate());
        parts.add(Double.toString(review.getSentimentAnalysis()));

        return String.join("\t", parts);
    }

}
