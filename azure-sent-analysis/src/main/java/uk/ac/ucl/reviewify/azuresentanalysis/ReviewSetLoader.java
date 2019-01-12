package uk.ac.ucl.reviewify.azuresentanalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import uk.ac.ucl.reviewify.azuresentanalysis.types.ReviewDocument;

@Component
public class ReviewSetLoader {

    public List<ReviewDocument> readSet(final Path setPath) throws IOException {
        return Files.lines(setPath).skip(1).map(line -> {
            final String[] lineArr = line.split("\t");
            return new ReviewDocument(lineArr[0], Integer.parseInt(lineArr[2]), lineArr[13]);
        }).collect(Collectors.toList());
    }

}