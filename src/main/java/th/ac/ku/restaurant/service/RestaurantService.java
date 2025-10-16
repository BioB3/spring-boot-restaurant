package th.ac.ku.restaurant.service;

import th.ac.ku.restaurant.entity.Restaurant;
import th.ac.ku.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class RestaurantService {

  private RestaurantRepository repository;

  @Autowired
  public RestaurantService(RestaurantRepository repository) {
      this.repository = repository;
  }
  
  public List<Restaurant> getAll() {
    return repository.findAll();
  }

  public Restaurant getRestaurantById(UUID id) {
    return repository.findById(id).get();  
  }

  public Restaurant getRestaurantByName(String name) {
    return repository.findByName(name);
  }

  public List<Restaurant> getRestaurantByLocation(String location) {
    return repository.findByLocation(location);
  }

  public Restaurant create(Restaurant restaurant) {
    restaurant.setCreatedAt(Instant.now());
    Restaurant record = repository.save(restaurant);
    return record;
  }

  public Restaurant update(Restaurant requestBody) {
    UUID id = requestBody.getId();
    Restaurant record = repository.findById(id).get();
    record.setName(requestBody.getName());
    record.setRating(requestBody.getRating());
    record.setLocation(requestBody.getLocation());

    record = repository.save(record);
    return record;
  }

  public Restaurant delete(UUID id) {
    Restaurant record = repository.findById(id).get();
    repository.deleteById(id);
    return record;
  }
}
