<%@page import="com.rays.pro4.Bean.CartBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.CartListCtl"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<meta charset="ISO-8859-1">
<title>CartListView</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

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
			yearRange : '1980:2002',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>
</head>
<body>

	<jsp:useBean id="bean" class="com.rays.pro4.Bean.CartBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>

	<form action="<%=ORSView.CART_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Cart List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

				<%
					/* List clist = (List) request.getAttribute("clist"); */

					int next = DataUtility.getInt(request.getAttribute("nextList").toString());
				%>

				<%
					int pageNo = ServletUtility.getPageNo(request);
					int pageSize = ServletUtility.getPageSize(request);
					int index = ((pageNo - 1) * pageSize) + 1;

					List list = ServletUtility.getList(request);
					Iterator<CartBean> it = list.iterator();

					if (list.size() != 0) {
				%>

				<table width="100%" align="center">
					<tr>
						<th></th>




						<td align="center"><label>Name</font> :
						</label> <input type="text" name="name" placeholder="Enter Name"
							value="<%=ServletUtility.getParameter("name", request)%>">

							<label></font> </label> <!-- <label>Name</font> :
						</label> --> <%-- <%=HTMLUtility.getList("cname", bean.getId(), clist)%> --%>


							<input type="submit" name="operation"
							value="<%=CartListCtl.OP_SEARCH%>"> &nbsp; <input
							type="submit" name="operation" value="<%=CartListCtl.OP_RESET%>">

						</td>
					</tr>
				</table>

				<br>

				<table border="1" width="100%" align="center" cellpadding=6px
					cellspacing=".2">
					<tr style="background: orange">
						<th><input type="checkbox" id="select_all" name="select">Select
							All</th>

						<th>S.No.</th>
						<th>Name</th>
						<th>Product</th>
						<th>Transaction Date</th>
						<th>Quantity</th>
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
						<td><%=bean.getName()%></td>
						<td><%=bean.getProduct()%></td>
						<td><%=bean.getTransactionDate()%></td>
						<td><%=bean.getQuantity()%></td>
						<td><a href="CartCtl?id=<%=bean.getId()%>">Edit</a></td>
					</tr>
					<%
						}
					%>
				</table>

				<table width="100%">
					<tr>
						<th></th>
						<%
							if (pageNo == 1) {
						%>
						<td><input type="submit" name="operation" disabled="disabled"
							value="<%=CartListCtl.OP_PREVIOUS%>"></td>
						<%
							} else {
						%>
						<td><input type="submit" name="operation"
							value="<%=CartListCtl.OP_PREVIOUS%>"></td>
						<%
							}
						%>

						<td><input type="submit" name="operation"
							value="<%=CartListCtl.OP_DELETE%>"></td>
						<td><input type="submit" name="operation"
							value="<%=CartListCtl.OP_NEW%>"></td>


						<td align="right"><input type="submit" name="operation"
							value="<%=CartListCtl.OP_NEXT%>"
							<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>
					</tr>
				</table>

				<%
					}
					if (list.size() == 0) {
				%>
				<td align="center"><input type="submit" name="operation"
					value="<%=CartListCtl.OP_BACK%>"></td>
				<%
					}
				%>

				<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
					type="hidden" name="pageSize" value="<%=pageSize%>">
		</center>

	</form>

</body>
</html>