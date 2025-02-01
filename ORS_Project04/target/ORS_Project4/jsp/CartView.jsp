<%@page import="com.rays.pro4.controller.CartCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>CartView</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2000:2024',
		//dateFormat:'yy-mm-dd'
		});
	});
</script>
</head>
<body>

	<jsp:useBean id="bean" class="com.rays.pro4.Bean.CartBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>



	<center>

		<form action="<%=ORSView.CART_CTL%>" method="post">

			<%
				Map m = (Map) request.getAttribute("cart");
			%>


			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Cart </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Cart </font></th>
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
			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>
				<tr>
					<th align="left">Name <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="name"
						placeholder="Enter Name" size="25"
						value="<%=DataUtility.getStringData(bean.getName())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("name", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Product <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="product"
						placeholder="Enter Product Name" size="25"
						value="<%=DataUtility.getStringData(bean.getProduct())%>"></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("product", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Transaction Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="tDate"
						placeholder="Select Transaction Date" size="25"
						readonly="readonly" id="udatee"
						value="<%=DataUtility.getStringData(bean.getTransactionDate())%>"></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("tDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Quantity <span style="color: red">*</span> :
					</th>
					<td><input type="quantity" name="quantity"
						placeholder="Enter quantity" size="25"
						value="<%=DataUtility.getStringData(bean.getQuantity())%>"></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("quantity", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<%-- <tr>
					<th align="left">Product <span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList("gender", String.valueOf(bean.getProduct()), m);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("gender", request)%></font></td>
				</tr> --%>

				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=CartCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=CartCtl.OP_CANCEL%>"></td>
					<%
						} else {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=CartCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=CartCtl.OP_RESET%>"></td>
					<%
						}
					%>
				</tr>
			</table>
		</form>



	</center>

	</form>

</body>
</html>