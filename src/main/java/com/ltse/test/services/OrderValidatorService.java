package com.ltse.test.services;

import com.ltse.test.model.BrokerOrder;
import com.ltse.test.refdata.Firms;
import com.ltse.test.refdata.Symbols;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Setter
public class OrderValidatorService {
    @Autowired
    Firms firms;
    @Autowired
    Symbols symbols;
    private Map<String, Long> brokerLastOrderTimestamp = new ConcurrentHashMap<>();
    private Map<String, Set<String>> brokerTradesIdMap = new ConcurrentHashMap<>();

    /**
     * check if a order should be accepted as per applicable rules.
     *
     * @return
     */
    public boolean shouldAccept(final BrokerOrder order) {
        if (order.isValid()) {
            if (firms.isSupported(order.getBroker())
                    && symbols.isSupported(order.getSymbol())) {
                return checkFrequency(order.getBroker(), order.getTime())
                        && checkTradeId(order.getBroker(), order.getSequencyId());
            }
            log.warn("this order can not be accepted {}", order);
            return false;
        }
        log.warn("this order is invalid {}", order);
        return false;
    }

    /**
     * check for max frequency of orders allowed per broker.
     * it is assumed orders are coming with minute level time stamp only.
     *
     * @param broker
     * @param current
     * @return
     */
    private boolean checkFrequency(final String broker, final Long current) {
        Long time = brokerLastOrderTimestamp.get(broker);
        int ordersAtThisTime = 0;
        if (time != null) {
            ordersAtThisTime = (int) (time / current);
            log.info("** current {} ,  ordersAtThisTime {} ", current, ordersAtThisTime);
            if (ordersAtThisTime > 2) {
                return false;
            }
        }
        ordersAtThisTime++;
        Long newTime = ordersAtThisTime * current;
        brokerLastOrderTimestamp.put(broker, newTime);
        return true;
    }

    /**
     * check for trade id unique per broker.
     *
     * @param broker
     * @param current
     * @return
     */
    private boolean checkTradeId(final String broker, final String current) {
        Set<String> brokerTradeIds = brokerTradesIdMap.get(broker);
        if (brokerTradeIds == null) {
            brokerTradeIds = new HashSet<>();
        }
        return brokerTradeIds.add(current);
    }
}
