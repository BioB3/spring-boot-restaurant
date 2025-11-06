package th.ac.ku.restaurant.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import th.ac.ku.restaurant.dto.RestaurantRequest;
import th.ac.ku.restaurant.entity.Restaurant;
import th.ac.ku.restaurant.repository.RestaurantRepository;

@Service
public class RestaurantService {

  private RestaurantRepository repository;

  @Autowired
  public RestaurantService(RestaurantRepository repository) {
    this.repository = repository;
  }

  public Page<Restaurant> getRestaurantPage(PageRequest pageRequest) {
    return repository.findAll(pageRequest);
  }

  public Restaurant getRestaurantById(UUID id) {
    return repository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
  }

  public Restaurant getRestaurantByName(String name) {
    return repository
      .findByName(name)
      .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
  }

  public List<Restaurant> getRestaurantByLocation(String location) {
    return repository.findByLocation(location);
  }

  public Restaurant create(RestaurantRequest request) {
    if (
      repository.existsByName(request.getName())
    ) throw new EntityExistsException("Restaurant name already exists");
    Restaurant dao = new Restaurant();
    dao.setName(request.getName());
    dao.setRating(request.getRating());
    dao.setLocation(request.getLocation());
    dao.setCreatedAt(Instant.now());
    dao.setUpdatedAt(Instant.now());
    Restaurant record = repository.save(dao);
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
