package uk.ac.ucl.reviewify.azuresentanalysis.types;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize(as = ImmutableReviewDocument.class)
@JsonDeserialize(as = ImmutableReviewDocument.class)
public interface ReviewDocument {

    String getLanguage();

    int getId();

    String getText();

}
