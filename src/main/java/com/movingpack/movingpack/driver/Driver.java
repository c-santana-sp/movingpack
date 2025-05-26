package com.movingpack.movingpack.driver;

import com.movingpack.movingpack.delivery.Delivery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries;
}
