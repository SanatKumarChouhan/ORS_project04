package com.rays.pro4.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PurchaseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.PurchaseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "PurchaseListCtl", urlPatterns = { "/ctl/PurchaseListCtl" })
public class PurchaseListCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		Map<Integer, String> map = new HashMap();
		map.put(1, "Mobile");
		map.put(2, "Laptop");
		map.put(3, "Earphones");
		map.put(4, "Speaker");
		map.put(5, "Charger");
		map.put(6, "Power Cabel");
		map.put(7, "Battery");
		map.put(8, "Power Bank");

		request.setAttribute("purchase", map);
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		PurchaseBean bean = new PurchaseBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));
		bean.setProduct(DataUtility.getString(request.getParameter("product")));
		bean.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));
		bean.setCost(DataUtility.getLong(request.getParameter("cost")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("IN DO GET...");
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		PurchaseBean bean = (PurchaseBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		// System.out.println(">>>>>>>>>>>>>>>helooo" + bean.getAppointmentDate());

		PurchaseModel model = new PurchaseModel();

		try {
			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			// ServletUtility.setBean(bean, request);

		} catch (ApplicationException e) {

			ServletUtility.handleException(e, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("PaymentListCtl doPost Start");

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		PurchaseBean bean = (PurchaseBean) populateBean(request);

		String[] ids = request.getParameterValues("ids");
		PurchaseModel model = new PurchaseModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;

		} else if (OP_NEW.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
			return;

		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
				PurchaseBean deletebean = new PurchaseBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {

						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("Purchase is Deleted Successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {

			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

		} catch (ApplicationException e) {

			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);

		ServletUtility.forward(getView(), request, response);

		return;

	}

	@Override
	protected String getView() {
		return ORSView.PURCHASE_LIST_VIEW;
	}

}
