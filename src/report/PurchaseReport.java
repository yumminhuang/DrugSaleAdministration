package report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

public class PurchaseReport {
	public static JasperViewer printReport() {
		try {
			JasperCompileManager
					.compileReportToFile(".\\report\\report_pur.jrxml");
			JasperFillManager.fillReportToFile(".\\report\\report_pur.jasper",
					null, new PurchaseReportDateSorce());
			return new JasperViewer(".\\report\\report_pur.jrprint", false,
					false);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
