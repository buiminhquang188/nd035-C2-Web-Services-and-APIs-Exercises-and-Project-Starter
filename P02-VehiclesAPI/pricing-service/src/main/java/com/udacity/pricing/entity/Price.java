package com.udacity.pricing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity(name = "price")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "vehicle_id")
    private Long vehicleId;
}
