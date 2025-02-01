package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CartBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.CartModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "CartListCtl", urlPatterns = { "/ctl/CartListCtl" })
public class CartListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CartListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

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
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		CartBean bean = new CartBean();

		bean.setId(DataUtility.getInt(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setProduct(DataUtility.getString(request.getParameter("product")));
		bean.setTransactionDate(DataUtility.getDate(request.getParameter("tdate")));
		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CartList ki doGet");
		System.out.println("CartList ki doGet");

		List list = null;
		List nextList = null;
		
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		
		System.out.println("pageNo"+pageNo+"pageSize"+pageSize);

		CartBean bean = (CartBean) populateBean(request);

		String op = request.getParameter("operation");

		CartModel model = new CartModel();

		try {
			list = model.search(bean, pageNo, pageSize);
			
			System.out.println("CartList>>" + list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextList", nextList.size());

			if (list == null && list.size() == 0) {
				ServletUtility.setErrorMessage("No Record Found......!", request);
				System.out.println("No Record Found......!");
			}

			ServletUtility.setList(list, request);
//			ServletUtility.setBean(bean, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("CartListCtl ki doPost Started");
		List list = null;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(request.getParameter("pageSize")) : pageSize;

		String op = request.getParameter("operation");
		System.out.println("CartListCtl me operation get hua>>>" + op);
		String[] ids = request.getParameterValues("ids");
		CartBean bean = (CartBean) populateBean(request);
		CartModel model = new CartModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CART_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;

			if (ids != null && ids.length > 0) {

				CartBean deleteBean = new CartBean();
				for (String id : ids) {
					deleteBean.setId(DataUtility.getInt(id));
					try {
						model.delete(deleteBean);
					} catch (ApplicationException e) {
						e.printStackTrace();
						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("Data Delete SuccessFully....!!!", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select At Least One Record.....!!", request);
			}
		}
		try {
			list = model.search(bean, pageNo, pageSize);
			System.out.println("cartctl>>>>" + list);
			nextList = model.search(bean, pageNo + 1, pageSize);
			request.setAttribute("nextList", nextList.size());

		} catch (Exception e) {
			ServletUtility.handleException(e, request, response);
			return;
		}

		if (list == null && list.size() == 0) {
			ServletUtility.setErrorMessage("No Record Found......!", request);
			System.out.println("No Record Found......!");
		}
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.CART_LIST_VIEW;
	}

}
