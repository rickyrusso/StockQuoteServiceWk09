package com.origamisoftware.teach.advanced.services;

import com.origami.teach.services.ServiceFactory;
import com.origami.teach.services.StockService;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * JUnit test for <CODE>StockServiceFactory</CODE>
 */
public class ServiceFactoryTest {

    @Test
    public void testGetStockServiceInstance() {
        StockService stockService = ServiceFactory.getStockServiceInstance();
        assertNotNull(stockService);
    }
}
