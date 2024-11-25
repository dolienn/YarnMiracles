package pl.dolien.shop.auth.registration.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    @NotEmpty(message = "Firstname field cannot be empty")
    @NotBlank(message = "Firstname field cannot be empty")
    private String firstname;

    @NotEmpty(message = "Lastname field cannot be empty")
    @NotBlank(message = "Lastname field cannot be empty")
    private String lastname;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email is not formatted")
    @NotEmpty(message = "Email field cannot be empty")
    @NotBlank(message = "Email field cannot be empty")
    private String email;

    @NotNull(message = "Date of birth field cannot be empty")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Password field cannot be empty")
    @NotBlank(message = "Password field cannot be empty")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
