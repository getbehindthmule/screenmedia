package com.greenhills.fuel.dto.builder;

import com.greenhills.fuel.dto.FuelPriceData;
import com.greenhills.fuel.dto.FuelTypeDetails;
import com.greenhills.fuel.dto.UkWeeklyPriceDetailsCsv;
import com.greenhills.fuel.service.FuelService;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class FuelPriceDataTreeBuilderTest {

    @Test
    public void testConversionAndOrder() {
        // arrange
        List<UkWeeklyPriceDetailsCsv> ukWeeklyPriceDetailsCsvs = Arrays.asList(
                new UkWeeklyPriceDetailsCsv(LocalDate.of(2019,1, 7), new BigDecimal("70"), new BigDecimal("100"), new BigDecimal("40"), new BigDecimal("50"),new BigDecimal("17.5"), new BigDecimal("17.5")),
                new UkWeeklyPriceDetailsCsv(LocalDate.of(2018,12, 31), new BigDecimal("180"), new BigDecimal("200"), new BigDecimal("40"), new BigDecimal("50"),new BigDecimal("20"), new BigDecimal("20"))
        );

        Map.Entry<LocalDate, FuelPriceData> expected2019Value = new AbstractMap.SimpleEntry<>(LocalDate.of(2019,1, 7),
                FuelPriceData.builder().fuelDetailMap(new HashMap<FuelService.FuelType, FuelTypeDetails>() {{
                    put(FuelService.FuelType.DIESEL,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("100"))
                                    .dutyPerLitre(new BigDecimal("50"))
                                    .vat(new BigDecimal("17.5"))
                                    .build());
                    put(FuelService.FuelType.UNLEADED,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("70"))
                                    .dutyPerLitre(new BigDecimal("40"))
                                    .vat(new BigDecimal("17.5"))
                                    .build());
                }}).build());
        Map.Entry<LocalDate, FuelPriceData> expected2018Value = new AbstractMap.SimpleEntry<>(LocalDate.of(2018,12, 31),
                FuelPriceData.builder().fuelDetailMap(new HashMap<FuelService.FuelType, FuelTypeDetails>() {{
                    put(FuelService.FuelType.DIESEL,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("200"))
                                    .dutyPerLitre(new BigDecimal("50"))
                                    .vat(new BigDecimal("20"))
                                    .build());
                    put(FuelService.FuelType.UNLEADED,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("180"))
                                    .dutyPerLitre(new BigDecimal("40"))
                                    .vat(new BigDecimal("20"))
                                    .build());
                }}).build());

        // act
        TreeMap<LocalDate, FuelPriceData> actualValue = FuelPriceDataTreeBuilder.mapFuelCsvToFuelDataTree(ukWeeklyPriceDetailsCsvs);

        // assert
        assertThat(actualValue).containsExactly(expected2018Value, expected2019Value);
    }

    @Test
    public void testEmptyList() {
        List<UkWeeklyPriceDetailsCsv> ukWeeklyPriceDetailsCsvs = Collections.emptyList();

        // act
        TreeMap<LocalDate, FuelPriceData> actualValue = FuelPriceDataTreeBuilder.mapFuelCsvToFuelDataTree(ukWeeklyPriceDetailsCsvs);

        // assert
        assertThat(actualValue).isEmpty();
    }

}