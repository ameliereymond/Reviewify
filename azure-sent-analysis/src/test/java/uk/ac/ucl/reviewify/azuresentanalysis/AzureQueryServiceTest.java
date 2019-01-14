package uk.ac.ucl.reviewify.azuresentanalysis;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ucl.reviewify.azuresentanalysis.types.ImmutableReviewDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocumentAnalysis;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AzureQueryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureQueryServiceTest.class);

    @Autowired
    private AzureQueryService queryService;

    @Test
    public void queryAzureFor() {
        ReviewDocument testDocument = ImmutableReviewDocument.builder().id(1).language("fr").text("C'est un test.").build();
        final List<ReviewDocumentAnalysis> queryResult = queryService.queryAzureFor(Collections.singletonList(testDocument));
        LOGGER.info("Result : {}", queryResult);
    }

}