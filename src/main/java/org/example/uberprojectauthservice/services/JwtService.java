package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements CommandLineRunner {

    @Value("${jwt.expiry}") // JWT expiration time in seconds, fetched from application properties.
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    //this method create brand-new Jwt token for us based on payload
    public String createToken(Map<String, Object> payload, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);



        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .subject(email)
                .signWith(getSignKey())
                .compact();
    }

    public String createToken(String email) {
        return createToken(new HashMap<>(), email);
    }

    //Generates a signing key from the SECRET using HMAC SHA.
    public Key getSignKey()
    {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //Extracts all claims (payload) from a given JWT.
    private Claims extractAllPayload(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts a specific claim from the JWT using a resolver function.
     *
     * @param <T>            The type of the claim to be extracted.
     * @param token          The JWT as a String.
     * @param claimsResolver A function that resolves a specific claim from the Claims object.
     * @return The resolved claim of type T.
     */
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllPayload(token);
        return claimsResolver.apply(claims);
    }

    //Extracts the expiration date of the token.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Checks if the token has expired.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Extracts the subject (typically the email) from the token.
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Validates the JWT by checking if the email matches and the token has not expired.
    public Boolean validateToken(String token, String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        return (userEmailFetchedFromToken.equals(email) && !isTokenExpired(token));
    }

    /**
     * Extracts a specific key-value pair from the payload of the token.
     *
     * @param token      The JWT as a String.
     * @param payloadKey The key of the payload to extract.
     * @return The value associated with the payloadKey.
     */
    private Object extractPayload(String token,String payloadKey) {
        Claims claims = extractAllPayload(token);
        return (Object) claims.get(payloadKey);
    }
    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> mp=new HashMap<>();
        mp.put("email","a@b.com");
        mp.put("phoneNumber","9999999");

        String result=createToken(mp,"Nikita");
        System.out.println(result);
        System.out.println(extractPayload(result,"phoneNumber").toString());
    }
}
