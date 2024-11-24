package pl.dolien.shop.email.activationAccount;

import jakarta.mail.MessagingException;

public interface AccountActivationEmailService {

    void sendActivationEmail(AccountActivationMessageDTO accountActivationMessageDTO) throws MessagingException;
}
