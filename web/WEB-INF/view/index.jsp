<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Student</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<table class="table">
    <thead>
    <tr>
        <th>#</th>
        <th>Order ID</th>
        <th>Product Name</th>
        <th>Price</th>
        <th>Amount</th>
        <th>Total Price</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${orderDetailList}" var="orderDetail" varStatus="cnt">
        <tr>
            <td>${cnt.count}</td>
            <td>${orderDetail.orderId}</td>
            <td>${orderDetail.productName}</td>
            <td>${orderDetail.price}</td>
            <td>${orderDetail.amount}</td>
            <td>${orderDetail.totalPrice}</td>
            <td>
                <a href="/confirm/${orderDetail.id}" type="button" class="btn btn-warning">${orderDetail.status}</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
</div>
</div>
</body>
</html>