package uk.ac.ucl.reviewify.azuresentanalysis.types.azure;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Immutable
@JsonSerialize(as = ImmutableUnreviewedDocument.class)
@JsonDeserialize(as = ImmutableUnreviewedDocument.class)
public interface UnreviewedDocument {

    String getLanguage();

    int getId();

    String getText();

}
