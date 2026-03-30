package com.miguel.ecommerce.financial;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinancialReportResponse(
        BigDecimal totalRevenue,
        BigDecimal totalCost,
        BigDecimal grossProfit,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
