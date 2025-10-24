package th.ac.ku.restaurant.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import th.ac.ku.restaurant.service.CustomUserDetailsService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtils;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String jwt = null;

      // Get authorization header and validate
      final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (authHeader != null && authHeader.startsWith("Bearer ")) jwt =
        authHeader.substring(7);

      // Get jwt token and validate
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUsernameFromToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(
          username
        );
        UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
        auth.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      System.out.println("Cannot set user authentication: " + e);
    }
  }
}
