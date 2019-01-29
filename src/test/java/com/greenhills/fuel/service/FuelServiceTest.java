package com.greenhills.fuel.service;

import com.greenhills.fuel.dto.FuelPriceData;
import com.greenhills.fuel.dto.FuelTypeDetails;
import com.greenhills.fuel.dto.UkWeeklyPriceDetailsCsv;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class FuelServiceTest {


    private FuelService sut;

    @Before
    public void setUp() {
        sut = new FuelService();

    }

    @Test
    public void testFileLoad() {
        // arrange

        // act
        List<UkWeeklyPriceDetailsCsv> ukWeeklyPriceDetailCsvs = sut.loadWeeklyFuelList();

        // assert
        assertThat(ukWeeklyPriceDetailCsvs).hasSize(816)
                .extracting("localDate","unleadedPricePerLitre","dieselPricePerLitre","unleadedDutyPerLitre", "dieselDutyPerLitre","unleadedVat","dieselVat")
                .contains(
                        tuple(LocalDate.of(2003, 6, 9), new BigDecimal("74.59"), new BigDecimal("76.77"), new BigDecimal("45.82"), new BigDecimal("45.82"), new BigDecimal("17.5"), new BigDecimal("17.5")),
                        tuple(LocalDate.of(2019, 1, 21), new BigDecimal("119.12"), new BigDecimal("128.92"), new BigDecimal("57.95"), new BigDecimal("57.95"), new BigDecimal("20"), new BigDecimal("20"))
                );

    }

    @Test
    public void testPostConstructLoad () {
        // arrange
        Map.Entry<LocalDate, FuelPriceData> expectedFirstValue = new AbstractMap.SimpleEntry<>(LocalDate.of(2003,6, 9),
                FuelPriceData.builder().fuelDetailMap(new HashMap<FuelService.FuelType, FuelTypeDetails>() {{
                    put(FuelService.FuelType.DIESEL,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("76.77"))
                                    .dutyPerLitre(new BigDecimal("45.82"))
                                    .vat(new BigDecimal("17.5"))
                                    .build());
                    put(FuelService.FuelType.UNLEADED,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("74.59"))
                                    .dutyPerLitre(new BigDecimal("45.82"))
                                    .vat(new BigDecimal("17.5"))
                                    .build());
                }}).build());
        Map.Entry<LocalDate, FuelPriceData> expectedLastValue = new AbstractMap.SimpleEntry<>(LocalDate.of(2019,1, 21),
                FuelPriceData.builder().fuelDetailMap(new HashMap<FuelService.FuelType, FuelTypeDetails>() {{
                    put(FuelService.FuelType.DIESEL,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("128.92"))
                                    .dutyPerLitre(new BigDecimal("57.95"))
                                    .vat(new BigDecimal("20"))
                                    .build());
                    put(FuelService.FuelType.UNLEADED,
                            FuelTypeDetails.builder()
                                    .pricePerLitre(new BigDecimal("119.12"))
                                    .dutyPerLitre(new BigDecimal("57.95"))
                                    .vat(new BigDecimal("20"))
                                    .build());
                }}).build());

        // act
        sut.initialiseFuelPriceList();

        // assert
        assertThat(sut.priceDataTreeMap).hasSize(816);
        assertThat(sut.priceDataTreeMap.firstEntry()).isEqualTo(expectedFirstValue);
        assertThat(sut.priceDataTreeMap.lastEntry()).isEqualTo(expectedLastValue);

    }

    @Test
    public void testBadDate (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateFuelCost(LocalDate.of(2003, 6, 8), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.isPresent()).isFalse();
    }

    @Test
    public void testZeroMpgInFuelCost (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateFuelCost(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ZERO, BigDecimal.TEN);

        // assert
        assertThat(actualValue.isPresent()).isFalse();
    }

    @Test
    public void testZeroMileageInFuelCost (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateFuelCost(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.ZERO);

        // assert
        assertThat(actualValue.get()).isZero();
    }

    @Test
    public void testGoodMileageInDieselInFuelCost (){
        // arrange
        sut.initialiseFuelPriceList();  // diesel price for 09/06/2003 is 76.77 pence

        // act
        Optional<BigDecimal> actualValue = sut.calculateFuelCost(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("3490");
    }

    @Test
    public void testGoodMileageInUnleadedInFuelCost (){
        // arrange
        sut.initialiseFuelPriceList();  // unleaded price for 22/01/2019 is 119.12 pence

        // act
        Optional<BigDecimal> actualValue = sut.calculateFuelCost(LocalDate.of(2019, 1, 22), FuelService.FuelType.UNLEADED, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("5415");
    }

    @Test
    public void testZeroMpgInDutyCalc (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDuty(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ZERO, BigDecimal.TEN);

        // assert
        assertThat(actualValue.isPresent()).isFalse();
    }

    @Test
    public void testZeroMileageInDutyCalc (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDuty(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.ZERO);

        // assert
        assertThat(actualValue.get()).isZero();
    }

    @Test
    public void testGoodMileageInDieselInDutyCalc (){
        // arrange
        sut.initialiseFuelPriceList();  // diesel duty for 09/06/2003 is 45.82 pence

        // act
        Optional<BigDecimal> actualValue = sut.calculateDuty(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("2083");
    }

    @Test
    public void testGoodMileageInUnleadedInDutyCalc (){
        // arrange
        sut.initialiseFuelPriceList();  // unleaded price for 22/01/2019 is 57.95 pence

        // act
        Optional<BigDecimal> actualValue = sut.calculateDuty(LocalDate.of(2019, 1, 22), FuelService.FuelType.UNLEADED, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("2634");
    }

    @Test
    public void testZeroMpgInComparisonCalc (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDailyComparison(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ZERO, BigDecimal.TEN);

        // assert
        assertThat(actualValue.isPresent()).isFalse();
    }

    @Test
    public void testZeroMileageInComparisonCalc (){
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDailyComparison(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.ZERO);

        // assert
        assertThat(actualValue.get()).isZero();
    }

    @Test
    public void testCurrentlyMoreExpensiveInComparisonCalc (){
        // diesel cost 09/06/2003 was 76.77 pence
        // latest diesel cost is 128.92 pence
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDailyComparison(LocalDate.of(2003, 6, 9), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("2371");
    }

    @Test
    public void testCurrentlyLessExpensiveInComparisonCalc (){
        // diesel cost 16/4/2012 was 148.05 pence
        // latest diesel cost is 128.92 pence
        // arrange
        sut.initialiseFuelPriceList();

        // act
        Optional<BigDecimal> actualValue = sut.calculateDailyComparison(LocalDate.of(2012, 4, 16), FuelService.FuelType.DIESEL, BigDecimal.ONE, BigDecimal.TEN);

        // assert
        assertThat(actualValue.get()).isEqualByComparingTo("-869");
    }
}