package com.movingpack.movingpack.delivery;

import com.movingpack.movingpack.driver.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postalCode;

    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
