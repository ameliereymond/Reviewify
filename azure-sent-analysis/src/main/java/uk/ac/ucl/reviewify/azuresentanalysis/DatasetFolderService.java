package uk.ac.ucl.reviewify.azuresentanalysis;

import static java.nio.file.Files.newDirectoryStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DatasetFolderService {

    private final Environment environment;

    public DatasetFolderService(Environment environment) {
        this.environment = environment;
    }

    List<Path> findDatasets() throws IOException {
        final Path datasetsFolder = getDatasetFolder();

        try (DirectoryStream<Path> datasetFolderElementsStream = newDirectoryStream(datasetsFolder)) {
            final Spliterator<Path> childrenSpliterator = datasetFolderElementsStream.spliterator();
            return StreamSupport
                    .stream(childrenSpliterator, false)
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(file -> file.getName().endsWith(".tsv"))
                    .map(File::toPath)
                    .collect(Collectors.toList());
        }
    }

    Path getDatasetFolder() {
        final String sentAnalysisFolderStr = environment.getProperty("azure_sent_analysis_folder");
        final File sentAnalysisFolder = new File(Objects.requireNonNull(sentAnalysisFolderStr));
        final File mainProjectFolder = new File(sentAnalysisFolder, "..");
        return new File(mainProjectFolder, "data").toPath();
    }

}
