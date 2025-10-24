package th.ac.ku.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "user_info")
public class User {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(unique = true)
  private String username;

  private String password;

  private String name;
  private String role;
  private Instant createdAt;
}
