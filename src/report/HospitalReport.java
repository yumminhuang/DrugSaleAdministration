package report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

public class HospitalReport {
	public static JasperViewer printReport() {
		try {
			JasperCompileManager
					.compileReportToFile(".\\report\\report_hos.jrxml");
			JasperFillManager.fillReportToFile(".\\report\\report_hos.jasper",
					null, new HospitalReportDateSorce());
			return new JasperViewer(".\\report\\report_hos.jrprint", false,
					false);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
