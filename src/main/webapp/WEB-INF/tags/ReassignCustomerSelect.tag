<%@ tag language="java" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%--
	Create a select for all customers except the current customer.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="orderNumber" type="java.lang.Long" %>
<%@ attribute name="exceptCustomer" %>
<select name="custName" onchange="update(null, 'action=update&dataType=order&id=${orderNumber}&attribute=customer&value=' + this.options[this.selectedIndex].value)"
	style="font-size: 10pt;">
	<option selected value="">Reassign this order to customer...
	<c:forEach var="cust" items="${customers}">
		<c:if test="${cust.name != exceptCustomer}">
			<option value="${cust.name}">${cust.name}
		</c:if>
	</c:forEach>
</select>
