package th.ac.ku.restaurant.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import th.ac.ku.restaurant.entity.User;
import th.ac.ku.restaurant.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(
        "User Not Found with username: " + username
      );
    }

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getRole()));

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      authorities
    );
  }
}
