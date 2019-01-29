package com.greenhills.fuel.service;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.greenhills.fuel.dto.FuelPriceData;
import com.greenhills.fuel.dto.FuelTypeDetails;
import com.greenhills.fuel.dto.UkWeeklyPriceDetailsCsv;
import com.greenhills.fuel.dto.builder.FuelPriceDataTreeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class FuelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuelService.class);
    private static final String UK_FUEL_DETAILS = "fuel_prices.csv";
    private static final BigDecimal GALLONS_TO_LITRES_CONSTANT = new BigDecimal("4.54609");

    TreeMap<LocalDate, FuelPriceData> priceDataTreeMap;

    public enum FuelType {DIESEL, UNLEADED}


    public FuelService() {  }

    @PostConstruct
    public void initialiseFuelPriceList() {
        priceDataTreeMap = FuelPriceDataTreeBuilder.mapFuelCsvToFuelDataTree(loadWeeklyFuelList());
    }

    List<UkWeeklyPriceDetailsCsv> loadWeeklyFuelList()  {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
            CsvSchema schema = mapper.schemaFor(UkWeeklyPriceDetailsCsv.class)
                    .sortedBy("localDate", "unleadedPricePerLitre", "dieselPricePerLitre", "unleadedDutyPerLitre", "dieselDutyPerLitre", "unleadedVat", "dieselVat");
            InputStream stream = new ClassPathResource(UK_FUEL_DETAILS).getInputStream();

            MappingIterator<UkWeeklyPriceDetailsCsv> readValues = mapper.readerFor(UkWeeklyPriceDetailsCsv.class).with(schema).readValues(stream);
            return readValues.readAll();
        } catch (IOException e) {
            LOGGER.error("Unable to read fuel input file, exception details : " + e.toString());
            return Collections.emptyList();
        }
    }


    public Optional<BigDecimal> calculateFuelCost(LocalDate date, FuelType fuelType, BigDecimal milesperGallon, BigDecimal mileage){
        Map.Entry<LocalDate, FuelPriceData> activeWeek = priceDataTreeMap.floorEntry(date);
        if ((activeWeek == null) || milesperGallon.equals(BigDecimal.ZERO)) return Optional.empty();

        BigDecimal totalLitres = calculateTotalLitres(milesperGallon, mileage);
        FuelTypeDetails fuelTypeDetails = activeWeek.getValue().getFuelDetailMap().get(fuelType);

        return Optional.of(round(totalLitres.multiply(fuelTypeDetails.getPricePerLitre())));
    }

    public Optional<BigDecimal> calculateDuty(LocalDate date, FuelType fuelType, BigDecimal milesperGallon, BigDecimal mileage){
        Map.Entry<LocalDate, FuelPriceData> activeWeek = priceDataTreeMap.floorEntry(date);
        if ((activeWeek == null) || milesperGallon.equals(BigDecimal.ZERO)) return Optional.empty();

        BigDecimal totalLitres = calculateTotalLitres(milesperGallon, mileage);
        FuelTypeDetails fuelTypeDetails = activeWeek.getValue().getFuelDetailMap().get(fuelType);

        return Optional.of(round(totalLitres.multiply(fuelTypeDetails.getDutyPerLitre())));
    }

    public Optional<BigDecimal> calculateDailyComparison(LocalDate date, FuelType fuelType, BigDecimal milesperGallon, BigDecimal mileage){
        Optional<BigDecimal> requestedDateTravelCost = this.calculateFuelCost(date, fuelType, milesperGallon, mileage);
        Optional<BigDecimal> currentTravelCost = this.calculateFuelCost(LocalDate.now(), fuelType, milesperGallon, mileage);

        if ((!requestedDateTravelCost.isPresent()) || (!currentTravelCost.isPresent())) return Optional.empty();

        return Optional.of(round(currentTravelCost.get().subtract(requestedDateTravelCost.get())));
    }

    private BigDecimal round(BigDecimal input) {
        return input.setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalLitres(BigDecimal milesperGallon, BigDecimal mileage) {
        BigDecimal totalGallons = mileage.divide(milesperGallon, RoundingMode.HALF_UP);


        return totalGallons.multiply(GALLONS_TO_LITRES_CONSTANT);
    }
}
