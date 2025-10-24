package th.ac.ku.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantRequest {

  private String name;
  private Double rating;
  private String location;
}
