package pl.dolien.shop.email.activationAccount;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pl.dolien.shop.email.EmailTemplateName;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class AccountActivationEmailService {

    private static final String DEFAULT_TEMPLATE = "confirm-email";

    @Value("${support.email}")
    private final String supportEmail;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendActivationEmail(AccountActivationMessageDTO accountActivationMessageDTO) throws MessagingException {
        MimeMessage mimeMessage = createMimeMessage(accountActivationMessageDTO);
        mailSender.send(mimeMessage);
    }

    private MimeMessage createMimeMessage(AccountActivationMessageDTO accountActivationMessageDTO) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = initializeMimeMessageHelper(mimeMessage, accountActivationMessageDTO);

        String templateName = getTemplateName(accountActivationMessageDTO.getEmailTemplate());
        String emailContent = buildEmailContent(templateName, accountActivationMessageDTO);

        helper.setText(emailContent, true);
        return mimeMessage;
    }

    private MimeMessageHelper initializeMimeMessageHelper(MimeMessage mimeMessage, AccountActivationMessageDTO accountActivationMessageDTO) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        helper.setFrom(supportEmail);
        helper.setTo(accountActivationMessageDTO.getTo());
        helper.setSubject(accountActivationMessageDTO.getSubject());
        return helper;
    }

    private String getTemplateName(EmailTemplateName emailTemplate) {
        return emailTemplate != null ? emailTemplate.name() : DEFAULT_TEMPLATE;
    }

    private String buildEmailContent(String templateName, AccountActivationMessageDTO accountActivationMessageDTO) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", accountActivationMessageDTO.getUsername());
        properties.put("confirmationUrl", accountActivationMessageDTO.getConfirmationUrl());
        properties.put("activation_code", accountActivationMessageDTO.getActivationCode());

        Context context = new Context();
        context.setVariables(properties);
        return templateEngine.process(templateName, context);
    }
}
