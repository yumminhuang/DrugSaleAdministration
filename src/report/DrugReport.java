package report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

public class DrugReport {
	public static JasperViewer printReport() {
		try {
			JasperCompileManager
					.compileReportToFile(".\\report\\report_drug.jrxml");
			JasperFillManager.fillReportToFile(".\\report\\report_drug.jasper",
					null, new DrugReportDateSorce());
			return new JasperViewer(".\\report\\report_drug.jrprint", false,
					false);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
