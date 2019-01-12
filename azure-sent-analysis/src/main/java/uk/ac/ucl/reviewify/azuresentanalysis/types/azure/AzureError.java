package uk.ac.ucl.reviewify.azuresentanalysis.types.azure;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonDeserialize(as = ImmutableAzureError.class)
@JsonSerialize(as = ImmutableAzureError.class)
public interface AzureError {

    int getId();

    String getMessage();

}
