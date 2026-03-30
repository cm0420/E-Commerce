package com.miguel.ecommerce.financial;

import com.miguel.ecommerce.order.entity.Order;
import com.miguel.ecommerce.order.entity.OrderItem;
import com.miguel.ecommerce.order.entity.OrderStatus;
import com.miguel.ecommerce.order.repository.OrderRepository;
import com.miguel.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialService {
    private final OrderRepository orderRepository;

    public FinancialReportResponse getBalance(LocalDateTime start, LocalDateTime end) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end); // Precisará criar este método no OrderRepository

        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal costs = BigDecimal.ZERO;

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.CANCELLED) continue;

            for (OrderItem item : order.getItems()) {
                BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                revenue = revenue.add(item.getUnitPrice().multiply(qty));
                costs = costs.add(item.getCostPrice().multiply(qty));
            }
        }
        return new FinancialReportResponse(
                revenue,
                costs,
                revenue.subtract(costs),
                start,
                end
        );
    }
}
