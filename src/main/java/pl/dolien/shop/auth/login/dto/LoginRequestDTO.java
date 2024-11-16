package pl.dolien.shop.auth.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequestDTO {

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email is not formatted")
    @NotEmpty(message = "Email field cannot be empty")
    @NotBlank(message = "Email field cannot be empty")
    private String email;

    @NotEmpty(message = "Password field cannot be empty")
    @NotBlank(message = "Password field cannot be empty")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
