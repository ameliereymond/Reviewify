package uk.ac.ucl.reviewify.azuresentanalysis.types.azure;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize(as = ImmutableAzureReply.class)
@JsonDeserialize(as = ImmutableAzureReply.class)
public interface AzureReply {

    @Default
    default List<ReviewedDocument> getDocuments() {
        return Collections.emptyList();
    }

    Optional<List<AzureError>> getErrors();

}
