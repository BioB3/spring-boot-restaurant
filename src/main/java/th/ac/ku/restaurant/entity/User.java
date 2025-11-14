package th.ac.ku.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import th.ac.ku.restaurant.Security.AttributeEncryptor;

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

  @Convert(converter = AttributeEncryptor.class)
  private String name;

  private String role;
  private Instant createdAt;
}
