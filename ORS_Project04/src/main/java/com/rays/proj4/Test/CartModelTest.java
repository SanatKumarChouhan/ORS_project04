package com.rays.proj4.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.rays.pro4.Bean.CartBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.CartModel;

public class CartModelTest {

	public static void main(String[] args) throws ParseException, ApplicationException, DuplicateRecordException {

		insert();
	}

	public static void insert() throws ParseException, ApplicationException, DuplicateRecordException {

		CartBean bean = new CartBean();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		bean.setName("rahul");
		bean.setProduct("iPhone");
		bean.setTransactionDate(sdf.parse("01-01-2001"));
		bean.setQuantity(3);

		CartModel model = new CartModel();

		long pk = model.add(bean);

		System.out.println(pk);
	}

}
