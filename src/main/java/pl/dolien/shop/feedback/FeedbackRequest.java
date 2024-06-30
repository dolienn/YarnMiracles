package pl.dolien.shop.feedback;

import jakarta.validation.constraints.*;
import pl.dolien.shop.user.User;

public record FeedbackRequest(
        @Positive(message = "200")
                @Min(value = 0,message = "201")
                @Max(value = 5,message = "202")
        Double note,
        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,
        @NotNull(message = "204")
        Long productId
) {
}
