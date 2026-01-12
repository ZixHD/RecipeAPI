package com.example.MobileAppBackend.config;

import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.model.UserType;
import com.example.MobileAppBackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String apiKey = request.getHeader("X-API-Key");
        String origin = request.getHeader("Origin");
        String signature = request.getHeader("X-Signature");
        if (apiKey != null && !apiKey.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<User> userOptional = userRepository.findByApiKeyAndApiKeyActiveTrue(apiKey);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getUserType() == UserType.CLIENT) {

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                            "ROLE_" + user.getUserType().name()
                    );

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singletonList(authority)
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Origin check
                    if(origin != null && !origin.isBlank()){
                        if(user.getAllowedDomains() == null){
                            user.setAllowedDomains(origin);
                            userRepository.save(user);

                        }
                        else if(!user.getAllowedDomains().equalsIgnoreCase(origin)){
                            reject(response, 401, "Domain not allowed");
                            return;
                        }


                    }

                    // X-signature check
                    if (signature == null || signature.isBlank()) {
                        reject(response, 401, "Missing X-signature header");
                        return;
                    }
                    String expectedSignature = user.getApiSecret();

                    if (!expectedSignature.equals(signature)) {
                        reject(response, 401, "Invalid signature");
                        return;
                    }

                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void reject(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
        response.getWriter().flush();
    }
}


