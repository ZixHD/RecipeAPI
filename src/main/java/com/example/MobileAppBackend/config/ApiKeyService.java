package com.example.MobileAppBackend.config;


import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.repository.UserRepository;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.UUID;

@Service
public class ApiKeyService {

    private static final SecureRandom  secureRandom = new SecureRandom();

    public String generateApiKey(){
        return UUID.randomUUID().toString();
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"), secureRandom); // P-256 curve
        return keyGen.generateKeyPair();
    }

    public static String privateKeyToPem(PrivateKey privateKey) {
        String base64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return "-----BEGIN PRIVATE KEY-----\n" +
                chunkString(base64, 64) +
                "\n-----END PRIVATE KEY-----";
    }

    public static String publicKeyToPem(PublicKey publicKey) {
        String base64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" +
                chunkString(base64, 64) +
                "\n-----END PUBLIC KEY-----";
    }

    private static String chunkString(String str, int chunkSize) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < str.length()) {
            sb.append(str, index, Math.min(index + chunkSize, str.length()));
            sb.append("\n");
            index += chunkSize;
        }
        return sb.toString().trim();
    }

    public static String generateClientPrivatePublicKey(User user, UserRepository userRepository){
        try {
            KeyPair pair = generateKeyPair();
            String privatePem = privateKeyToPem(pair.getPrivate());
            String publicPem = publicKeyToPem(pair.getPublic());

            user.setPublicKey(publicPem);

            return privatePem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
