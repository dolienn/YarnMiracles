package pl.dolien.shop.auth.login;

import pl.dolien.shop.auth.login.dto.LoginRequestDTO;
import pl.dolien.shop.auth.login.dto.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO request);
}

