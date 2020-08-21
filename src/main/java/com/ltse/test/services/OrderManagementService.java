package com.ltse.test.services;

import com.ltse.test.model.BrokerOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * this class serves as primary interface for exchange to receive client orders.
 * We simple store the orders in a collection for this demo.
 */
@Component
@Slf4j
@Getter
@Setter
public class OrderManagementService {
    @Autowired
    OrderValidatorService orderValidatorService;

    private final List<BrokerOrder> receivedOrders = new ArrayList<>();
    private final List<BrokerOrder> rejectedOrders = new ArrayList<>();

    /**
     * @param order
     * @return
     */
    public boolean submitOrders(final BrokerOrder order) {
        //assign order id
        order.setOrderId(UUID.randomUUID().toString());
        if (orderValidatorService.shouldAccept(order)) {
            log.info("receiving order {} ", order);
            receivedOrders.add(order);
            return true;
        } else {
            log.info("rejecting order {} ", order);
            rejectedOrders.add(order);
        }
        return false;
    }
}
