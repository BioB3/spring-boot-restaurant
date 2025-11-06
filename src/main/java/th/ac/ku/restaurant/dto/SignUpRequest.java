package th.ac.ku.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotBlank(message = "Username is mandatory")
  @Size(min = 4, message = "Username must has at least 4 characters in length")
  private String username;

  @NotBlank(message = "Password is mandatory")
  @Size(min = 8, message = "Password must has at least 8 characters in length")
  private String password;

  @NotBlank(message = "Name is mandatory")
  @Pattern(regexp = "^[a-zA-z]+$", message = "Name can only contain letters")
  private String name;
}
