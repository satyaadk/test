package com.ltse.test;

import com.ltse.test.refdata.Firms;
import com.ltse.test.refdata.Symbols;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * test resources
 */
public class TestUtils {

    /**
     *
     * @return
     * @throws Exception
     */
    public static Firms loadTestFirms() throws Exception {
        Firms firms = new Firms();
        firms.setFirmsFile(new ClassPathResource("data/firms.txt"));
        firms.afterPropertiesSet();
        return firms;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static Symbols loadTestSymbols() throws Exception {
        Symbols symbols = new Symbols();
        symbols.setSymbolFile(new ClassPathResource("data/symbols.txt"));
        symbols.afterPropertiesSet();
        return symbols;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static Resource geTestOrderFile() throws Exception {
        return new ClassPathResource("data/tades.csv");
    }

}
