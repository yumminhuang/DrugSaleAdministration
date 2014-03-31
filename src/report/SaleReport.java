package report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

public class SaleReport {
	public static JasperViewer printReport() {
		try {
			JasperCompileManager
					.compileReportToFile(".\\report\\report_sale.jrxml");
			JasperFillManager.fillReportToFile(".\\report\\report_sale.jasper",
					null, new SaleReportDateSorce());
			return new JasperViewer(".\\report\\report_sale.jrprint", false,
					false);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
