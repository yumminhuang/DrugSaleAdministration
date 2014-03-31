package report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sql.DrugSQL;
import sql.HospitalSQL;

class SaleReportDateSorce extends BaseReportDateSorce {
	SaleReportDateSorce() {
		this.map = new HashMap();
		Connection con = null;
		Statement sql = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection(url, "", "");
			sql = con.createStatement();
			result = sql.executeQuery("SELECT * FROM Sale");
			this.list = new ArrayList();
			DecimalFormat Dformat = new DecimalFormat("0.00");
			while (result.next()) {
				HashMap att = new HashMap();
				att.put("sale_id", result.getString(1));
				att.put("drug_name", DrugSQL.getDrugname(result.getString(2)));
				att.put("batch_num", result.getString(3));
				att.put("amount", result.getString(7));
				att.put("price", Dformat.format(result.getDouble(8)));
				att.put("cost", Dformat.format(result.getDouble(9)));
				att.put("hos_name",
						HospitalSQL.getHospitalname(result.getString(6)));
				att.put("sale_date", result.getString(10).substring(0, 10));
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
