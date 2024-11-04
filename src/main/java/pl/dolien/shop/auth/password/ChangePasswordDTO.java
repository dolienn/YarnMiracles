package pl.dolien.shop.auth.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordDTO {

    @NotEmpty(message = "Your password is mandatory")
    @NotBlank(message = "Your password is mandatory")
    private String yourPassword;

    @NotEmpty(message = "New password is mandatory")
    @NotBlank(message = "New password is mandatory")
    @Size(min = 8, message = "New password should be 8 characters long minimum")
    private String newPassword;
}
