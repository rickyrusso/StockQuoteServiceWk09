package com.origami.teach.servlet;

import com.origami.teach.model.StockQuote;
import com.origami.teach.services.ServiceFactory;
import com.origami.teach.services.StockService;
import com.origami.teach.services.StockServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StockSearch  extends HttpServlet {
    private static final String STOCK_SYMBOL_KEY = "stockSymbol";
    private static final String FROM_DATE_KEY = "fromDate";
    private static final String UNTIL_DATE_KEY = "untilDate";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String symbol = request.getParameter(STOCK_SYMBOL_KEY);
        String fromDate = request.getParameter(FROM_DATE_KEY);
        String untilDate = request.getParameter(UNTIL_DATE_KEY);

        StockService stockService = ServiceFactory.getStockServiceInstance();
        HttpSession session = request.getSession();
        try {
            List<StockQuote> stockQuotes = stockService.getQuotes(symbol, parseDate(fromDate), parseDate(untilDate));
            session.setAttribute("stockQuotes", stockQuotes);
            session.setAttribute("inputError", false);
            session.setAttribute("serverError", false);
        } catch (ParseException parseEx){
            session.setAttribute("inputError", true);
        } catch (StockServiceException stockServiceEx){
            session.setAttribute("serverError", true);
        }

        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher =
                servletContext.getRequestDispatcher("/stockquoteResults.jsp");
        dispatcher.forward(request, response);

    }

    private Calendar parseDate(String strDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = simpleDateFormat.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
