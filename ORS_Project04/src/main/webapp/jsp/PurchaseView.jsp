<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.PurchaseCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Purchase Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2000:2024',

		});
	});
</script>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.PurchaseBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>

	<div align="center">

		<form action="<%=ORSView.PURCHASE_CTL%>" method="post">

			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Purchase </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Purchase </font></th>
					</tr>
					<%
						}
					%>
				</h1>
				<h3>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				HashMap map = (HashMap) request.getAttribute("purchase");
			%>

			<input type="hidden" name="id" value="<%=bean.getId()%>">

			<table>
				<tr>
					<th align="left">Quantity <span style="color: red">*</span> :
					</th>

					<td><input type="text" name="quantity"
						placeholder="Enter Quantity " size="25"
						value="<%=(DataUtility.getStringData(bean.getQuantity()).equals("0") ? "" : bean.getQuantity())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("quantity", request)%></td>
				</tr>

				<tr>
					<th style="padding: 1px"></th>
				</tr>
				<tr>
					<th align="left">Product <span style="color: red">*</span> :
					</th>
					<td><%=HTMLUtility.getList2("product", String.valueOf(bean.getProduct()), map)%>

						<font color="red"> <%=ServletUtility.getErrorMessage("product", request)%></font></br>

					</td>
				</tr>

				<tr>
					<th style="padding: 1px"></th>
				</tr>


				<tr>
					<th align="left">OrderDate <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="orderDate"
						placeholder="Enter orderDate" size="25" id="datepicker"
						readonly="readonly"
						value="<%=DataUtility.getDateString(bean.getOrderDate())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("orderDate", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 1px"></th>
				</tr>
				<tr>
					<th align="left">Cost <span style="color: red">*</span> :
					</th>

					<td><input type="text" name="cost" placeholder="Enter Cost "
						size="25"
						value="<%=(DataUtility.getStringData(bean.getCost()).equals("0") ? "" : bean.getCost())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("cost", request)%></td>
				</tr>

				<th style="padding: 1px"></th>
				</tr>

				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=PurchaseCtl.OP_UPDATE%>">
						&nbsp; &nbsp; <input type="submit" name="operation"
						value="<%=PurchaseCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=PurchaseCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=PurchaseCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="Footer.jsp"%>
</body>
</html>