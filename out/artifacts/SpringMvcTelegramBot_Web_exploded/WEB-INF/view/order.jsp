<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<html>
<head>
    <title>User Page</title>
</head>
<body>
<h1>USER PAGE</h1>
<table style="border: 1px #4eb41b">
    <c:forEach items="${orderDetailList}" var="order">
        <tr>
            <th>${order.productName}</th>
            <th>${order.totalPrice}</th>
        </tr>
    </c:forEach>
</table>
</body>
</html>

