package th.ac.ku.restaurant.Security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    // Allow sending credentials like cookies, auth headers
    config.setAllowCredentials(true);

    // Add specific allowed origin
    config.addAllowedOrigin("https://localhost:3001");

    config.addAllowedHeader("*"); // Allow all headers
    config.addAllowedMethod("GET");
    config.addAllowedMethod("POST");

    // expose Set-Cookie header
    config.setExposedHeaders(List.of("Set-Cookie"));

    // Apply this configuration to all paths
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
