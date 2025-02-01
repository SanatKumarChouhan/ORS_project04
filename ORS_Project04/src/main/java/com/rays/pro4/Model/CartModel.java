package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.CartBean;
import com.rays.pro4.Bean.UserBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class CartModel {

	public int nextPk() throws DatabaseException {

		int pk = 0;
		String sql = "select max(id) from st_cart";
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception: exception in getting pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public int add(CartBean bean) throws ApplicationException, DuplicateRecordException {

		int pk = 0;
		String sql = "insert into st_cart values(?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			pk = nextPk();

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getProduct());
			pstmt.setDate(4, new Date(bean.getTransactionDate().getTime()));
			pstmt.setInt(5, bean.getQuantity());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int a = pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("exception in adding to add data" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void delete(CartBean bean) throws ApplicationException {

		String sql = "delete from st_cart where id=?";

		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception in deleting cart " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void update(CartBean bean) throws ApplicationException {

		String sql = "update st_cart set name=?, product=?, transaction_date=?, quantity=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getProduct());
			pstmt.setDate(3, new Date(bean.getTransactionDate().getTime()));
			pstmt.setInt(4, bean.getQuantity());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("exception in updation card");
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public List search(CartBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_cart where 1=1");

		if (bean != null) {

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getProduct() != null && bean.getProduct().length() > 0) {
				sql.append(" and product like '" + bean.getProduct() + "%'");
			}
			if (bean.getTransactionDate() != null && bean.getTransactionDate().getTime() > 0) {
				Date d = new java.sql.Date(bean.getTransactionDate().getTime());
				sql.append(" and tansaction_date like '" + d + "%'");
			}
			if (bean.getQuantity() > 0) {
				sql.append(" and quantity =" + bean.getQuantity());
			}
			if (bean.getId() > 0) {
				sql.append(" and id =" + bean.getId());
			}

			if (pageSize > 0) {

				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + ", " + pageSize);
			}
		}

		List list = new ArrayList();

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				bean = new CartBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setProduct(rs.getString(3));
				bean.setTransactionDate(rs.getDate(4));
				bean.setQuantity(rs.getInt(5));
			

				list.add(bean);
			}

			rs.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception: Exception in Search User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public CartBean findByPK(long pk) throws ApplicationException {
		String sql = "select * from st_cart where id=?";
		CartBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CartBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setProduct(rs.getString(3));
				bean.setTransactionDate(rs.getDate(4));
				bean.setQuantity(rs.getInt(5));
				

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_cart where 1=1");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("preload........" + sql);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				CartBean bean = new CartBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setProduct(rs.getString(3));
				bean.setTransactionDate(rs.getDate(4));
				bean.setQuantity(rs.getInt(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
