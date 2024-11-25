package pl.dolien.shop.email.activationAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.dolien.shop.email.EmailTemplateName;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivationMessageDTO {
    private String to;
    private String subject;
    private String text;
    private String username;
    private EmailTemplateName emailTemplate;
    private String confirmationUrl;
    private String activationCode;
}
