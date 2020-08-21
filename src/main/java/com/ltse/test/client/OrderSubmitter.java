package com.ltse.test.client;

import com.ltse.test.model.BrokerOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Component
@Slf4j
/**
 * this class mimics a client.
 * a client in this context is a broker who is sending orders.
 * for the purpose of this test, when this is invoked this will read test csv files
 * and submit all orders
 */
public class OrderSubmitter {
    @Value("classpath:data/trades.csv")
    Resource orderFile;
    private String serverUrl = "http://localhost:8080/submit";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Assume file structure for now.
     * Time stamp,broker,sequence id,type,Symbol,Quantity,Price,Side
     *
     * @throws Exception
     */
    public void sendTestOrders() throws Exception {
        log.info("sending orders called...");
        InputStream resource = orderFile.getInputStream();
        BufferedReader reader = null;
        int count = -1;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resource));
            String line = reader.readLine();
            while (line != null) {
                log.info("read line {}", line);
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    if (count >= 0) { //skip first row in CSV
                        try {
                            BrokerOrder order = new BrokerOrder();
                            order.setTime(format(parts[0]));
                            order.setBroker(parts[1]);
                            order.setSequencyId(parts[2]);
                            order.setType(parts[3]);
                            order.setSymbol(parts[4]);
                            order.setQuantity(new BigDecimal(parts[5]));
                            order.setPrice(new BigDecimal(parts[6]));
                            order.setSide(parts[7]);
                            log.info("Submitting order {}", order);
                            restTemplate.postForLocation(serverUrl, order);
                        } catch (Exception e) {
                            log.error("failed to handle row", e);
                        }
                    }
                    count++;
                } else {
                    log.warn("can not handle this line {}", line);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            log.error("failed to load broker file", e);
            throw new Exception(e);
        }
        log.info("sending orders completd for {}", count);
    }

    /**
     * @param order
     */
    public void sendOrder(final BrokerOrder order) {
        restTemplate.postForLocation(serverUrl, order);
    }

    private long format(final String input) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        long time = sdf.parse(input).getTime();
        log.info("format@@ {} {}", input, time);
        return time;
    }
}
