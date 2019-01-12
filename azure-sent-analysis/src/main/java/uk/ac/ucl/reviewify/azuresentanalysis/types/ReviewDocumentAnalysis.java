package uk.ac.ucl.reviewify.azuresentanalysis.types;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize(as = ImmutableReviewDocumentAnalysis.class)
@JsonDeserialize(as = ImmutableReviewDocumentAnalysis.class)
public interface ReviewDocumentAnalysis {

    double getScore();

    int getId();

}
