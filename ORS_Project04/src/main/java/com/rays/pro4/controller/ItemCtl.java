package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.ItemBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.ItemModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "ItemCtl", urlPatterns = "/ctl/ItemCtl")
public class ItemCtl extends BaseCtl {

	Logger log = Logger.getLogger(ItemCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> map = new HashMap();

		map.put("Electronics", "Electronics");
		map.put("Fashion", "Fashion");
		map.put("Fitness", "Fitness");
		map.put("Games", "Games");
		map.put("Griceries", "Griceries");
		System.out.println("item Map list>>>>> " + map);
		request.setAttribute("mapList", map);

	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		System.out.println("uctl Validate");
		log.debug("ItemCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("title"))) {
			request.setAttribute("title", PropertyReader.getValue("error.require", "Title"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("overView"))) {
			request.setAttribute("overView", PropertyReader.getValue("error.require", "OverView"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.require", "Cost"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.integer", "Cost"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.require", "Purchase Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.date", "Purchase Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("category"))) {
			request.setAttribute("category", PropertyReader.getValue("error.require", "Category"));
			pass = false;
		}
		log.debug("ItemCtl Method validate Ended");

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		ItemBean bean = new ItemBean();

		bean.setId(DataUtility.getInt(request.getParameter("id")));
		bean.setTitle(DataUtility.getString(request.getParameter("title")));
		bean.setOverView(DataUtility.getString(request.getParameter("overView")));
		bean.setCost(DataUtility.getInt(request.getParameter("cost")));
		bean.setPurchaseDate(DataUtility.getDate(request.getParameter("purchaseDate")));
		bean.setCategory(DataUtility.getString(request.getParameter("category")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("ItemCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		ItemModel model = new ItemModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Item Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			ItemBean bean;
			try {
				bean = model.findByPk(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("ItemCtl Method doGet Ended");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("uctl Do Post");

		log.debug("ItemCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		ItemModel model = new ItemModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			ItemBean bean = (ItemBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Item is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

//					bean = model.findByPk(pk);
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Item is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */ }
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Item is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			ItemBean bean = (ItemBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.ITEM_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.ITEM_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("ItemCtl Method doPostEnded");

	}

	@Override
	protected String getView() {
		return ORSView.ITEM_VIEW;
	}

}
