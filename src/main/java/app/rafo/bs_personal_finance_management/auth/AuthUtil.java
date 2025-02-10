package app.rafo.bs_personal_finance_management.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public static String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
