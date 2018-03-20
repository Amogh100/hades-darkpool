package application.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static application.core.security.Constants.SECRET;

@Component
public class TokenManager {
    public String getUsername(String authToken) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(authToken);
            username = claims.getSubject();
        } catch (Exception e){
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String authToken) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                         .setSigningKey(SECRET)
                         .parseClaimsJws(authToken)
                         .getBody();
        } catch (Exception e){
            claims = null;
        }
        return claims;
    }

    public boolean validateToken(String authToken, UserDetails details) {
        final String username = getUsername(authToken);
        return username.equals(details.getUsername()) && !isExpired(authToken);
    }

    private boolean isExpired(String authToken) {
        try {
            final Claims claims = getClaimsFromToken(authToken);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date(System.currentTimeMillis()));
        } catch(Exception e){
            return true;
        }
    }

    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date(System.currentTimeMillis()));
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 700000000))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}
