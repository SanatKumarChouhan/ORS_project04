<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.controller.PurchaseListCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Bean.PurchaseBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Purchase page</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

</head>
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
			yearRange : '2000:20024',

		});
	});
</script>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.PurchaseBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="<%=ORSView.PURCHASE_LIST_CTL%>" method="post">
		<center>

			<div align="center">
				<h1>Purchase List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			<%
				HashMap map = (HashMap) request.getAttribute("purchase");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<PurchaseBean> it = list.iterator();
				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<div>

					<td align="center"><label>Quantity</font> :
					</label> <input type="text" name="quantity"
						placeholder="Please Enter Quantity"
						value="<%=ServletUtility.getParameter("quantity", request)%>">
						<br></td>
				</div>

				<div>
					<td><label>Product</font> :
					</label> <%=HTMLUtility.getList2("product", String.valueOf(bean.getProduct()), map)%></td>

				</div>

				<td align="center"><label>OrderDate</font> :
				</label> <input type="text" name="orderDate" id="datepicker"
					placeholder="Enter orderDate" readonly="readonly"
					value="<%=ServletUtility.getParameter("orderDate", request)%>">
					<div>

						<td align="center"><label>Cost</font>:
						</label><input type="text" name="cost" size="25" placeholder="Enter Cost "
							value="<%=ServletUtility.getParameter("cost", request)%>"
							id="mobileError"> <br></td>

					</div>
				<td><input type="submit" name="operation"
					value="<%=PurchaseListCtl.OP_SEARCH%>"> <input
					type="submit" name="operation"
					value="<%=PurchaseListCtl.OP_RESET%>"></td>


			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">


				<tr style="background: orange">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Quantity</th>
					<th>Product</th>
					<th>OrderDate</th>
					<th>Cost</th>
					<th>Edit</th>
				</tr>
				<%
					while (it.hasNext()) {
							bean = it.next();
				%>
				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>

					<td><%=index++%></td>
					<td><%=bean.getQuantity()%></td>
					<td><%=map.get(Integer.parseInt(bean.getProduct()))%></td>
					<td><%=DataUtility.getDateString(bean.getOrderDate())%></td>
					<td><%=bean.getCost()%></td>

					<td><a href="PurchaseCtl?id=<%=bean.getId()%>">Edit</td>
				</tr>
				<%
					}
				%>

				<table width="100%">

					<tr>
						<th></th>
						<%
							if (pageNo == 1) {
						%>
						<td><input type="submit" name="operation" disabled="disabled"
							value="<%=PurchaseListCtl.OP_PREVIOUS%>"></td>
						<%
							} else {
						%>
						<td><input type="submit" name="operation"
							value="<%=PurchaseListCtl.OP_PREVIOUS%>"></td>
						<%
							}
						%>

						<td><input type="submit" name="operation"
							value="<%=PurchaseListCtl.OP_DELETE%>"></td>
						<td align="center"><input type="submit" name="operation"
							value="<%=PurchaseListCtl.OP_NEW%>"></td>

						<td align="right"><input type="submit" name="operation"
							value="<%=PurchaseListCtl.OP_NEXT%>"
							<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



					</tr>
				</table>
				<%
					}
					if (list.size() == 0) {
				%>
				<td align="center"><input type="submit" name="operation"
					value="<%=PurchaseListCtl.OP_BACK%>"></td>


				<%
					}
				%>

				<input type="hidden" name="pageNo" value="<%=pageNo%>">
				<input type="hidden" name="pageSize" value="<%=pageSize%>">

				</form>
				</br>

				</br>
				</br>
				</br>
				</br>
				</br>
				</br>

				</center>
				<%@include file="Footer.jsp"%>
</body>
</html>