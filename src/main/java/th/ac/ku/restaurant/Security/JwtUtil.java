package th.ac.ku.restaurant.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  private SecretKey key;

  // Key: token -> value: username
  // we usually store these tokens in an in-memory database such as Redis
  private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    String token = Jwts.builder()
      .subject(username)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
      .signWith(key, Jwts.SIG.HS256)
      .compact();
    tokenStore.put(token, username);
    return token;
  }

  public void invalidateToken(String token) {
    tokenStore.remove(token);
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  public boolean validateJwtToken(String token)
    throws SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
    Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    return true;
  }
}
