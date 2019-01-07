package uk.ac.ucl.reviewify.azuresentanalysis.types;

public final class ReviewDocumentAnalysis {

    private final double score;
    private final int id;

    public ReviewDocumentAnalysis(double score, int id) {
        this.score = score;
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
