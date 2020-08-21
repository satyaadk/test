package com.ltse.test.refdata;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
@Setter
@Getter
public class Firms implements InitializingBean {
    @Value("classpath:data/firms.txt")
    Resource firmsFile;
    private final Set<String> firms = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("loading Broker Firms...");
        InputStream resource = firmsFile.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resource));
            String line = reader.readLine();
            while (line != null) {
                log.info("read line {}", line);
                firms.add(line.trim().toLowerCase());
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            log.error("failed to load broker file", e);
            throw new Exception(e);
        }
        log.info("**loading Broker Firms completed with {}", firms.size());
    }

    /**
     * check if supported broker firm.
     *
     * @return
     */
    public boolean isSupported(final String broker) {
        log.info("isSupported {} result {}", broker, firms.contains(broker.toLowerCase()));
        return firms.contains(broker.toLowerCase());
    }
}
