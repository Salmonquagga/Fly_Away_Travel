package org.example.flyawayapi.auth.application;

import org.example.flyawayapi.user.domain.User;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {

    private static final String SECRET = "fly-away-secret-key";

    public String generateToken(User user) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        long now = Instant.now().getEpochSecond();
        long exp = now + 86400;

        String payload = "{"
                + "\"sub\":\"" + user.getId() + "\","
                + "\"email\":\"" + user.getEmail() + "\","
                + "\"firstName\":\"" + user.getFirstName() + "\","
                + "\"lastName\":\"" + user.getLastName() + "\","
                + "\"iat\":" + now + ","
                + "\"exp\":" + exp
                + "}";

        String encodedHeader = base64Url(header);
        String encodedPayload = base64Url(payload);

        String signature = hmacSha256(encodedHeader + "." + encodedPayload);

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    SECRET.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(secretKey);

            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(signature);

        } catch (Exception e) {
            throw new RuntimeException("Could not generate JWT");
        }
    }
}