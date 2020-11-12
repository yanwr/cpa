package br.com.softplan.security;

import br.com.softplan.models.dto.CredentialDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTBuilder jwtBuilder;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTBuilder jwtBuilder) {
        this.authenticationManager = authenticationManager;
        this.jwtBuilder = jwtBuilder;
    }

    @Override
    public Authentication attemptAuthentication
        (HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            CredentialDTO userCredential = new ObjectMapper().readValue(req.getInputStream(), CredentialDTO.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userCredential.getEmail(), userCredential.getPassword(), new ArrayList<>());
            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication (HttpServletRequest req, HttpServletResponse res,
     FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserSS user = ((UserSS) authentication.getPrincipal());
        String token = jwtBuilder.generateToken(user);
        res.addHeader("Authorization", "Bearer " + token);
        res.setHeader("Access-Control-Expose-Headers", "Authorization");
    }
}