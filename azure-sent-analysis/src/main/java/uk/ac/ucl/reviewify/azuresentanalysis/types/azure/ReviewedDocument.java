package uk.ac.ucl.reviewify.azuresentanalysis.types.azure;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize(as = ImmutableReviewedDocument.class)
@JsonDeserialize(as = ImmutableReviewedDocument.class)
public interface ReviewedDocument {

    double getScore();

    int getId();

}
