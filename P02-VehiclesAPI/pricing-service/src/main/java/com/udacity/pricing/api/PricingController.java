package com.udacity.pricing.api;

import com.udacity.pricing.entity.Price;
import com.udacity.pricing.exception.PriceException;
import com.udacity.pricing.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Implements a REST-based controller for the pricing service.
 */
@RepositoryRestController
@AllArgsConstructor
public class PricingController {
    private final PriceRepository priceRepository;

    /**
     * Gets the price for a requested vehicle.
     *
     * @param id
     *         ID number of the vehicle for which the price is requested
     * @return price of the vehicle, or error that it was not found.
     */
    @GetMapping("prices/{id}")
    public ResponseEntity<?> getPrice(@PathVariable Integer id) {
        Optional<Price> optionalPrice = this.priceRepository.findByVehicleId(id);
        Price price = optionalPrice.orElseThrow(() -> new PriceException("Cannot find price for Vehicle " + id));
        return ResponseEntity.ok(price);
    }
}
