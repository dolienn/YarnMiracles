package pl.dolien.shop.email.activationAccount;

import lombok.Builder;
import lombok.Getter;
import pl.dolien.shop.email.EmailTemplateName;

@Getter
@Builder
public class AccountActivationMessageDTO {
    private String to;
    private String subject;
    private String text;
    private String username;
    private EmailTemplateName emailTemplate;
    private String confirmationUrl;
    private String activationCode;
}
