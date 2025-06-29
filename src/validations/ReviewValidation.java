package validations;

import models.Review;

public class ReviewValidation {

    public static void isReviewValid(Review review) {
        if (!isBookingIdValid(review.getBooking())) {
            throw new IllegalArgumentException("Booking ID must be a positive integer.");
        }
        if (!isStarValid(review.getStar())) {
            throw new IllegalArgumentException("Star rating must be between 1 and 5.");
        }
        if (!isTitleValid(review.getTitle())) {
            throw new IllegalArgumentException("Title must not exceed 100 characters.");
        }
        if (!isContentValid(review.getContent())) {
            throw new IllegalArgumentException("Content must not exceed 500 characters.");
        }
    }

    public static boolean isBookingIdValid(int bookingId) {
        return bookingId > 0;
    }

    public static boolean isStarValid(int star) {
        return star >= 1 && star <= 5;
    }

    public static boolean isTitleValid(String title) {
        return title == null || title.length() <= 100;
    }

    public static boolean isContentValid(String content) {
        return content == null || content.length() <= 500;
    }
}
