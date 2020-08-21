package com.ltse.test;

import com.ltse.test.model.BrokerOrder;
import com.ltse.test.refdata.Firms;
import com.ltse.test.refdata.Symbols;
import com.ltse.test.services.OrderManagementService;
import com.ltse.test.services.OrderValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * unit test for order validation service
 * check all conditions for order processing
 */
@Slf4j
public class c {

    OrderManagementService orderManagementService;

    /**
     * initial test setup
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        orderManagementService = new OrderManagementService();
        OrderValidatorService orderValidatorService = new OrderValidatorService();
        orderValidatorService.setSymbols(TestUtils.loadTestSymbols());
        orderValidatorService.setFirms(TestUtils.loadTestFirms());
        orderManagementService.setOrderValidatorService(orderValidatorService);
    }

    /**
     * test sending order flow.
     * @throws Exception
     */
    @Test
    public void sendTestOrders() throws Exception {
        log.info("sendTestOrders...");
        String testFileName = "data/trades.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(testFileName).getFile());
        InputStream resource = new FileInputStream(file);
        BufferedReader reader = null;
        int count = -1;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resource));
            String line = reader.readLine();
            while (line != null) {
                log.info("read line {}", line);
                String[] parts = line.split(",");
                if(parts.length == 8) {
                    if(count >=0) { //skip first row in CSV
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
                            orderManagementService.submitOrders(order);
                        } catch (Exception e) {
                            log.error("failed to handle row", e);
                        }
                    }
                    count ++;
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
        //assert
         log.info("test completed with {} rejected and {} accepted orders.",
                 orderManagementService.getRejectedOrders().size()
         , orderManagementService.getReceivedOrders().size());
        printFiles();
        Assert.assertEquals(orderManagementService.getReceivedOrders().size(), 412);
        Assert.assertEquals(orderManagementService.getRejectedOrders().size(), 141);
    }

    /**
     * format time stamp.
     * @param input
     * @return
     * @throws Exception
     */
    private long format(final String input) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        long time = sdf.parse(input).getTime();
        log.info("format@@ {} {}", input, time);
        return time;
    }

    /**
     *
     */
    private void printFiles() {
        log.info("printing received orders and file");
        String newLine = "\r\n";
        StringBuilder tmp = new StringBuilder();
        tmp.append("Broker,TradeID");
        tmp.append(newLine);
        for (BrokerOrder order : orderManagementService.getReceivedOrders()) {
            tmp.append(order.getBroker());
            tmp.append(",");
            tmp.append(order.getOrderId());
            tmp.append(newLine);
        }
        log.info(tmp.toString());
        writeToFile("received.cvs", tmp.toString());
        log.info("printing rejected orders and file");
        tmp = new StringBuilder();
        tmp.append("Broker,TradeID");
        tmp.append(newLine);
        for (BrokerOrder order : orderManagementService.getRejectedOrders()) {
            tmp.append(order.getBroker());
            tmp.append(",");
            tmp.append(order.getOrderId());
            tmp.append(newLine);
        }
        log.info(tmp.toString());
        writeToFile("rejected.cvs", tmp.toString());
    }

    /**
     *
     * @param fileName
     * @param contents
     */
    private void writeToFile(final String fileName, final String contents){
         try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(contents);
            myWriter.close();
            log.info("writeToFile completed..");
        } catch (IOException e) {
           log.error("writeToFile failed", e);
        }
    }

}
