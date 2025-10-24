package th.ac.ku.restaurant.service;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import th.ac.ku.restaurant.dto.SignUpRequest;
import th.ac.ku.restaurant.entity.User;
import th.ac.ku.restaurant.repository.UserRepository;

@Service
public class UserService {

  private UserRepository userRepository;
  private PasswordEncoder encoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }

  public void createUser(SignUpRequest request) {
    User dao = new User();
    dao.setUsername(request.getUsername());
    dao.setPassword(request.getPassword());
    dao.setPassword(encoder.encode(request.getPassword()));
    dao.setName(request.getName());
    dao.setRole("ROLE_USER");
    dao.setCreatedAt(Instant.now());

    userRepository.save(dao);
  }
}
