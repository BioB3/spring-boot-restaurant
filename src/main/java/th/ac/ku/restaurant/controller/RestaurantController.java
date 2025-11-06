package th.ac.ku.restaurant.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import th.ac.ku.restaurant.dto.RestaurantRequest;
import th.ac.ku.restaurant.entity.Restaurant;
import th.ac.ku.restaurant.service.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

  private RestaurantService service;

  @Autowired
  public RestaurantController(RestaurantService service) {
    this.service = service;
  }

  @GetMapping("/restaurants")
  public Page<Restaurant> getAllRestaurants(
    @RequestParam(value = "offset", required = false) Integer offset,
    @RequestParam(value = "pageSize", required = false) Integer pageSize,
    @RequestParam(value = "sortBy", required = false) String sortBy
  ) {
    if (offset == null) offset = 0;
    if (pageSize == null) pageSize = 10;
    if (StringUtils.isEmpty(sortBy)) sortBy = "name";

    return service.getRestaurantPage(
      PageRequest.of(offset, pageSize, Sort.by(sortBy))
    );
  }

  @GetMapping("/restaurants/{id}")
  public Restaurant getRestaurantById(@PathVariable UUID id) {
    return service.getRestaurantById(id);
  }

  @GetMapping("/restaurants/name/{name}")
  public Restaurant getRestaurantByName(@PathVariable String name) {
    return service.getRestaurantByName(name);
  }

  @GetMapping("/restaurants/location/{location}")
  public List<Restaurant> getRestaurantByLocation(
    @PathVariable String location
  ) {
    return service.getRestaurantByLocation(location);
  }

  @PostMapping("/restaurants")
  public Restaurant create(@Valid @RequestBody RestaurantRequest restaurant) {
    return service.create(restaurant);
  }

  @PutMapping("/restaurants")
  public Restaurant update(@RequestBody Restaurant restaurant) {
    return service.update(restaurant);
  }

  @DeleteMapping("/restaurants/{id}")
  public Restaurant delete(@PathVariable UUID id) {
    return service.delete(id);
  }
}
