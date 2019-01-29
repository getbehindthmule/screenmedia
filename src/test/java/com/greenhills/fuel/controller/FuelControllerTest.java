package com.greenhills.fuel.controller;

import com.greenhills.fuel.dto.FuelSummaryResponse;
import com.greenhills.fuel.exception.IllegalFuelTypeException;
import com.greenhills.fuel.exception.InvalidInputParametersException;
import com.greenhills.fuel.service.FuelService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuelControllerTest {

    private final FuelService mockFuelService = mock(FuelService.class);
    private FuelController sut;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        sut = new FuelController();
        sut.setFuelService(mockFuelService);
    }

    @Test
    public void testInvalidFuelType() {
        // arrange
        expectedException.expect(IllegalFuelTypeException.class);

        // act
        sut.getFuelSummary(LocalDate.now(), "bad-fuel-type", BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    public void testInvalidFuelCostParams() {
        // arrange
        when(mockFuelService.calculateFuelCost(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(mockFuelService.calculateDailyComparison(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        when(mockFuelService.calculateDuty(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        expectedException.expect(InvalidInputParametersException.class);

        // act
        sut.getFuelSummary(LocalDate.now(), "DIESEL", BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    public void testInvalidDutyParams() {
        // arrange
        when(mockFuelService.calculateFuelCost(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        when(mockFuelService.calculateDailyComparison(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(mockFuelService.calculateDuty(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        expectedException.expect(InvalidInputParametersException.class);

        // act
        sut.getFuelSummary(LocalDate.now(), "DIESEL", BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    public void testInvalidComparisonParams() {
        // arrange
        when(mockFuelService.calculateFuelCost(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        when(mockFuelService.calculateDailyComparison(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        when(mockFuelService.calculateDuty(any(), any(), any(), any())).thenReturn(Optional.empty());
        expectedException.expect(InvalidInputParametersException.class);

        // act
        sut.getFuelSummary(LocalDate.now(), "DIESEL", BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    public void testGoodQuery() {
        // arrange
        FuelSummaryResponse expectedResponse = FuelSummaryResponse.builder()
                .cost(BigDecimal.ONE)
                .duty(BigDecimal.ONE)
                .comparisonWithToday(BigDecimal.ONE.negate())
                .build();

        when(mockFuelService.calculateFuelCost(any(), any(), any(), any())).thenReturn(Optional.of(BigDecimal.ONE));
        when(mockFuelService.calculateDailyComparison(any(), any(), any(), any())).thenReturn (Optional.of(BigDecimal.ONE.negate()));
        when(mockFuelService.calculateDuty(any(), any(), any(), any())).thenReturn (Optional.of(BigDecimal.ONE));

        // act
        FuelSummaryResponse actualResponse = sut.getFuelSummary(LocalDate.now(), "DIESEL", BigDecimal.ONE, BigDecimal.ONE);

        // assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}