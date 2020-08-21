package com.ltse.test;

import com.ltse.test.model.BrokerOrder;
import com.ltse.test.refdata.Firms;
import com.ltse.test.refdata.Symbols;
import com.ltse.test.services.OrderValidatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * unit test for order validation service
 * check all conditions for order processing
 */
@Slf4j
public class OrderValidatorServiceTest {
    private OrderValidatorService orderValidatorService;

    @Before
    public void setUp(){
        Firms firms = new Firms();
        firms.getFirms().add("Fidelity".toLowerCase());
        firms.getFirms().add("Charles Schwab".toLowerCase());
        Symbols symbols = new Symbols();
        symbols.getSymbols().add("TSLA".toLowerCase());
        symbols.getSymbols().add("AMZN".toLowerCase());
        orderValidatorService = new OrderValidatorService();
        orderValidatorService.setFirms(firms);
        orderValidatorService.setSymbols(symbols);
    }

    @Test
    public void testValidSymbol(){
        BrokerOrder order = createBrokerOrder();
        order.setBroker("Fidelity");
        order.setSymbol("SHOP");
        boolean result = orderValidatorService.shouldAccept(order);
        log.info("testValidSymbol result {}", result);
        Assert.assertFalse(result);
    }

    @Test
    public void testValidBroker(){
        BrokerOrder order = createBrokerOrder();
        order.setBroker("Edward Jones");
        order.setSymbol("TSLA");
        boolean result = orderValidatorService.shouldAccept(order);
        log.info("testValidBroker result {}", result);
        Assert.assertFalse(result);
    }

    @Test
    public void testValidOrder(){
        BrokerOrder order = createBrokerOrder();
        order.setBroker("Fidelity");
        order.setSymbol("TSLA");
        boolean result = orderValidatorService.shouldAccept(order);
        log.info("testValidOrder result {}", result);
        Assert.assertTrue(result);
    }

    @Test
    public void testOrderFrequency(){
        //reinitialize
        Firms firms = new Firms();
        firms.getFirms().add("Fidelity".toLowerCase());
        Symbols symbols = new Symbols();
        symbols.getSymbols().add("TSLA".toLowerCase());
        orderValidatorService = new OrderValidatorService();
        orderValidatorService.setFirms(firms);
        orderValidatorService.setSymbols(symbols);
        BrokerOrder order = createBrokerOrder();
        order.setBroker("Fidelity");
        order.setSymbol("TSLA");
        boolean result = orderValidatorService.shouldAccept(order);
        log.info("testOrderFrequency order 1 result {}", result);
        Assert.assertTrue(result);
        result = orderValidatorService.shouldAccept(order);
        log.info("testOrderFrequency order 2 result {}", result);
        Assert.assertTrue(result);
        result = orderValidatorService.shouldAccept(order);
        log.info("testOrderFrequency order 3 result {}", result);
        Assert.assertTrue(result);
        result = orderValidatorService.shouldAccept(order);
        log.info("testOrderFrequency order 4 result {}", result);
        Assert.assertFalse(result);
        result = orderValidatorService.shouldAccept(order);
        log.info("testOrderFrequency order 5 result {}", result);
        Assert.assertFalse(result);
    }

    /**
     * create dummy broker order.
     * @return
     */
    private BrokerOrder createBrokerOrder(){
        BrokerOrder order = new BrokerOrder();
        order.setBroker("NA");
        order.setSymbol("NA");
        order.setTime(1234);
        order.setType("1");
        order.setQuantity(BigDecimal.valueOf(100));
        order.setSequencyId("1");
        order.setSide("Buy");
        order.setPrice(BigDecimal.valueOf(123.25));
        return order;
    }

}
