package pl.dolien.shop.auth.password.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordRequestDTO {

    @NotEmpty(message = "Your password field cannot be empty")
    @NotBlank(message = "Your password field cannot be empty")
    private String yourPassword;

    @NotEmpty(message = "New password field cannot be empty")
    @NotBlank(message = "New password field cannot be empty")
    @Size(min = 8, message = "New password should be 8 characters long minimum")
    private String newPassword;
}
