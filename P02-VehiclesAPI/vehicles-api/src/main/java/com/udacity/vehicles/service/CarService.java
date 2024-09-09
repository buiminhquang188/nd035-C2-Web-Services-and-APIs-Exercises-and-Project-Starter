package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.car.Car;

import java.util.List;

public interface CarService {
    List<Car> list();

    Car findById(Long id);

    Car save(Car car);

    void delete(Long id);
}
