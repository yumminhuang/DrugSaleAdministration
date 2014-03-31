package gui;

import java.util.ArrayList;

class BalanceTableModel extends MyTableModel {
	private static final long serialVersionUID = -3448229643907612271L;

	BalanceTableModel(Object[][] data) {
		this.rows = new ArrayList<Object[]>();
		for (int i = 0; i < data.length; i++)
			this.rows.add(data[i]);
	}
}
