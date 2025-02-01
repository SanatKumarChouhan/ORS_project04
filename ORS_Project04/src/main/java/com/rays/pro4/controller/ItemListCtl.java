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
import com.rays.pro4.Bean.ItemBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.ItemModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "ItemListCtl", urlPatterns = "/ctl/ItemListCtl")
public class ItemListCtl extends BaseCtl {

	Logger log = Logger.getLogger(ItemListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

		log.debug("ItemListCtl ki prload chali");
//		ItemModel model = new ItemModel();
//
//		try {
//			List itemList = model.list(0, 0);
//
//			request.setAttribute("ItemList", itemList);
//		} catch (ApplicationException e) {
//			e.printStackTrace();
//		}

		Map<String, String> map = new HashMap();

		map.put("Electronics", "Electronics");
		map.put("Fashion", "Fashion");
		map.put("Fitness", "Fitness");
		map.put("Games", "Games");
		map.put("Griceries", "Griceries");
		System.out.println("item Map list>>>>> " + map);
		request.setAttribute("mapList", map);

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
		log.debug("ItemListCtl doGet Start");
		System.out.println("ItemListCtl doGet Start");
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		ItemBean bean = (ItemBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

//	        get the selected checkbox ids array for delete list

		// String[] ids = request.getParameterValues("ids");

		ItemModel model = new ItemModel();

		try {
			list = model.search(bean, pageNo, pageSize);
			System.out.println("list" + list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			// ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("ItemListCtl doGet End");
		System.out.println("ItemListCtl doGet End");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("ItemListCtl doPost Start");
		System.out.println("ItemListCtl doPost Start");

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));

		ItemBean bean = (ItemBean) populateBean(request);

		// get the selected checkbox ids array for delete list

		String[] ids = request.getParameterValues("ids");

		ItemModel model = new ItemModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ITEM_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ITEM_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
				ItemBean deletebean = new ItemBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("Item is Deleted Successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {

			list = model.search(bean, pageNo, pageSize);
			System.out.println(list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("ItemListCtl doGet End");
		System.out.println("ItemListCtl doGet End");

	}

	@Override
	protected String getView() {
		return ORSView.ITEM_LIST_VIEW;
	}

}
