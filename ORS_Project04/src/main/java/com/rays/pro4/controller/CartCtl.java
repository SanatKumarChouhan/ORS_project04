package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CartBean;
import com.rays.pro4.Bean.CourseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CartModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "CartCtl", urlPatterns = { "/ctl/CartCtl" })
public class CartCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		System.out.println("cart ctl ki preload chali>>>>>");
		CartModel cmodel = new CartModel();

		try {
			List clist = cmodel.list(0, 0);
			request.setAttribute("clist", clist);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		CartModel model = new CartModel();
		Map<Integer, String> map = new HashMap();

		map.put(1, "Tablet");
		map.put(2, "Mobile");
		map.put(3, "Laptop");
		map.put(4, "Fridge");
		map.put(5, "Speaker");
		map.put(6, "Ac");

		request.setAttribute("card", map);

		System.out.println("cart ctl ki preload end>>>>>");
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		System.out.println("cart ctl ki validate started>>>>>");

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.name", "name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "product"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("tDate"))) {
			request.setAttribute("tDate", PropertyReader.getValue("error.require", "Transaction Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("tDate"))) {
			request.setAttribute("tDate", PropertyReader.getValue("error.date", "Transaction Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		}

		System.out.println("cart ctl ki validate end>>>>>");
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		CartBean bean = new CartBean();

		bean.setId(DataUtility.getInt(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setProduct(DataUtility.getString(request.getParameter("product")));
		bean.setTransactionDate(DataUtility.getDate(request.getParameter("tDate")));
		System.out.println("cart ctl par tdate get hui>>>>" + bean.getTransactionDate());
		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("cart ctl ki doget start>>>>>");
		String op = DataUtility.getString(request.getParameter("operation"));

		System.out.println("cart ctl me operation get hau>>>>" + op);

		// get Model
		CartModel model = new CartModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {
			CartBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("cart ctl ki dopost started>>>>");
		String op = DataUtility.getString(request.getParameter("operation"));

		System.out.println("cart ctl me operation get hua>>>>" + op);

		// Get Model
		CartModel model = new CartModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println("cart ctl me id get hua>>>>" + id);

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			CartBean bean = (CartBean) populateBean(request);
			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Cart is Successfully Updated", request);

				} else {
					long pk = model.add(bean);
//					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Cart is Successfully Added", request);

					bean.setId(pk);
				}
//				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("cart Name Already Exist", request);

			}
		} /*
			 * else if (OP_DELETE.equalsIgnoreCase(op)) { CourseBean bean =(CourseBean)
			 * populateBean(request); try { model.delete(bean);;
			 * ServletUtility.redirect(ORSView.COURSE_CTL, request, response); return; }
			 * catch (ApplicationException e) { log.error(e);
			 * ServletUtility.handleException(e, request, response); return ; } }
			 */
		else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CART_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		return ORSView.CART_VIEW;
	}

}
