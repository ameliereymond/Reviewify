package uk.ac.ucl.reviewify.azuresentanalysis.types.full;

public interface Review {

    String getMarketplace();

    String getCustomerId();

    String getReviewId();

    String getProductId();

    String getProductParent();

    String getProductTitle();

    String getProductCategory();

    String getStarRating();

    String getHelpfulVotes();

    String getTotalVotes();

    String getVerifiedPurchase();

    String getReviewHeadline();

    String getReviewBody();

    String getReviewDate();

}
