package report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sql.Statistic;

class BalanceReportDateSorce extends BaseReportDateSorce {
	BalanceReportDateSorce() {
		this.map = new HashMap();
		this.list = new ArrayList();
		Object[][] obj = Statistic.Balance();
		for (int i = 0; i < obj.length; i++) {
			HashMap att = new HashMap();
			att.put("drug_name", obj[i][0].toString());
			att.put("batch_num", obj[i][1].toString());
			att.put("vali_date", obj[i][2].toString().substring(0, 10));
			att.put("balance", obj[i][3].toString());
			this.list.add(att);
			att = null;
		}
	}
}
