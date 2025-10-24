package th.ac.ku.restaurant.dto;

import lombok.Data;

@Data
public class SignUpRequest {

  private String username;
  private String password;
  private String name;
}
