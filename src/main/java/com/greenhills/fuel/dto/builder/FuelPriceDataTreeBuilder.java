package com.greenhills.fuel.dto.builder;

import com.greenhills.fuel.dto.FuelPriceData;
import com.greenhills.fuel.dto.FuelTypeDetails;
import com.greenhills.fuel.dto.UkWeeklyPriceDetailsCsv;
import com.greenhills.fuel.service.FuelService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FuelPriceDataTreeBuilder {

    public static TreeMap<LocalDate, FuelPriceData> mapFuelCsvToFuelDataTree(List<UkWeeklyPriceDetailsCsv> ukWeeklyPriceDetailsList) {
        // first collect to a transformed value  Map then convert to TreeMap - simpler than trying to create TreeMap directly for transformed values
        return ukWeeklyPriceDetailsList.stream()
                .collect(Collectors.toMap(UkWeeklyPriceDetailsCsv::getLocalDate,
                        FuelPriceDataTreeBuilder::buildFuelPriceData,
                        (oldValue, newValue) -> newValue,
                        TreeMap::new));

    }

    private static FuelPriceData buildFuelPriceData(UkWeeklyPriceDetailsCsv entry) {
        return FuelPriceData.builder()
                .fuelDetailMap(new HashMap<FuelService.FuelType, FuelTypeDetails>() {{
                    put(FuelService.FuelType.DIESEL,
                        FuelTypeDetails.builder()
                            .pricePerLitre(entry.getDieselPricePerLitre())
                            .dutyPerLitre(entry.getDieselDutyPerLitre())
                            .vat(entry.getDieselVat())
                            .build());
                    put(FuelService.FuelType.UNLEADED,
                        FuelTypeDetails.builder()
                            .pricePerLitre(entry.getUnleadedPricePerLitre())
                            .dutyPerLitre(entry.getUnleadedDutyPerLitre())
                            .vat(entry.getUnleadedVat())
                            .build());

        }}).build();
    }

}
