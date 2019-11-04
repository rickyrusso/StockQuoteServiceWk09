package com.origami.teach.services;

import com.origami.teach.model.StockQuote;
import com.origami.teach.util.DatabaseConnectionException;
import com.origami.teach.util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseStockService implements StockService {
    /**
     * Get a historical list of stock quotes for the provide symbol
     *
     * @param symbol the stock symbol to search for
     * @param from   the date of the first stock quote
     * @param until  the date of the last stock quote
     * @return a list of StockQuote instances
     * @throws   StockServiceException if using the service generates an exception.
     * If this happens, trying the service may work, depending on the actual cause of the
     * error.
     */
    @Override
    public List<StockQuote> getQuotes(String symbol, Calendar from, Calendar until) throws StockServiceException {
        List<StockQuote> stockQuotes = null;

        String sql = "select * from quotes where Symbol = ? AND time > ? AND time < (? + INTERVAL 1 DAY) ;";
        try (
                Connection connection = DatabaseUtils.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
            ) {
            statement.setString(1, symbol);
            java.sql.Date fromSqlDate = new java.sql.Date(from.getTime().getTime());
            java.sql.Date untilSqlDate = new java.sql.Date(until.getTime().getTime());
            statement.setDate(2, fromSqlDate);
            statement.setDate(3, untilSqlDate);

            try( ResultSet resultSet = statement.executeQuery()){
                stockQuotes = new ArrayList<StockQuote>(resultSet.getFetchSize());
                while (resultSet.next()) {
                    StockQuote stockQuote = new StockQuote();

                    stockQuote.setId(resultSet.getInt("ID"));
                    stockQuote.setSymbol(resultSet.getString("symbol"));
                    Calendar calendarDate = new GregorianCalendar();
                    calendarDate.setTime(resultSet.getDate("time"));
                    stockQuote.setDate(calendarDate);
                    stockQuote.setPrice(resultSet.getBigDecimal("price"));

                    stockQuotes.add(stockQuote);
                }
            }
        } catch (DatabaseConnectionException | SQLException exception) {
            throw new StockServiceException(exception.getMessage(), exception);
        }
        if (stockQuotes.isEmpty()) {
            throw new StockServiceException("There is no stock data for:" + symbol);
        }
        return stockQuotes;
    }
}