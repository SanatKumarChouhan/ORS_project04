<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.controller.ItemCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
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
<title>Item List</title>

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
			yearRange : '1980:2024',
		//dateFormat:'yy-mm-dd'
		});
	});
</script>
</head>
<body>

	<jsp:useBean id="bean" class="com.rays.pro4.Bean.ItemBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>

	<center>

		<form action="<%=ORSView.ITEM_CTL%>" method="post">

			<%
				HashMap map = (HashMap) request.getAttribute("mapList");
			%>


			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Item </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Item </font></th>
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
					<th align="left">Title <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="title" placeholder="Enter title"
						size="25" value="<%=DataUtility.getStringData(bean.getTitle())%>"></td>
					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("title", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Over View <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="overView"
						placeholder="Enter Over View" size="25"
						value="<%=DataUtility.getStringData(bean.getOverView())%>"></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("overView", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Cost <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="cost" placeholder="Enter Cost"
						size="25" value="<%=DataUtility.getStringData(bean.getCost())%>"
						<%=(bean.getId() > 0) ? "" : ""%>></td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("cost", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Purchase Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="purchaseDate"
						placeholder="Enter Purchase Date" size="25" readonly="readonly"
						id="udatee"
						value="<%=DataUtility.getDateString(bean.getPurchaseDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("purchaseDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Category <span style="color: red">*</span> :
					</th>
					<td>
						<%
							String htmlList = HTMLUtility.getList("category", DataUtility.getString(bean.getCategory()), map);
						%>
						<%=htmlList%> <%-- <%=HTMLUtility.getList("category", DataUtility.getStringData(bean.getCategory()), map)%> --%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("category", request)%></font></td>
				</tr>

				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &nbsp; <input type="submit"
						name="operation" value="<%=ItemCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=ItemCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &nbsp; <input type="submit"
						name="operation" value="<%=ItemCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=ItemCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</center>

	<%@ include file="Footer.jsp"%>

</body>
</html>