package pl.dolien.shop.auth.userProfile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserProfileDTO extends RegistrationDTO {
}
