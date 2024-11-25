package pl.dolien.shop.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean accountLocked;
    private LocalDate dateOfBirth;
}

