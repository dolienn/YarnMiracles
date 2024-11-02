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
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String from;

    @NotEmpty(message = "Subject is mandatory")
    @NotBlank(message = "Subject is mandatory")
    private String subject;

    @NotEmpty(message = "Message is mandatory")
    @NotBlank(message = "Message is mandatory")
    private String text;

}
