package de.elliepotato.elliepotatoapi.endpoint.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Service for generating and handling JWTs.
 */
@Service
public class JwtService {

    private static final long TTL = TimeUnit.DAYS.toMillis(3);

    @Value("${auth.jwt.secretKey}")
    private String secretKey;

    /**
     * Extract a field from a JWT.
     *
     * @param token JWT.
     * @param claimsResolver Field mapper.
     * @return The field.
     * @param <T> Expected type.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract the claimed username from a JWT.
     *
     * @param token Token to extract username from.
     * @return Username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Check if a JWT token is valid.
     *
     * @param token Token to check.
     * @param userDetails Claimed user details.
     * @return If the token is valid.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Check if the token is expired.
     *
     * @param token JWT.
     * @return If the expiration is before now.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the expiration from a JWT.
     *
     * @param token JWT.
     * @return The expiration of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generate a new JWT based of a claimed user details with no extra claims.
     *
     * @param userDetails User details.
     * @return Generated token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    /**
     * Generate a JWT by a claimed user details and extra claims.
     *
     * @param userDetails User details to create with.
     * @param extraClaims Extra claims to add to the token.
     * @return The JWT.
     */
    public String generateToken(
            UserDetails userDetails,
            Map<String, Object> extraClaims
    ) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TTL))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract all claims from a JWT.
     *
     * @param token Token to extract claims from.
     * @return Extracted claims.
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
