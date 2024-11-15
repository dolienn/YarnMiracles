package pl.dolien.shop.product;

import pl.dolien.shop.feedback.Feedback;

import java.util.Set;

public class RatingCalculator {
    public static double calculateRating(Set<Feedback> feedbacks) {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        return Math.round(feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0) * 10.0) / 10.0; // Rounded to one decimal place
    }
}
