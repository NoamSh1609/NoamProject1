package com.noam.noamproject1.models;

import java.util.Date;

public class Review {
    private String reviewId;
    private String userId;
    private String reviewText;
    private float rating;
    private Date reviewDate;

    // Constructor
    public Review(String reviewId, String userId, String reviewText, float rating, Date reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    // Getters
    public String getReviewId() {
        return reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    // Setters
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", userId='" + userId + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
