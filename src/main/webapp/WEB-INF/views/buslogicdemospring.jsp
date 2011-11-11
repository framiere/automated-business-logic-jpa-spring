<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="demo" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ABL demo - Spring edition</title>
	<style>
	.actionlink {
	-moz-box-shadow:inset 0px 1px 0px 0px #ffffff;
	-webkit-box-shadow:inset 0px 1px 0px 0px #ffffff;
	box-shadow:inset 0px 1px 0px 0px #ffffff;
	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #dfdfdf) );
	background:-moz-linear-gradient( center top, #ededed 5%, #dfdfdf 100% );
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed', endColorstr='#dfdfdf');
	background-color:#ededed;
	-moz-border-radius:6px;
	-webkit-border-radius:6px;
	border-radius:6px;
	border:1px solid #dcdcdc;
	display:inline-block;
	color:#444444;
	font-family:arial;
	font-size:15px;
	font-weight:bold;
	padding:5px 20px;
	text-decoration:none;
	text-shadow:1px 1px 0px #ffffff;
	}
	.actionlink:hover {
		background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #dfdfdf), color-stop(1, #ededed) );
		background:-moz-linear-gradient( center top, #dfdfdf 5%, #ededed 100% );
		filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#ededed');
		background-color:#dfdfdf;
	}
	.actionlink:active {
		position:relative;
		top:1px;
	}

	</style>

	<script type="text/javascript">
		function update(custName, arg) {
			if ( ! custName || custName == '')
				custName = "${customer.name}";
			window.location='/BusLogicDemoSpring/${controllerName}?name=' + escape(custName) + '&' + arg;
		}
		
		function isNumber(str) {
			if (str.length == 0) {
				return false;
			}
			numdecs = 0;
			for (i = 0; i < str.length; i++) {
				mychar = str.charAt(i);
				if ((mychar >= "0" && mychar <= "9") || mychar == "." || mychar == "$" || mychar == ",") {
					if (mychar == ".")
						numdecs++;
				}
				else 
					return false;
			}
			if (numdecs > 1) {
				return false;
			}
			return true;
		}
				
	</script>
</head>

<body style="font-family: arial; font-size: 10pt;">

<table border="0" style="border-spacing: 0px; padding: 0px;"><tr><td valign="top">
	<table border="0">
		<tr>
			<td>
				Customers:
			</td>
			<td>
				<demo:AllCustomersSelect />
			</td>
			<td style="width: 80%; background-color: EEEEEE;">
				Current transaction mode: ${txMode}
			</td>
			<td>&nbsp;&nbsp;</td>
			<td><a href="/BusLogicDemoSpring/resources/Readme.html" target="help" style="padding: 3px; background-color: #333333; color: #DDDDDD;">Help</a></td>
		</tr>
	</table>
	<p/>
	<c:if test="${errorMessage != null}">
	<div style="padding: 5px; background-color: #FFDDDD; border: solid 1px #FF0000;">${errorMessage}
	<br/>
	<a href="/BusLogicDemoSpring/${controllerName}?name=${customer.name}">Refresh</a>
	</div>
	</c:if>
	
    <table border="0" cellspacing="3">
    	<tr>
    		<td>Balance:</td>
    		<td>
    <input type="text" value="${customer.balance}" 
    	style="text-align: right; background-color: #DDDDDD;" readonly size="9"/>
			</td>
			<td>&nbsp;&nbsp;</td>
			<td>Credit limit:</td>
			<td>
    <input type="text" value="${customer.creditLimit}" 
    	style="text-align: right;" size="9"
    	onchange="if ( ! isNumber(this.value)) {alert('Invalid number'); return false;} update(null, 'todo=update&type=Customer&att=creditLimit&value=' + escape(this.value))"
    	/>
			</td>
			<td>&nbsp;&nbsp;</td>
			<td>Preferred:</td>
			<td>
				<input type="checkbox" ${customer.preferred ? "checked" : ""} onclick="update(null, 'todo=update&type=Customer&att=isPreferred');" />
			</td>
		</tr>
	</table>
	<p/>
	
	<table cellpadding="3" cellspacing="0">
		<c:forEach var="order" items="${customer.purchaseorders}">
			<thead style="background-color: #555555; color: #DDDDFF;">
				<tr>
					<td>&nbsp;</td>
					<td nowrap style="border-right: 1px solid #AAAAAA;">Order #</td>
					<td nowrap style="border-right: 1px solid #AAAAAA;">Amount total</td>
					<td nowrap>Order paid</td>
					<td>&nbsp;</td>
				</tr>
			</thead>
		
			<tr valign="top" style="background-color: #E4E4E4;">
				<td style="font-size: 9pt;">
					<a href="#" 
						onclick="if ( ! confirm('Delete this order?')) return false; update(null, 'todo=delete&type=Order&id=${order.orderNumber}'); return false;">Delete</a>
				</td>
				<td align="center">${order.orderNumber}</td>
				<td align="right">${order.amountTotal}</td>
				<td align="center">
					<input type="checkbox" ${order.paid ? "checked" : ""} onclick="update(null, 'todo=update&type=Order&id=${order.orderNumber}&att=paid');" />
				<td rowspan="2">
					<table cellspacing="0" cellpadding="4">
						<thead>
							<tr style="background-color: #BBBBBB;"><td>&nbsp;</td><td nowrap>Item #</td><td>Product</td><td>Quantity</td><td nowrap>Unit price</td><td>Amount</td></tr>
						</thead>
						<c:forEach var="item" items="${order.lineitems}">
							<tr>
								<td style="font-size: 9px; border: 1px solid #888888;">
									<a href="#" onclick="if ( ! confirm('Delete this line item?')) return false; update(null, 'todo=delete&type=Lineitem&id=${item.lineitemId}'); return false;">Delete</a>
								</td>
								<td style="border: 1px solid #888888;">${item.lineitemId}</td>
								<td style="border: 1px solid #888888;"><demo:ProductSelect lineItemId="${item.lineitemId}" currentProductId="${item.product.productNumber}"/></td>
								<td style="border: 1px solid #888888;">
								    <input type="text" value="${item.qtyOrdered}" 
								    	style="text-align: right;" size="9"
								    	onchange="if ( ! isNumber(this.value)) {alert('Invalid number'); return false;} update(null, 'todo=update&type=Lineitem&id=${item.lineitemId}&att=qtyOrdered&value=' + escape(this.value))"
								    	/>
								</td>
								<td align="right" style="border: 1px solid #888888;">$${item.productPrice}</td>
								<td align="right" style="border: 1px solid #888888;">$${item.amount}</td>
							</tr>
						</c:forEach>
						<c:if test="${order.lineitems.size() == 0}">
							<tr><td colspan="6"><center><i>No line items</i></center></td></tr>
						</c:if>
					</table>
				</td>
		
			</tr>
			<tr><td colspan="4" valign="top">
				<input type="text" size="30" style="font-size:10pt;" value="${order.notes}" 
					onchange="update(null, 'todo=update&type=Order&id=${order.orderNumber}&att=notes&value=' + escape(this.value))"
			/>
			</td></tr>
			<tr style="background-color: #CCCCCC;"><td colspan="6"><center>
				<demo:ReassignCustomerSelect orderNumber="${order.orderNumber}" exceptCustomer="${customer.name}"/>
				&nbsp;&nbsp;
				<a class="actionlink" href="/BusLogicDemoSpring/${controllerName}?name=${customer.name}&todo=create&type=Lineitem&id=${order.orderNumber}">Create line item</a>
			</center></td></tr>
			<tr><td colspan="5">&nbsp;</td></tr>

		</c:forEach>
		<c:if test="${cust.purchaseorders.size() == 0}">
			<tr><td colspan="5" style="text-align: center; font-style: italic;">No orders for this customer</td></tr>
		</c:if>
		<tr><td colspan="5">
			<a class="actionlink" href="/BusLogicDemoSpring/${controllerName}?name=${cust.name}&todo=create&type=Order">Create new order</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Show events: <input type="checkbox" ${showEvents ? 'checked' : ''} onclick="update(null, 'todo=prefs&type=showEvents&value=' + this.checked);" />
		</td></tr>
	</table>
</td>
	<c:if test="${showEvents}">
		<td>&nbsp;&nbsp;</td>
		<td valign="top">
		<demo:TxEventsTable />
		</td>
	</c:if>

	<c:if test="${fyi != null}">
		<script type="text/javascript">alert("${fyi}");</script>
	</c:if>
	
</body>
</html>