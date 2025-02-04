package app.rafo.bs_personal_finance_management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Service for generating and validating JWT tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;


    /**
     * Generates a JWT token for a given email.
     * @param email The email of the authenticated user.
     * @return The generated JWT token.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the token and checks if it belongs to a given email.
     * @param token The JWT token.
     * @param email The user's email.
     * @return True if valid, false otherwise.
     */
    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    /**
     * Extracts the email from the JWT token.
     * @param token The JWT token.
     * @return The extracted email.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token.
     * @param token The JWT token.
     * @param claimsResolver Function to extract a claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     * @param token The JWT token.
     * @return The extracted claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token is expired.
     * @param token The JWT token.
     * @return True if expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Retrieves the signing key for JWT.
     * @return The signing key.
     */
    private Key getSigningKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey.trim());
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT secret key. Ensure it is Base64 encoded.", e);
        }
    }
}