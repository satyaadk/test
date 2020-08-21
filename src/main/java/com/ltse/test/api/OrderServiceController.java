package com.ltse.test.api;

import com.ltse.test.client.OrderSubmitter;
import com.ltse.test.model.BrokerOrder;
import com.ltse.test.services.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * expose services that can be invoked
 * from a browser for test purpose in this demo
 */
@RestController
public class OrderServiceController {

    @Autowired
    OrderManagementService orderManagementService;
    @Autowired
    OrderSubmitter orderSubmitter;

    /**
     * use health check
     *
     * @return
     */
    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    /**
     * run demo tests to send orders as it test file.
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/test")
    public String test() throws Exception {
        orderSubmitter.sendTestOrders();
        return "OK";
    }

    /**
     * main interface to accept orders
     *
     * @param brokerOrder
     * @return
     */
    @PostMapping("/submit")
    Boolean submitOrder(@RequestBody BrokerOrder brokerOrder) {
        return orderManagementService.submitOrders(brokerOrder);
    }

    /**
     * print rejected orders
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getRejected")
    public List<BrokerOrder> getRejected() throws Exception {
        return orderManagementService.getRejectedOrders();
    }

    /**
     * print accepted orders
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getAccepted")
    public List<BrokerOrder> getAccepted() throws Exception {
        return orderManagementService.getReceivedOrders();
    }
}
