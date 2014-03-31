package report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class HospitalReportDateSorce extends BaseReportDateSorce {
	private static String[] str_level = { "三甲", "三乙", "二甲", "二乙", "社区" };

	HospitalReportDateSorce() {
		this.map = new HashMap();
		Connection con = null;
		Statement sql = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection(url, "", "");
			sql = con.createStatement();
			result = sql.executeQuery("SELECT * FROM Hospital");
			this.list = new ArrayList();
			while (result.next()) {
				HashMap att = new HashMap();
				att.put("hos_id", result.getString(1));
				att.put("hos_name", result.getString(2));
				att.put("level", str_level[(result.getInt(3) - 1)]);
				att.put("address", result.getString(4));
				this.list.add(att);
				att = null;
			}
			result.close();
			con.close();
			sql.close();
			this.iter = this.list.iterator();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
