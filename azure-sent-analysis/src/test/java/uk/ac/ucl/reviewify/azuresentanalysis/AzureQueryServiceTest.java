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

import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.ImmutableUnreviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.ReviewedDocument;
import uk.ac.ucl.reviewify.azuresentanalysis.types.azure.UnreviewedDocument;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AzureQueryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureQueryServiceTest.class);

    @Autowired
    private AzureQueryService queryService;

    @Test
    public void queryAzureFor() {
        UnreviewedDocument testDocument = ImmutableUnreviewedDocument.builder().id(1).language("fr").text("C'est un test.").build();
        final List<ReviewedDocument> queryResult = queryService.queryAzureFor(Collections.singletonList(testDocument));
        LOGGER.info("Result : {}", queryResult);
    }

}