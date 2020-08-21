package com.ltse.test.refdata;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
@Getter
@Setter
public class Symbols implements InitializingBean {

    @Value("classpath:data/symbols.txt")
    Resource symbolFile;
    private final Set<String> symbols = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("loading Symbols...");
        InputStream resource = symbolFile.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resource));
            String line = reader.readLine();
            while (line != null) {
                log.info("read line {}", line);
                symbols.add(line.trim().toLowerCase());
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            log.error("failed to load broker file", e);
            throw new Exception(e);
        }
        log.info("**loading Symbols completed with {}", symbols.size());
    }

    /**
     * check if supported symbol
     *
     * @return
     */
    public boolean isSupported(final String symbol) {
        log.info("isSupported {} result {}", symbol, symbols.contains(symbol.toLowerCase()));
        return symbols.contains(symbol.toLowerCase());
    }
}
