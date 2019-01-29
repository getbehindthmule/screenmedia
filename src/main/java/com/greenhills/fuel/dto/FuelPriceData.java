package com.greenhills.fuel.dto;

import com.greenhills.fuel.service.FuelService;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class FuelPriceData {
    private HashMap<FuelService.FuelType, FuelTypeDetails> fuelDetailMap;
}
