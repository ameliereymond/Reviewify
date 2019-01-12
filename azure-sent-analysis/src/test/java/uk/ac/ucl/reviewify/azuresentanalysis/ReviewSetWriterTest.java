package uk.ac.ucl.reviewify.azuresentanalysis;

import java.io.IOException;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ucl.reviewify.azuresentanalysis.types.full.AnalyzedReview;
import uk.ac.ucl.reviewify.azuresentanalysis.types.full.ImmutableAnalyzedReview;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewSetWriterTest {

    @Autowired
    private ReviewSetWriter reviewSetWriter;

    @Test
    public void writeReviewSet() throws IOException {

        final AnalyzedReview sampleReview = ImmutableAnalyzedReview
                .builder()
                .marketplace("FR")
                .customerId("CID")
                .reviewId("RID")
                .productId("PID")
                .productParent("PP")
                .productTitle("PTITLE")
                .productCategory("PCAT")
                .starRating("SR")
                .helpfulVotes("HV")
                .totalVotes("TV")
                .verifiedPurchase("VP")
                .reviewHeadline("RH")
                .reviewBody("RB(aéioü)")
                .reviewDate("RD")
                .sentimentAnalysis(0.555d)
                .build();

        reviewSetWriter.writeReviewSet("test.tsv", Collections.singletonList(sampleReview));
    }

}