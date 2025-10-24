package th.ac.ku.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.ac.ku.restaurant.Security.JwtUtil;
import th.ac.ku.restaurant.dto.LoginRequest;
import th.ac.ku.restaurant.dto.SignUpRequest;
import th.ac.ku.restaurant.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

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
  public String authenticateUser(@RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
      )
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return jwtUtils.generateToken(userDetails.getUsername());
  }

  @PostMapping("/signup")
  public String registerUser(@RequestBody SignUpRequest request) {
    if (
      userService.userExists(request.getUsername())
    ) return "Error: Username is already taken!";

    userService.createUser(request);
    return "User registered successfully!";
  }
}
