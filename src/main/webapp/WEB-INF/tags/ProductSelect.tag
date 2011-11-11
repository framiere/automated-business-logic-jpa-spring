<%@ tag language="java" pageEncoding="ISO-8859-1"
	import="java.util.*"
	import="autobizlogic.demo.buslogicdemospring.data.Customer"
	import="buslogicdemo.test.*"
	import="buslogicdemo.util.*"
%>

<%--
	Create a select for all products.
--%>

<%@ tag isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="lineItemId" type="java.lang.Long" %>
<%@ attribute name="currentProductId" type="java.lang.Long" %>
<select name="prodName" onchange="update(null, 'todo=update&type=Lineitem&id=${lineItemId}&att=product&value=' + this.options[this.selectedIndex].value)"
	style="font-size: 10pt;">
	<c:forEach var="product" items="${products}">
			<option value="${product.productNumber}"
		<c:if test="${product.productNumber == currentProductId}">
			selected
		</c:if>
			>${product.name}
	</c:forEach>
</select>
