package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;

public interface ManufacturerService {
    Manufacturer getManufacturerByCode(Integer code);
}
