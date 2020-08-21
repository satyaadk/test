package com.ltse.test.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BrokerOrder {
    String orderId;
    long time;
    String broker;
    String symbol;
    String type;
    BigDecimal quantity;
    String sequencyId;
    String side;
    BigDecimal price;

    public boolean isValid() {
        return broker != null
                && symbol != null
                && type != null
                && quantity != null
                && sequencyId != null
                && side != null
                && price != null;
    }
}
