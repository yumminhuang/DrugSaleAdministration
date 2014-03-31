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

class PurchaseReportDateSorce extends BaseReportDateSorce {
	PurchaseReportDateSorce() {
		this.map = new HashMap();
		Connection con = null;
		Statement sql = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection(url, "", "");
			sql = con.createStatement();
			result = sql.executeQuery("SELECT * FROM Purchase");
			this.list = new ArrayList();
			DecimalFormat Dformat = new DecimalFormat("0.00");
			while (result.next()) {
				HashMap att = new HashMap();
				att.put("pur_id", result.getString(1));
				att.put("drug_name", DrugSQL.getDrugname(result.getString(2)));
				att.put("batch_num", result.getString(3));
				att.put("amount", result.getString(5));
				att.put("price", Dformat.format(result.getDouble(6)));
				att.put("cost", Dformat.format(result.getDouble(7)));
				att.put("pur_date", result.getString(8).substring(0, 10));
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
