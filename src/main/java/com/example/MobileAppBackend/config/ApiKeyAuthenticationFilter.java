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
    private final RedisRateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String apiKey = request.getHeader("X-API-Key");
        String origin = request.getHeader("Origin");
        String signature = request.getHeader("X-Signature");
        String nonce = request.getHeader("X-Nonce");
        String timestamp = request.getHeader("X-Timestamp");



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
                    String dataToSign = apiKey + "\n" + timestamp + "\n" + nonce + "\n" + request.getMethod() + "\n" + request.getRequestURI();
                    String expectedSignature = HmacUtil.sign(user.getApiSecret(), dataToSign);


                    System.out.println("---- HMAC DEBUG ----");
                    System.out.println("API KEY: " + apiKey);
                    System.out.println("TIMESTAMP: " + timestamp);
                    System.out.println("NONCE: " + nonce);
                    System.out.println("METHOD: " + request.getMethod());
                    System.out.println("PATH: " + request.getRequestURI());
                    System.out.println("DATA TO SIGN:\n" + dataToSign);
                    System.out.println("EXPECTED SIGNATURE: " + expectedSignature);
                    System.out.println("RECEIVED SIGNATURE: " + signature);
                    System.out.println("--------------------");
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

                    // Redis rate-limiting
                    // In filter method
                    if (!rateLimiter.allowRequest(apiKey)) {
                        reject(response,429, "Too many requests");
                        return;
                    }
                    // X-signature check
                    if (signature == null || timestamp == null || nonce == null) {
                        reject(response,401, "Missing HMAC headers");
                        return;
                    }

                    long ts = Long.parseLong(timestamp);
                    if (Math.abs(System.currentTimeMillis() - ts) > 2 * 60 * 1000) {
                        reject(response,401, "Request too old");
                        return;
                    }

                    if (!NonceUtil.checkNonce(apiKey, nonce)) {
                        reject(response, 401, "Replay attack detected");
                        return;
                    }



                    if (!expectedSignature.equals(signature)) {
                        reject(response,401, "Invalid signature");
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


