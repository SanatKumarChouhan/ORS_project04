package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.ItemBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class ItemModel {

	Logger log = Logger.getLogger(ItemModel.class);

	public long nextPk() throws DatabaseException {

		log.debug("Model nextPK Started");
		long pk = 0;
		String sql = "select max(id) from st_item";
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

			throw new DatabaseException("Exception: exception in getting next pk from database");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK Started");
		return pk + 1;
	}

	public long add(ItemBean bean) throws ApplicationException, DuplicateRecordException {

		String sql = "insert into st_item values(?,?,?,?,?,?)";
		long pk = 0;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getOverView());
			pstmt.setLong(4, bean.getCost());
			pstmt.setDate(5, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setString(6, bean.getCategory());

			int a = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				e.printStackTrace();
				log.error("Database Exception ...", e);
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("exception in rollBack" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Add End");
		return pk;
	}

	public void delete(ItemBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_item where id=?";
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
			log.error("DataBase Exception", e);
			try {

				e.printStackTrace();
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Exception in Deleting Items");
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	public void update(ItemBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_item set title=?, over_view=?, cost=?, purchase_date=?, category=? where id=?";

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getOverView());
			pstmt.setLong(3, bean.getCost());
			pstmt.setDate(4, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setString(5, bean.getCategory());
			pstmt.setLong(6, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception", e);
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Exception in Updating Items");
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End ");
	}

	public ItemBean findByPk(long pk) throws ApplicationException {

		String sql = "select * from st_item where id=?";
		ItemBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new ItemBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getString(6));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception in getting find by Pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public List search(ItemBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(ItemBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_item where true");

		if (bean != null) {
			if (bean.getTitle() != null && bean.getTitle().length() > 0) {
				sql.append(" and title like '" + bean.getTitle() + "'%");
			}
			if (bean.getOverView() != null && bean.getOverView().length() > 0) {
				sql.append(" and over_view like '" + bean.getOverView() + "'%");
			}
			if (bean.getCost() > 0) {
				sql.append(" and cost = " + bean.getCost());
			}
			if (bean.getPurchaseDate() != null && bean.getPurchaseDate().getTime() > 0) {
				Date d = new java.sql.Date(bean.getPurchaseDate().getTime());
				sql.append(" and purchase_date =" + d);
			}
			if (bean.getCategory() != null && bean.getCategory().length() > 0) {
				sql.append(" and category like '" + bean.getCategory() + "'%");
			}
			if (bean.getId() > 0) {
				sql.append(" and id =" + bean.getId());
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println(sql);
		List list = new ArrayList();

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				bean = new ItemBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getString(6));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception: Exception in Searching Items");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public List list() throws ApplicationException {

		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Start");
		StringBuffer sql = new StringBuffer("select * from st_item where true");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		List list = new ArrayList();
		Connection conn = null;
		ItemBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ItemBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getString(6));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception in getting List");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
