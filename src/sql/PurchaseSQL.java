package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PurchaseSQL extends BaseSQL {
	public static int CountRecord() {
		int c = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM Purchase");
			while (rs.next()) {
				c = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	public static int CountTodayRecord(java.util.Date date) {
		int c = 0;
		java.sql.Date d = new java.sql.Date(date.getTime());
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			PreparedStatement sql = conn
					.prepareStatement("SELECT COUNT(*) FROM Purchase WHERE pur_date = ?");
			sql.setDate(1, d);

			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				c = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	public static Object[][] getAll() {
		int n = CountRecord();
		Object[][] obj = new Object[n][8];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql.executeQuery("SELECT * FROM Purchase");
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 8; j++) {
					if (j == 2)
						obj[i][(j - 1)] = DrugSQL.getDrugname(rs.getString(j));
					else if ((j == 4) || (j == 8))
						obj[i][(j - 1)] = rs.getDate(j);
					else
						obj[i][(j - 1)] = rs.getObject(j);
				}
				i++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object[] getRecord(String id) {
		Object[] object = new Object[8];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM Purchase WHERE pur_id = ?");

			sql.setString(1, id);
			ResultSet rs = sql.executeQuery();
			int j = 1;
			while (rs.next()) {
				if (j == 2)
					object[(j - 1)] = DrugSQL.getDrugname(rs.getString(j));
				else if ((j == 4) || (j == 8))
					object[(j - 1)] = rs.getDate(j);
				else
					object[(j - 1)] = rs.getObject(j);
				j++;
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static void Insert(Object[] o) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			String str = "INSERT INTO Purchase VALUES ('" + o[0] + "','" + o[1]
					+ "','" + o[2] + "',#" + o[3] + "#,'" + o[4] + "','" + o[5]
					+ "','" + o[6] + "',#" + o[7] + "#)";
			sql.executeUpdate(str);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void Update(String id, Object[] o) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			String str = "UPDATE Purchase SET drug_id ='" + o[0]
					+ "',batch_num='" + o[1] + "',vali_date=#" + o[2]
					+ "#,amount=" + o[3] + ",price=" + o[4] + ",cost=" + o[5]
					+ ",pur_date=#" + o[6] + "# WHERE pur_id = '" + id + "'";
			sql.executeUpdate(str);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void Delete(String id) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			sql.executeUpdate("DELETE FROM Purchase WHERE pur_id = '" + id
					+ "'");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Object[][] Query(Object[] s) {
		Object[][] obj = (Object[][]) null;
		StringBuffer sb = new StringBuffer("SELECT * FROM Purchase WHERE ");
		boolean b = false;
		if (s[0] != "") {
			sb.append("drug_id = '" + s[0] + "'");
			b = true;
		}
		if (s[1] != "") {
			if (b)
				sb.append(" AND ");
			sb.append("batch_num = '" + s[1] + "'");
			b = true;
		}
		if (s[2] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("vali_date < #" + s[2] + "#");
			b = true;
		}
		if (s[3] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("pur_date < #" + s[3] + "#");
		}
		String sql_query = new String(sb);
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			obj = new Object[n][8];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 8; j++) {
					if (j == 2)
						obj[i][(j - 1)] = DrugSQL.getDrugname(rs.getString(j));
					else if ((j == 4) || (j == 8))
						obj[i][(j - 1)] = rs.getDate(j);
					else
						obj[i][(j - 1)] = rs.getObject(j);
				}
				i++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
