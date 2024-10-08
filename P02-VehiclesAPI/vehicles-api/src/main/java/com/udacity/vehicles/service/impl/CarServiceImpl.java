package com.udacity.vehicles.service.impl;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.exception.CarNotFoundException;
import com.udacity.vehicles.service.CarService;
import com.udacity.vehicles.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ManufacturerService manufacturerService;
    private final PriceClient priceClient;
    private final MapsClient mapsClient;

    public CarServiceImpl(CarRepository carRepository, ManufacturerService manufacturerService, PriceClient priceClient, MapsClient mapsClient) {
        this.carRepository = carRepository;
        this.manufacturerService = manufacturerService;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     *
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return this.carRepository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     *
     * @param id
     *         the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Optional<Car> optionalCar = this.carRepository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new);

        String price = this.priceClient.getPrice(car.getId());
        car.setPrice(price);

        Location location = new Location(car.getLocation()
                .getLat(), car.getLocation()
                .getLon());
        Location address = this.mapsClient.getAddress(location);
        car.setLocation(address);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     *
     * @param car
     *         A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        Manufacturer manufacturer = this.manufacturerService.getManufacturerByCode(car.getDetails()
                .getManufacturer()
                .getCode());
        car.getDetails()
                .setManufacturer(manufacturer);

        if (car.getId() != null) {
            return this.carRepository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());

                        if (car.getLocation()
                                    .getLat()
                                    .doubleValue() == carToBeUpdated.getLocation()
                                    .getLat()
                                    .doubleValue() && car.getLocation()
                                                              .getLon()
                                                              .doubleValue() == carToBeUpdated.getLocation()
                                                              .getLon()
                                                              .doubleValue()) {
                            Location location = this.mapsClient.getAddress(car.getLocation());
                            car.setLocation(location);
                        }

                        carToBeUpdated.setLocation(car.getLocation());
                        return this.carRepository.save(carToBeUpdated);
                    })
                    .orElseThrow(CarNotFoundException::new);
        }

        return this.carRepository.save(car);
    }

    /**
     * Deletes a given car by ID
     *
     * @param id
     *         the ID number of the car to delete
     */
    public void delete(Long id) {
        Optional<Car> optionalCar = this.carRepository.findById(id);
        optionalCar.orElseThrow(CarNotFoundException::new);
        this.carRepository.deleteById(id);
    }
}
