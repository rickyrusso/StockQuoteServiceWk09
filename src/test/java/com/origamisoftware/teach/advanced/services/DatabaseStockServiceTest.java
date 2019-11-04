package com.origamisoftware.teach.advanced.services;

import com.origami.teach.model.StockQuote;
import com.origami.teach.services.DatabaseStockService;
import com.origami.teach.util.DatabaseUtils;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the DatabaseStockService
 */
public class DatabaseStockServiceTest {

    private void initDb() throws Exception {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
    }

    @Before
    public void setup() throws Exception {
        initDb();
    }

    @After
    public void tearDown() throws Exception {
        initDb();
    }

    @Test
    public void testGetQuote() throws Exception {
        DatabaseStockService databaseStockService = new DatabaseStockService();
        String symbol = "GOOG";
        Calendar from = new GregorianCalendar(2019, 1, 1);
        Calendar until = new GregorianCalendar(2019, 12, 31);

        List<StockQuote> stockQuotes = databaseStockService.getQuotes(symbol, from, until);

        assertEquals("Returns 2 records", 2, stockQuotes.size());
    }
}
