<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%--
	Create a select control with all the customers.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<select onchange="update(this.options[this.selectedIndex].value, null)">
	<c:forEach var="cust" items="${customers}">
		<option value="${cust.name}"
		<c:if test="${cust.name == customer.name}">
			selected="true"
		</c:if>
		>${cust.name}
	</c:forEach>	
</select>