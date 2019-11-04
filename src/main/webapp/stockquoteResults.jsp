<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <title>Search Results</title>
    </head>
    <body>
        <h2>Here are your results.</h2>
        <p>
            <table>
                <thead>
                    <tr>
                        <th>Symbol</th>
                        <th>Date</th>
                        <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sessionScope.stockQuotes}" var="stockQuote">
                        <tr>
                            <td><c:out value="${stockQuote.getSymbol()}"/></td>

                            <fmt:formatDate value="${stockQuote.getDate().getTime()}" var="formattedDate" type="date" pattern="MM/dd/yyy" />
                            <td><c:out value="${formattedDate}"/></td>

                            <td><c:out value="${stockQuote.getPrice()}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </p>
    </body>
</html>
