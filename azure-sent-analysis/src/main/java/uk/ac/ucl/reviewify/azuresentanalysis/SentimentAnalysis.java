package uk.ac.ucl.reviewify.azuresentanalysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SentimentAnalysis {

    private final DatasetFolderService datasetFolderService;

    public SentimentAnalysis(DatasetFolderService datasetFolderService) {
        this.datasetFolderService = datasetFolderService;
    }

    public void analyzeDatasets() throws IOException {
        final List<Path> datasets = datasetFolderService.findDatasets();
    }

}
