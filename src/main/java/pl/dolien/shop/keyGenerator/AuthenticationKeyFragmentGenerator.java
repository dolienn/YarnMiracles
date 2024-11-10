package pl.dolien.shop.keyGenerator;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dolien.shop.user.User;

@Component
public class AuthenticationKeyFragmentGenerator implements KeyFragmentGenerator {

    @Override
    public void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        if (param instanceof Authentication authentication) {
            appendAuthenticationKeyFragment(keyBuilder, authentication);
        }
    }

    private void appendAuthenticationKeyFragment(StringBuilder keyBuilder, Authentication authentication) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            keyBuilder.append("_userId:").append(user.getId());
        } else {
            keyBuilder.append("_userId:anonymous");
        }
    }
}
