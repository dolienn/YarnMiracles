package pl.dolien.shop.email.support;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupportMessageDTO {

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email is not formatted")
    @NotEmpty(message = "Email field cannot be empty")
    @NotBlank(message = "Email field cannot be empty")
    private String from;

    @NotEmpty(message = "Subject field cannot be empty")
    @NotBlank(message = "Subject field cannot be empty")
    private String subject;

    @NotEmpty(message = "Message field cannot be empty")
    @NotBlank(message = "Message field cannot be empty")
    private String text;

}
