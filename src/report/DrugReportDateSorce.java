package report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sql.SupplerSQL;

class DrugReportDateSorce extends BaseReportDateSorce {
	private final String[] str_quality = { "原研类", "专利类", "GMP类", "GMP单独定价类",
			"其它" };

	DrugReportDateSorce() {
		this.map = new HashMap();
		Connection con = null;
		Statement sql = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection(url, "", "");
			sql = con.createStatement();
			result = sql
					.executeQuery("SELECT * FROM Drug ORDER BY drug_id ASC");
			HashMap att;
			for (this.list = new ArrayList(); result.next(); att = null) {
				att = new HashMap();
				att.put("drug_id", result.getString(1));
				att.put("drug_name", result.getString(2));
				att.put("app_num", result.getString(4));
				att.put("spec", result.getString(5));
				att.put("pack", result.getString(6));
				att.put("quality", this.str_quality[result.getInt(7)]);
				att.put("factory",
						SupplerSQL.getFactoryname(result.getString(8)));
				this.list.add(att);
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
