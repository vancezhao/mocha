package com.coral.foundation.security.basic.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ClientSession;
import org.springframework.transaction.annotation.Transactional;

import com.coral.foundation.persistence.BaseDao;
import com.coral.foundation.report.ReportConfiguration;
import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.security.model.MochaReport;
import com.coral.foundation.security.model.ReportColumn;
import com.coral.foundation.security.model.ReportTable;

/**
 * MochaReportDao is a auto Generated class. Please don't modify it.
 */
public class MochaReportDao extends BaseDao<MochaReport> {

	private static Connection conn;
	private static DatabaseMetaData meta;
	private static Map<String, ReportTable> fullDBInfo;

	@Override
	public Class<MochaReport> getEntityClass() {
		return MochaReport.class;
	}

	public void saveMochaReport(MochaReport mochaReport) {
		getEntityManager().persist(mochaReport);
	}

	@Transactional
	public ArrayList executeReport(MochaReport mochaReport) {
		// CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		// final Query query = entityManager.createNativeQuery(mochaReport
		// .getReportPureQuery());

		// entityManager.getTransaction().begin();
		// java.sql.Connection cnn =
		// entityManager.unwrap(java.sql.Connection.class);

		ResultSet rs = null;
		Statement st;
		ArrayList<String[]> reportRowDataReslt = new ArrayList<String[]>();
		try {
			Connection conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(mochaReport.getReportPureQuery());

			ResultSetMetaData rsmd;
			try {
				rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				// output the column name
				String[] columnNames = new String[columnCount];
				for (int i = 1; i <= columnCount; i++) {

					System.out.print(rsmd.getColumnName(i));
					columnNames[i - 1] = rsmd.getColumnName(i);
					System.out.print("(" + rsmd.getColumnTypeName(i) + ")");
					System.out.print(" | ");
				}
				reportRowDataReslt.add(columnNames);

				// output the row data
				while (rs.next()) {
					String[] columnValues = new String[columnCount];
					for (int i = 1; i <= columnCount; i++) {
						System.out.print(rs.getString(i) + " | ");
						columnValues[i - 1] = rs.getString(i);
					}
					reportRowDataReslt.add(columnValues);
					System.out.println();
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

		}

		return reportRowDataReslt;
	}

	private Connection getConnection() {
		// Session session = entityManager.unwrap(Session.class);
		// SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) session.getSessionFactory();
		// ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		// try {
		// return connectionProvider.getConnection();
		// }
		// catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return null;

		java.sql.Connection connection = getEntityManager().unwrap(java.sql.Connection.class);
		return connection;
	}

	@Transactional
	public Map<String, ReportTable> loadDBBasicInfo() {
		if (fullDBInfo == null) {
			meta = getDBMetaData();
			try {
				HashMap<String, ReportTable> reportTables = new HashMap<String, ReportTable>();
				// ouput the table
				ResultSet rs = getMeta().getTables(null, null, null, new String[] { "TABLE" });
				System.out.println("Start to list shcema");
				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					System.out.println("tableName=" + tableName);

					ReportTable reportTable = new ReportTable();
					reportTable.setTableName(tableName);

					// primary key
					ResultSet primaryKeyRS = getMeta().getPrimaryKeys(null, null, tableName);
					java.util.List list = new java.util.ArrayList();
					// while (primaryKeyRS.next()) {
					// String columnName = primaryKeyRS
					// .getString("COLUMN_NAME");
					// System.out.println("getPrimaryKeys(): columnName="
					// + columnName);
					// }

					ResultSet collumnKeyRs = getMeta().getColumns(conn.getCatalog(), null, tableName, null);
					while (collumnKeyRs.next()) {
						String columnName = collumnKeyRs.getString("COLUMN_NAME");
						ReportColumn reportColumn = new ReportColumn();
						reportColumn.setColumnName(columnName);
						reportTable.getReportColumns().add(reportColumn);
					}
					reportTables.put(tableName, reportTable);
				}
				fullDBInfo = loadDBAdvanceInfo(reportTables);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error when parsing the db infor");
				e.printStackTrace();
			}
			finally {
				try {
					conn.close();
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return fullDBInfo;

	}

	public Map<String, ReportTable> loadDBAdvanceInfo(Map<String, ReportTable> reportTables) {
		try {

			for (Object reportTableName : reportTables.keySet().toArray()) {

				ResultSet foreignKeys = getMeta().getImportedKeys(conn.getCatalog(), null, reportTableName.toString());

				while (foreignKeys.next()) {
					ReportColumn reportColumn = new ReportColumn();
					reportColumn.setColumnUseMode(ReportConfiguration.ReportColumnType.ForeignKeyRefernceColumn.toString());
					String fkTableName = foreignKeys.getString("FKTABLE_NAME");
					String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");

					String pkTableName = foreignKeys.getString("PKTABLE_NAME");
					String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

					// find the reference table
					if (reportTables.containsKey(pkTableName)) {
						reportColumn.setColumnName(fkColumnName);
						reportColumn.setReferenceTableName(pkTableName);
						reportColumn.setReferenceColumnName(pkColumnName);
						reportTables.get(reportTableName).getReportColumns().add(reportColumn);
					}

					System.out.println(fkTableName + "." + fkColumnName + " -> " + pkTableName + "." + pkColumnName);
				}
			}

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reportTables;

	}

	@SuppressWarnings("deprecation")
	public DatabaseMetaData getDBMetaData() {
		if (conn == null) {
			// Session hibernateSession = entityManager.unwrap(Session.class);
			// setConn(hibernateSession.connection());
		}

		DatabaseMetaData meta = null;
		try {
			meta = conn.getMetaData();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meta;
	}

	@SuppressWarnings("deprecation")
	public Connection getConn() {
		if (getConn() == null) {
			// Session hibernateSession = entityManager.unwrap(Session.class);
			// setConn(hibernateSession.connection());
		}
		return conn;
	}

	public List<MochaReport> findByCreator(BasicUser basicUser) {
		Query query = getEntityManager().createQuery("from MochaReport m where m.creator = :creator", MochaReport.class);
		query.setParameter("creator", basicUser);
		List<MochaReport> mochaReports = query.getResultList();
		if (mochaReports.size() > 0) {
			return mochaReports;
		}
		return null;
	}

	public static void setConn(Connection conn) {
		MochaReportDao.conn = conn;
	}

	public static DatabaseMetaData getMeta() {
		return meta;
	}

	public static void setMeta(DatabaseMetaData meta) {
		MochaReportDao.meta = meta;
	}

}
