package report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SupplerReportDateSorce extends BaseReportDateSorce {
	SupplerReportDateSorce() {
		this.map = new HashMap();
		Connection con = null;
		Statement sql = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection(url, "", "");
			sql = con.createStatement();
			result = sql.executeQuery("SELECT * FROM Factory");
			this.list = new ArrayList();
			while (result.next()) {
				HashMap att = new HashMap();
				att.put("fac_id", result.getString(1));
				att.put("fac_name", result.getString(2));
				att.put("address", result.getString(3));
				att.put("verification_date", result.getString(8));
				att.put("linkman", result.getString(4));
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
