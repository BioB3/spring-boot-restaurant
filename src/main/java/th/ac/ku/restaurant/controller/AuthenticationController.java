package th.ac.ku.restaurant.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.ac.ku.restaurant.Security.JwtUtil;
import th.ac.ku.restaurant.dto.LoginRequest;
import th.ac.ku.restaurant.dto.SignUpRequest;
import th.ac.ku.restaurant.dto.UserInfoResponse;
import th.ac.ku.restaurant.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private static final String AUTH_COOKIE_NAME = "token";

  private UserService userService;
  private AuthenticationManager authenticationManager;
  private JwtUtil jwtUtils;

  @Autowired
  public AuthenticationController(
    UserService userService,
    AuthenticationManager authenticationManager,
    JwtUtil jwtUtils
  ) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(
    @Valid @RequestBody LoginRequest request
  ) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
      )
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtUtils.generateToken(userDetails.getUsername());

    // Create HttpOnly cookie
    ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
      .httpOnly(true) // Javascript cannot read cookie
      .secure(true) // HTTPS only
      .path("/")
      .maxAge(60 * 60) // 1 hour
      .sameSite("Strict")
      .build();

    // Return cookie in response headers, optional JSON body
    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(Map.of("message", "Successfully logged in"));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(
    @Valid @RequestBody SignUpRequest request
  ) {
    if (
      userService.userExists(request.getUsername())
    ) throw new EntityExistsException("Username already exists");

    userService.createUser(request);
    return ResponseEntity.ok("User registered successfully!");
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(HttpServletRequest request) {
    String token = extractTokenFromCookie(request);
    if (token == null) {
      return ResponseEntity.status(401).body("No auth token");
    }

    String username = jwtUtils.getUsernameFromToken(token);
    if (username == null) {
      return ResponseEntity.status(401).body("Invalid token");
    }

    return ResponseEntity.ok(new UserInfoResponse(username));
  }

  private String extractTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) return null;

    for (Cookie cookie : request.getCookies()) {
      if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    String token = extractTokenFromCookie(request);

    if (token != null) jwtUtils.invalidateToken(token);

    // Clear cookie
    ResponseCookie cleared = ResponseCookie.from(AUTH_COOKIE_NAME, "")
      .httpOnly(true)
      .secure(true)
      .path("/")
      .maxAge(0) // expires immediately
      .sameSite("None")
      .build();

    response.addHeader("Set-Cookie", cleared.toString());

    return ResponseEntity.ok("Logged out");
  }
}
