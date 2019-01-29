package com.greenhills.fuel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FuelTypeDetails {
    private BigDecimal pricePerLitre;
    private BigDecimal dutyPerLitre;
    private BigDecimal vat;
}
