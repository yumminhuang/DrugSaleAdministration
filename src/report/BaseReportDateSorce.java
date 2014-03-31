package report;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class BaseReportDateSorce implements JRDataSource {
	protected static String url = "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=.\\database\\DrugAdministation.accdb";

	protected static String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	protected List<Map<String, String>> list = null;
	protected Iterator<Map<String, String>> iter = null;
	protected HashMap<?, ?> map = null;

	BaseReportDateSorce() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
	}

	public Object getFieldValue(JRField arg0) throws JRException {
		return this.map.get(arg0.getName());
	}

	public boolean next() throws JRException {
		if (this.iter.hasNext()) {
			this.map = ((HashMap) this.iter.next());
			return true;
		}
		return false;
	}
}
