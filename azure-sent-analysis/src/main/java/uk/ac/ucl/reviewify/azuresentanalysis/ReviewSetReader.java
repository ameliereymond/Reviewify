package uk.ac.ucl.reviewify.azuresentanalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import uk.ac.ucl.reviewify.azuresentanalysis.types.full.ImmutableNonAnalyzedReview;
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.NonAnalyzedReview;

@Component
public class ReviewSetReader {

    List<NonAnalyzedReview> readSet(final Path setPath) throws IOException {
        return Files.lines(setPath).skip(1).map(line -> {
            final String[] lineArr = line.split("\t");
            return ImmutableNonAnalyzedReview
                    .builder()
                    .marketplace(lineArr[0])
                    .customerId(lineArr[1])
                    .reviewId(lineArr[2])
                    .productId(lineArr[3])
                    .productParent(lineArr[4])
                    .productTitle(lineArr[5])
                    .productCategory(lineArr[6])
                    .starRating(lineArr[7])
                    .helpfulVotes(lineArr[8])
                    .totalVotes(lineArr[9])
                    .verifiedPurchase(lineArr[11])
                    .reviewHeadline(lineArr[12])
                    .reviewBody(String.format("%.4000s", lineArr[13]))
                    .reviewDate(lineArr[14])
                    .build();
        }).collect(Collectors.toList());
    }

}
