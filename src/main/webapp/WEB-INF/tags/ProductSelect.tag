<%@ attribute name="lineItemId" type="java.lang.Long" %>
<%@ attribute name="currentProductId" type="java.lang.Long" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<select name="prodName" onchange="update(null, 'action=update&dataType=lineitem&id=${lineItemId}&attribute=product&value=' + this.options[this.selectedIndex].value)"
	style="font-size: 10pt;">
	<c:forEach var="product" items="${products}">
			<option value="${product.productNumber}"
		<c:if test="${product.productNumber == currentProductId}">
			selected
		</c:if>
			>${product.name}
	</c:forEach>
</select>
