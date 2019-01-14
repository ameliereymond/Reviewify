package uk.ac.ucl.reviewify.azuresentanalysis.types;

import java.util.List;
import java.util.Optional;

public final class AzureApiWrapper<T> {

    private final List<T> documents;

    private Optional<?> errors;

    public AzureApiWrapper(List<T> documents) {
        this.documents = documents;
        this.errors = Optional.empty();
    }

    public List<T> getDocuments() {
        return documents;
    }

    public Optional<?> getErrors() {
        return errors;
    }

    public void setErrors(Optional<?> errors) {
        this.errors = errors;
    }
}
