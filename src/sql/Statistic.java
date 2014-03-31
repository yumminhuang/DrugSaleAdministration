package sql;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class Statistic extends BaseSQL {
	public static Object[][] Balance() {
		Object[][] Pobj = (Object[][]) null;
		Object[][] Sobj = (Object[][]) null;
		String sql_query1 = "SELECT * FROM purchase_amount";
		String sql_query2 = "SELECT * FROM sale_amount";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query1);
			rs.last();
			int n1 = rs.getRow();
			rs.beforeFirst();
			Pobj = new Object[n1][4];
			int i = 0;
			while (rs.next()) {
				Pobj[i][0] = DrugSQL.getDrugname(rs.getString(1));
				Pobj[i][1] = rs.getString(2);
				Pobj[i][2] = rs.getDate(3);
				Pobj[i][3] = Integer.valueOf(rs.getInt(4));
				i++;
			}
			rs = sql.executeQuery(sql_query2);
			rs.last();
			int n2 = rs.getRow();
			rs.beforeFirst();
			Sobj = new Object[n2][4];
			int j = 0;
			while (rs.next()) {
				Sobj[j][0] = DrugSQL.getDrugname(rs.getString(1));
				Sobj[j][1] = rs.getString(2);
				Sobj[j][2] = rs.getDate(3);
				Sobj[j][3] = Integer.valueOf(rs.getInt(4));
				j++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < Pobj.length; i++)
			for (int j = 0; j < Sobj.length; j++)
				if ((Pobj[i][0].equals(Sobj[j][0]))
						&& (Pobj[i][1].equals(Sobj[j][1])))
					Pobj[i][3] = Integer.valueOf(Integer.parseInt(Pobj[i][3]
							.toString())
							- Integer.parseInt(Sobj[j][3].toString()));
		Sobj = (Object[][]) null;
		return Pobj;
	}

	public static Object[][] Statitic_Drug() {
		Object[][] obj = (Object[][]) null;

		String sql_query = "SELECT * FROM drug_statistic";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			obj = new Object[n][2];
			int i = 0;
			System.out.println(n);
			while (rs.next()) {
				obj[i][0] = DrugSQL.getDrugname(rs.getString(1));
				obj[i][1] = rs.getString(2);
				i++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object[][] Statitic_Hospital() {
		Object[][] obj = (Object[][]) null;
		String sql_query = "SELECT * FROM hos_statistic";
		try {
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement sql = conn.createStatement(1004, 1007);

			ResultSet rs = sql.executeQuery(sql_query);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			obj = new Object[n][2];
			int i = 0;
			while (rs.next()) {
				obj[i][0] = HospitalSQL.getHospitalname(rs.getString(1));
				obj[i][1] = Double.valueOf(rs.getDouble(2));
				i++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	private static String StrProf(int num, boolean isSale) {
		String str1 = "SELECT SUM(Sale.cost) FROM Sale WHERE sale_date >= ";
		String str2 = " AND sale_date < ";
		String str3 = "SELECT SUM(Purchase.cost) FROM Purchase WHERE pur_date >= ";
		String str4 = " AND pur_date < ";

		int[] month = new int[6];
		String[] strmonth = new String[7];
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		int y = c.get(1);
		int m = c.get(2) + 1;

		for (int i = 5; i >= 0; m--) {
			month[i] = ((m + 12) % 12 != 0 ? (m + 12) % 12 : 12);
			i--;
		}
		for (i = 0; i < 6; i++) {
			StringBuffer s = new StringBuffer();
			if (month[i] > c.get(2) + 1) {
				s.insert(2, month[i]);
				s.insert(1, y - 1);
			} else {
				s.insert(2, month[i]);
				s.insert(1, y);
			}
			strmonth[i] = new String(s);
		}
		StringBuffer s = new StringBuffer();
		s.insert(2, month[5] + 1);
		if (month[5] == 12)
			s.insert(1, y + 1);
		else
			s.insert(1, y);
		strmonth[6] = new String(s);
		if (isSale) {
			return str1 + strmonth[num] + str2 + strmonth[(num + 1)];
		}
		return str3 + strmonth[num] + str4 + strmonth[(num + 1)];
	}
}
