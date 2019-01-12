package uk.ac.ucl.reviewify.azuresentanalysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DatasetFolderServiceTest {

    @Autowired
    private DatasetFolderService datasetFolderService;

    @Test
    public void findDatasets() throws IOException {
        final List<Path> foundDatasets = datasetFolderService.findDatasets();
        assertThat(foundDatasets).hasSize(5);
    }

}
