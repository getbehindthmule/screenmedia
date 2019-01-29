package com.greenhills.fuel.controller;


import com.greenhills.fuel.dto.FuelSummaryResponse;
import com.greenhills.fuel.exception.IllegalFuelTypeException;
import com.greenhills.fuel.exception.InvalidInputParametersException;
import com.greenhills.fuel.service.FuelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequestMapping("/fuel")
public class FuelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuelController.class);

    @Autowired
    private FuelService fuelService;

    public void setFuelService(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping("cost")
    FuelSummaryResponse getFuelSummary(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                       @RequestParam("fuel_type") String incomingFuelType,
                                       @RequestParam("mpg") BigDecimal mpg,
                                       @RequestParam("mileage") BigDecimal mileage) {
        LOGGER.info(String.format("getFuelSummary called with fromDate: %s, fuel_type: %s, mpg: %s, mileage: %s", fromDate, incomingFuelType, mpg, mileage));

        FuelService.FuelType fuelType;
        try {
            fuelType = FuelService.FuelType.valueOf(incomingFuelType.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined fuel type found: " + incomingFuelType);
            throw new IllegalFuelTypeException();
        }
        Optional<BigDecimal> fuelCostInPence = fuelService.calculateFuelCost(fromDate, fuelType, mpg, mileage);
        Optional<BigDecimal> dutyInPence = fuelService.calculateDuty(fromDate, fuelType, mpg, mileage);
        Optional<BigDecimal> priceComparisonWithTodayInPence = fuelService.calculateDailyComparison(fromDate, fuelType, mpg, mileage);

        if ((!fuelCostInPence.isPresent()) || (!dutyInPence.isPresent()) || (!priceComparisonWithTodayInPence.isPresent())) {
            LOGGER.error(String.format("invalid input parameter values detected. One of fromDate: %s, mpg: %s, mileage: %s is invalid ", fromDate, mpg, mileage));
            throw new InvalidInputParametersException();
        }

        return FuelSummaryResponse.builder()
                .cost(fuelCostInPence.get())
                .duty(dutyInPence.get())
                .comparisonWithToday(priceComparisonWithTodayInPence.get())
                .build();

    }

}
