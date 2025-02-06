package app.rafo.bs_personal_finance_management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Service for generating and validating JWT tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;


    /**
     * Generates a JWT token for a given user.
     * @param userDetails The authenticated user's details.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // 🔹 Agregar "ROLE_" a los roles antes de guardarlos en el JWT
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(auth -> "ROLE_" + auth.getAuthority()) // 🔥 Agregar "ROLE_" aquí
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
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

    /**
     * Extracts roles from the JWT token.
     * @param token The JWT token.
     * @return A list of GrantedAuthority roles.
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);

        // 🔹 Convertir los roles en SimpleGrantedAuthority sin agregar "ROLE_" nuevamente
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role)) // 🔥 No agregar "ROLE_" aquí
                .collect(Collectors.toList());
    }

}