package pl.dolien.shop.product;

import org.springframework.stereotype.Component;
import pl.dolien.shop.feedback.Feedback;

import java.util.List;

@Component
public class RatingCalculator {
    public static double calculateRate(List<Feedback> feedbacks) {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        return Math.round(feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0) * 10.0) / 10.0; // Rounded to one decimal place
    }
}
