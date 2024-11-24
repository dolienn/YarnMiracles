package pl.dolien.shop.auth.registration;

import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.user.User;

import javax.management.relation.RoleNotFoundException;

public interface RegistrationService {

    User registerUser(RegistrationDTO dto) throws RoleNotFoundException;
}

