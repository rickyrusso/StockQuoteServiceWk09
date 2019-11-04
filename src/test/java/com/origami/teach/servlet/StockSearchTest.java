package com.origami.teach.servlet;

import com.origami.teach.model.StockQuote;
import com.origami.teach.util.DatabaseUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * JUnit test for StockQuote class
 */
public class StockSearchTest {
    Map<String,Object> attributes = new HashMap<String,Object>();

    private void initDb() throws Exception {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
    }

    @Before
    public void setUp() throws Exception {
        initDb();
    }

    @After
    public void tearDown() throws Exception {
        initDb();
    }

    @Test
    public void doPost() throws IOException, ServletException {
        HttpSession httpSessionMock = Mockito.mock(HttpSession.class);

        Mockito.doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(httpSessionMock).getAttribute(anyString());

        Mockito.doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(httpSessionMock).setAttribute(anyString(), any());


        HttpServletRequest httpServletRequestMock = Mockito.mock(HttpServletRequest.class);

        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);

        when(httpServletRequestMock.getParameter("stockSymbol")).thenReturn("GOOG");
        when(httpServletRequestMock.getParameter("fromDate")).thenReturn("1/1/2019");
        when(httpServletRequestMock.getParameter("untilDate")).thenReturn("12/31/2019");

        HttpServletResponse httpServletResponseMock = Mockito.mock(HttpServletResponse.class);

        RequestDispatcher requestDispatcherMock = Mockito.mock(RequestDispatcher.class);

        final ServletContext servletContextMock = Mockito.mock(ServletContext.class);
        when(servletContextMock.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcherMock);

        StockSearch stockSearch = new StockSearch(){
            public ServletContext getServletContext() {
                return servletContextMock; // return the mock
            }
        };

        stockSearch.doPost(httpServletRequestMock, httpServletResponseMock);

        List<StockQuote> stockQuotes = (List<StockQuote>)attributes.get("stockQuotes");
        assertEquals("Returns 2 records", 2, stockQuotes.size());
    }
}
