package com.greenhills.fuel.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FuelSummaryResponse {
    private BigDecimal cost ;
    private BigDecimal duty ;
    private BigDecimal comparisonWithToday ;

}
