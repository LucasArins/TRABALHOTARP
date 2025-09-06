package com.example.authservice.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.application.ports.TokenService;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.RefreshToken;
import com.example.authservice.domain.user.RefreshTokenRepository;
import com.example.authservice.domain.user.vo.TokenHash;
import com.example.authservice.infrastructure.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {
    private final JwtProperties props;
    private final RefreshTokenRepository refreshRepo;


    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateRawRefreshToken() {
        byte[] bytes = new byte[64];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Base64Url(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Hash error", e);
        }
    }

    @Override
    @Transactional
    public TokenPair issue(User user) {
        if (props.getSecret() == null || props.getSecret().isBlank()) {
            throw new IllegalStateException("Secret is mandatory (jwt.secret)");
        }

        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(props.getSecret().getBytes(StandardCharsets.UTF_8));

        Instant accessExpires = now.plusSeconds(props.getAccessTtlSeconds());
        String accessToken = JWT.create()
                .withIssuer(props.getIssuer())
                .withAudience(props.getAudience())
                .withSubject(user.getId().toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(accessExpires))
                .withClaim("type", "access")
                .withClaim("email", user.getEmail().getValue())
                .withClaim("role", user.getRole().getValue().name())
                .withClaim("level", user.getRole().getValue().getLevel())
                .sign(algorithm);

        String rawRefresh = generateRawRefreshToken();
        String hash = sha256Base64Url(rawRefresh);

        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setTokenHash(TokenHash.of(hash));
        rt.setExpiresAt(now.plusSeconds(props.getRefreshTtlSeconds()));
        refreshRepo.save(rt);

        return new TokenPair(accessToken, rawRefresh, (int) props.getAccessTtlSeconds());
    }
}
