package by.itstep.organizaer.service;

import by.itstep.organizaer.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            String login = token.getPrincipal().toString();
            User fromDb = userService.findByLogin(login).orElseThrow();
            // ЕЩЕ что-то проверить. права или еще что-то
            return new UsernamePasswordAuthenticationToken(fromDb, null, null);
        }
        throw new AuthenticationException("Not authorized") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }
}
