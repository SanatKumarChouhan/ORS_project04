package com.rays.pro4.Model;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.PurchaseBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class PurchaseModel {

	public int nextPK() throws DatabaseException {

		System.out.println("Next pk == ");

		String sql = "select max(id) from st_purchase";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
				System.out.println("Next pk == " + pk);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;

	}

	public long add(PurchaseBean bean) throws ApplicationException, DuplicateRecordException {

		System.out.println("ADD Method== ");

		String sql = "insert into st_purchase values (?,?,?,?,?)";

		Connection conn = null;

		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pk);
			pstmt.setInt(2, bean.getQuantity());
			pstmt.setString(3, bean.getProduct());
			pstmt.setDate(4, new java.sql.Date(bean.getOrderDate().getTime()));
			pstmt.setLong(5, bean.getCost());

			int a = pstmt.executeUpdate();
			System.out.println("ho gyua re" + a);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();

				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());

			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;

	}

	public void delete(PurchaseBean bean) throws ApplicationException {

		String sql = "delete from st_purchase where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println(i + "data deleted");
			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void update(PurchaseBean bean) throws ApplicationException, DuplicateRecordException {

		String sql = "update st_purchase set total_quantity=?, product=?, order_date=?, total_cost=? where id=?";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bean.getQuantity());
			pstmt.setString(2, bean.getProduct());
			pstmt.setDate(3, new java.sql.Date(bean.getOrderDate().getTime()));
			pstmt.setLong(4, bean.getCost());
			pstmt.setLong(5, bean.getId());

			pstmt.executeUpdate();
			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	/*
	 * public List search(PurchaseBean bean) throws ApplicationException { return
	 * search(bean); }
	 */

	public List search(PurchaseBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_purchase where 1=1 ");
		if (bean != null) {
			if (bean != null && bean.getId() > 0) {

				sql.append(" and id = " + bean.getId());

			}
			if (bean.getQuantity() > 0) {
				sql.append(" and total_quantity = " + bean.getQuantity());
			}

			if (bean.getProduct() != null && bean.getProduct().length() > 0) {
				sql.append(" and product like '" + bean.getProduct() + "%'");
			}
			if (bean.getOrderDate() != null && bean.getOrderDate().getTime() > 0) {
				Date d = new java.sql.Date(bean.getOrderDate().getTime());
				sql.append(" and order_date like '" + d + "%'");
			}

			if (bean.getCost() > 0) {
				sql.append(" and total_cost = " + bean.getCost());
			}

			if (pageSize > 0) {

				pageNo = (pageNo - 1) * pageSize;

				sql.append(" Limit " + pageNo + ", " + pageSize);

			}
		}
		System.out.println("sql>>>>>>>>>> " + sql.toString());

		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PurchaseBean();

				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setProduct(rs.getString(3));
				bean.setOrderDate(rs.getDate(4));
				bean.setCost(rs.getLong(5));

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

	public PurchaseBean findByPK(long pk) throws ApplicationException {

		String sql = "select * from st_purchase where id=?";
		PurchaseBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PurchaseBean();
				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setProduct(rs.getString(3));
				bean.setOrderDate(rs.getDate(4));
				bean.setCost(rs.getLong(5));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();

			throw new ApplicationException("Exception : Exception in getting Payment by pk");
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
		StringBuffer sql = new StringBuffer("select * from st_purchase");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseBean bean = new PurchaseBean();
				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setProduct(rs.getString(3));
				bean.setOrderDate(rs.getDate(4));
				bean.setCost(rs.getLong(5));
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
