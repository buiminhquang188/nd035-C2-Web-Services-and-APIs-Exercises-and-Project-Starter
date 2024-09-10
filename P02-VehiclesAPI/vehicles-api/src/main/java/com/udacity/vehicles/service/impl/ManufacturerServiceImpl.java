package com.udacity.vehicles.service.impl;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import com.udacity.vehicles.exception.ManufacturerNotFoundException;
import com.udacity.vehicles.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Manufacturer getManufacturerByCode(Integer code) {
        Optional<Manufacturer> manufacturer = this.manufacturerRepository.findByCode(code);
        return manufacturer.orElseThrow(ManufacturerNotFoundException::new);
    }
}
