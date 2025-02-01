<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.ItemListCtl"%>
<%@page import="com.rays.pro4.Bean.ItemBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ItemListView</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Item List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2002',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>
</head>
<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="com.rays.pro4.Bean.ItemBean"
		scope="request"></jsp:useBean>
	<form action="<%=ORSView.ITEM_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Item List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>
			</div>

			<%
				/* List itemList = (List) request.getAttribute("ItemList"); */

				Map mapList = (Map) request.getAttribute("mapList");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<ItemBean> it = list.iterator();

				if (list.size() != 0) {
			%>

			<table width="100%" align="center">
				<tr>
					<th></th>

					<td align="center"><label>Title:</label> <input type="text"
						name="title" placeholder="Enter Title Here"
						value="<%=ServletUtility.getParameter("title", request)%>" /> <label>Cost:
					</label> <input type="text" name="cost" placeholder="Enter cost"
						value="<%=ServletUtility.getParameter("cost", request)%>">
						&nbsp; &nbsp; <input type="submit" name="operation"
						value="<%=ItemListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=ItemListCtl.OP_RESET%>">

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
					<th>Title</th>
					<th>Over View</th>
					<th>Cost</th>
					<th>Purchase Date</th>
					<th>Category</th>
					<th>Edit</th>

				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>



				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>" /></td>
					<td><%=index++%></td>
					<td><%=bean.getTitle()%></td>
					<td><%=bean.getOverView()%></td>
					<td><%=bean.getCost()%></td>
					<td><%=bean.getPurchaseDate()%></td>
					<td><%=bean.getCategory()%></td>
					<td><a href="ItemCtl?id=<%=bean.getId()%>">Edit</a></td>
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
						value="<%=ItemListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_NEW%>"></td>


					<td align="right"><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=ItemListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

		</center>
		</br> </br> </br> </br> </br> </br> </br>
	</form>


	<%@include file="Footer.jsp"%>
</body>
</html>