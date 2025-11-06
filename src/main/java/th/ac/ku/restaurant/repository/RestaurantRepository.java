package th.ac.ku.restaurant.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.ac.ku.restaurant.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
  boolean existsByName(String name);
  Optional<Restaurant> findByName(String name);
  List<Restaurant> findByLocation(String location);
}
