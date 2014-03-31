package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplerSQL extends BaseSQL {
	public static String[] getAllFactoryname() {
		int n = CountRecord();
		String[] factory_name = new String[n + 1];
		factory_name[0] = "";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT fac_name FROM Factory ORDER BY fac_id ASC");
			int j = 1;
			while (rs.next()) {
				factory_name[j] = rs.getString(1);
				j++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return factory_name;
	}

	public static String getFactoryname(String id) {
		String factory_name = null;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT fac_name FROM Factory WHERE fac_id = '"
							+ id + "'");
			while (rs.next()) {
				factory_name = rs.getString(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return factory_name;
	}

	public static int CountRecord() {
		int c = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM Factory");
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

			ResultSet rs = sql.executeQuery("SELECT * FROM Factory");
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 8; j++) {
					if (j == 8)
						obj[i][(j - 1)] = rs.getDate(j);
					else
						obj[i][(j - 1)] = rs.getString(j);
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
		Object[] obj = new Object[8];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM Factory WHERE fac_id = ?");

			sql.setString(1, id);
			ResultSet rs = sql.executeQuery();
			int j = 1;
			while (rs.next()) {
				if (j == 8)
					obj[(j - 1)] = rs.getDate(j);
				else
					obj[(j - 1)] = rs.getString(j);
				j++;
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void Insert(Object[] o) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			String str = "INSERT INTO Factory VALUES ('" + o[0] + "','" + o[1]
					+ "','" + o[2] + "','" + o[3] + "','" + o[4] + "','" + o[5]
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
			String str = "UPDATE Factory SET fac_name ='" + o[0]
					+ "',address='" + o[1] + "',linkman='" + o[2] + "',phone='"
					+ o[3] + "',acc_address='" + o[4] + "',acc='" + o[5]
					+ "',verification_date=#" + o[6] + "# WHERE fac_id = '"
					+ id + "'";
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
			sql.executeUpdate("DELETE FROM Factory WHERE fac_id = '" + id + "'");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Object[][] Query(Object[] s) {
		Object[][] obj = (Object[][]) null;
		StringBuffer sb = new StringBuffer("SELECT * FROM Factory WHERE ");
		boolean b = false;
		if (s[0] != "") {
			sb.append("fac_name = '" + s[0] + "'");
			b = true;
		}
		if (s[1] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("verification_date < #" + s[1] + "#");
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
					if (j == 8)
						obj[i][(j - 1)] = rs.getDate(j);
					else
						obj[i][(j - 1)] = rs.getString(j);
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
