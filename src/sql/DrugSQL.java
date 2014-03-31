package sql;

import fun.Backup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DrugSQL extends BaseSQL {
	private static String[] str_quality = { "原研类", "专利类", "GMP类", "GMP单独定价类",
			"其它" };

	public static String[] getAllDrugnames() {
		int n = CountRecord();
		String[] drug_name = new String[n + 1];
		drug_name[0] = "";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT drug_name FROM Drug ORDER BY drug_id ASC");
			int j = 1;
			while (rs.next()) {
				drug_name[j] = rs.getString(1);
				j++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drug_name;
	}

	public static String getDrugname(String id) {
		String name = null;
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT drug_name FROM Drug WHERE drug_id = '"
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

			ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM Drug");
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
		Object[][] obj = new Object[n][9];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();

			ResultSet rs = sql
					.executeQuery("SELECT * FROM Drug ORDER BY drug_id ASC");
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 9; j++) {
					if (j == 7) {
						obj[i][(j - 1)] = str_quality[(rs.getInt(j) - 1)];
					} else if (j == 8)
						obj[i][(j - 1)] = SupplerSQL.getFactoryname(rs
								.getString(j));
					else if (j == 9)
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
		Object[] obj = new Object[9];
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			PreparedStatement sql = conn
					.prepareStatement("SELECT * FROM Drug WHERE drug_id = ?");

			sql.setString(1, id);
			ResultSet rs = sql.executeQuery();
			int j = 1;
			while (rs.next()) {
				if (j == 7) {
					obj[(j - 1)] = str_quality[(rs.getInt(j) - 1)];
				} else if (j == 8) {
					String str = rs.getString(j);
					obj[(j - 1)] = SupplerSQL.getFactoryname(str);
				} else if (j == 9) {
					obj[(j - 1)] = rs.getDate(j);
				} else {
					obj[(j - 1)] = rs.getObject(j);
				}
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
			String str = "INSERT INTO Drug VALUES ('" + o[0] + "','" + o[1]
					+ "','" + o[2] + "','" + o[3] + "','" + o[4] + "','" + o[5]
					+ "'," + o[6] + ",'" + o[7] + "',#" + o[8] + "#)";
			Backup.recordLog(str);
			sql.executeUpdate(str);
			conn.close();
			Backup.recordLog(true);
		} catch (SQLException e) {
			Backup.recordLog(false);
		}
	}

	public static void Update(String id, Object[] o) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			String str = "UPDATE Drug SET drug_name ='" + o[0] + "',con_name='"
					+ o[1] + "',app_num='" + o[2] + "',spec='" + o[3]
					+ "',pack='" + o[4] + "',quality=" + o[5] + ",factory='"
					+ o[6] + "',verification=#" + o[7] + "# WHERE drug_id = '"
					+ id + "'";
			Backup.recordLog(str);
			sql.executeUpdate(str);
			conn.close();
			Backup.recordLog(true);
		} catch (SQLException e) {
			Backup.recordLog(false);
		}
	}

	public static void Delete(String id) {
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement();
			String str = "DELETE FROM Drug WHERE drug_id = '" + id + "'";
			Backup.recordLog(str);
			sql.executeUpdate(str);
			conn.close();
			Backup.recordLog(true);
		} catch (SQLException e) {
			Backup.recordLog(false);
		}
	}

	public static Object[][] Query(Object[] s) {
		Object[][] obj = (Object[][]) null;
		StringBuffer sb = new StringBuffer("SELECT * FROM Drug WHERE ");
		boolean b = false;
		if (s[0] != "") {
			sb.append("drug_name = '" + s[0] + "'");
			b = true;
		}
		if (s[1] != "") {
			if (b)
				sb.append(" AND ");
			sb.append("factory = '" + s[1] + "'");
			b = true;
		}
		if (s[2] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("quality = " + s[2]);
			b = true;
		}
		if (s[3] != null) {
			if (b)
				sb.append(" AND ");
			sb.append("verification < #" + s[3] + "#");
		}
		String sql_query = new String(sb);
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			obj = new Object[n][9];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= 9; j++) {
					if (j == 7)
						obj[i][(j - 1)] = str_quality[(rs.getInt(j) - 1)];
					else if (j == 8)
						obj[i][(j - 1)] = SupplerSQL.getFactoryname(rs
								.getString(j));
					else if (j == 9)
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
