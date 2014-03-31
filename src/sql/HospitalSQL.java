package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HospitalSQL extends BaseSQL {
	private static String[] str_level = { "三甲", "三乙", "二甲", "二乙", "社区" };

	public static String[] getAllHospitalname() {
		int n = CountRecord();
		String[] hospital_name = new String[n + 1];
		hospital_name[0] = "";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT hos_name FROM Hospital ORDER BY hos_id ASC");
			int j = 1;
			while (rs.next()) {
				hospital_name[j] = rs.getString(1);
				j++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hospital_name;
	}

	public static String getHospitalname(String id) {
		String name = null;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT hos_name FROM Hospital WHERE hos_id = '"
							+ id + "'");
			while (rs.next()) {
				name = rs.getString(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public static int CountRecord() {
		int c = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM Hospital");
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
		Object[][] obj = new Object[n][6];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql.executeQuery("SELECT * FROM Hospital");
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 6; j++) {
					if (j == 3)
						obj[i][(j - 1)] = str_level[(rs.getInt(j) - 1)];
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
		Object[] obj = new Object[6];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM Hospital WHERE hos_id = ?");

			sql.setString(1, id);
			ResultSet rs = sql.executeQuery();
			int j = 1;
			while (rs.next()) {
				if (j == 3)
					obj[(j - 1)] = str_level[(rs.getInt(j) - 1)];
				else
					obj[(j - 1)] = rs.getObject(j);
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
			String str = "INSERT INTO Hospital VALUES ('" + o[0] + "','" + o[1]
					+ "','" + o[2] + "','" + o[3] + "','" + o[4] + "','" + o[5]
					+ "')";
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
			String str = "UPDATE Hospital SET hos_name ='" + o[0]
					+ "',`level`=" + o[1] + ",address='" + o[2] + "',linkman='"
					+ o[3] + "',phone='" + o[4] + "' WHERE drug_id = '" + id
					+ "'";
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
			sql.executeUpdate("DELETE FROM Hospital WHERE hos_id = '" + id
					+ "'");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Object[][] Query(Object[] s) {
		Object[][] obj = (Object[][]) null;
		StringBuffer sb = new StringBuffer("SELECT * FROM Hospital WHERE ");
		boolean b = false;
		if (s[0] != "") {
			sb.append("hos_name = '" + s[0] + "'");
			b = true;
		}
		if (s[1] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("level = " + s[1]);
			b = true;
		}
		String sql_query = new String(sb);
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			obj = new Object[n][6];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 6; j++) {
					if (j == 3)
						obj[i][(j - 1)] = str_level[(rs.getInt(j) - 1)];
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
