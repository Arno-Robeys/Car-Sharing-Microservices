package be.ucll.carservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT c FROM Car c WHERE (:location IS NULL OR c.location = :location) AND (:carModel IS NULL OR c.carModel = :carModel) AND (:price IS NULL OR c.price <= :price)")
    List<Car> findAllByInfo(String location, String carModel, BigDecimal price);
}
