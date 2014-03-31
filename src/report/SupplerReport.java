package report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

public class SupplerReport {
	public static JasperViewer printReport() {
		try {
			JasperCompileManager
					.compileReportToFile(".\\report\\report_fac.jrxml");
			JasperFillManager.fillReportToFile(".\\report\\report_fac.jasper",
					null, new SupplerReportDateSorce());
			return new JasperViewer(".\\report\\report_fac.jrprint", false,
					false);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
