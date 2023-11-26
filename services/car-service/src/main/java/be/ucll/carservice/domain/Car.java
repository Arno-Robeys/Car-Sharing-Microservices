package be.ucll.carservice.domain;

import jakarta.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer ownerId;
    private String carModel;
    @Column(name = "\"year\"")
    private Integer year;
    private String location;
    private Double price;
    private Boolean available;

    public Car(
            Integer ownerId,
            String carModel,
            Integer year,
            String location,
            Double price,
            Boolean available
    ) {
        this.ownerId = ownerId;
        this.carModel = carModel;
        this.year = year;
        this.location = location;
        this.price = price;
        this.available = available;
    }

    public Car() {

    }
}
