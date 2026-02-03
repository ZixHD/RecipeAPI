package com.example.MobileAppBackend.config;

import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.model.UserType;
import com.example.MobileAppBackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final RedisRateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String apiKey = request.getHeader("X-API-Key");
//        String origin = request.getHeader("Origin");
        String signature = request.getHeader("X-Signature");
//        String nonce = request.getHeader("X-Nonce");
//        String timestamp = request.getHeader("X-Timestamp");



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
                    String fullPath = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
                    String dataToSign = apiKey + "\n" + request.getMethod() + "\n" + fullPath;



                    System.out.println("---- HMAC DEBUG ----");
                    System.out.println("API KEY: " + apiKey);
                    System.out.println("METHOD: " + request.getMethod());
                    System.out.println("PATH: " + fullPath);
                    System.out.println("DATA TO SIGN:\n" + dataToSign);

                    System.out.println("RECEIVED SIGNATURE: " + signature);
                    System.out.println("--------------------");
                    // Origin check
//                    if(origin != null && !origin.isBlank()){
//                        if(user.getAllowedDomains() == null){
//                            user.setAllowedDomains(origin);
//                            userRepository.save(user);
//
//                        }
//                        else if(!user.getAllowedDomains().equalsIgnoreCase(origin)){
//                            reject(response, 401, "Domain not allowed");
//                            return;
//                        }
//
//
//                    }

                    // Redis rate-limiting
                    // In filter method
//                    if (!rateLimiter.allowRequest(apiKey)) {
//                        reject(response,429, "Too many requests");
//                        return;
//                    }
//                    // X-signature check
//                    if (signature == null ) {
//                        reject(response,401, "Missing HMAC headers");
//                        return;
//                    }
//                    try {
//                        if (!verifySignature(dataToSign, signature, user.getPublicKey())) {
//                            reject(response, 401, "Invalid signature");
//                            log.debug("Date {}",dataToSign);
//                            log.debug("Signature {}",signature);
//                            log.debug("Public key {}",user.getPublicKey());
//                            return;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        reject(response, 500, "Error verifying signature");
//                        return;
//                    }

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

    private boolean verifySignature(String dataToSign, String base64Signature, String publicKeyPem) throws Exception {

        System.out.println("User_public: " + publicKeyPem);
        String pem = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(pem);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(spec);


        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initVerify(publicKey);


        sig.update(dataToSign.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);

        return sig.verify(signatureBytes);
    }
}


