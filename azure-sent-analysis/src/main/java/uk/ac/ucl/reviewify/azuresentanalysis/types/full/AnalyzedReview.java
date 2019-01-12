package uk.ac.ucl.reviewify.azuresentanalysis.types.full;

import org.immutables.value.Value.Immutable;

@Immutable
public interface AnalyzedReview extends Review {

    double getSentimentAnalysis();

}
