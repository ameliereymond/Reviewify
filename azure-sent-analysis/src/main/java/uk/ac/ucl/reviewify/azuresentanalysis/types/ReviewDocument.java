package uk.ac.ucl.reviewify.azuresentanalysis.types;

public final class ReviewDocument {
    private final String language;
    private final int id;
    private final String text;

    public ReviewDocument(String language, int id, String text) {
        this.language = language;
        this.id = id;
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
