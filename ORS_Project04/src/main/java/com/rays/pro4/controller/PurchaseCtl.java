package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PurchaseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.PurchaseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "PurchaseCtl", urlPatterns = { "/ctl/PurchaseCtl" })
public class PurchaseCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "quantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("quantity"))) {
			request.setAttribute("quantity", "Quantity Must Contain Integer Only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "product"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.require", "orderDate"));
			pass = false;

		} else if (!DataValidator.isDate(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.date", "orderDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.require", "cost"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("cost"))) {
			request.setAttribute("cost", "Cost Must Contain Integer Only");
			pass = false;
		}

		/*
		 * else if (!DataValidator.isName(request.getParameter("decease"))) {
		 * request.setAttribute("decease", "customer  must contains alphabet only");
		 * pass = false; }
		 */
		return pass;

	}

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
		String op = DataUtility.getString(request.getParameter("operation"));

		PurchaseModel model = new PurchaseModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println("order Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			PurchaseBean bean;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out.println("Operation is === " + op);

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>" + id + op);

		PurchaseModel model = new PurchaseModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PurchaseBean bean = (PurchaseBean) populateBean(request);

			try {
				if (id > 0) {

					model.update(bean);
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Purchase  is successfully Updated", request);
				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					ServletUtility.setSuccessMessage("Purchase is successfully Added", request);

					bean.setId(pk);

				}

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			PurchaseBean bean = (PurchaseBean) populateBean(request);
			try {
				model.delete(bean);

				ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.PURCHASE_VIEW;
	}
}
