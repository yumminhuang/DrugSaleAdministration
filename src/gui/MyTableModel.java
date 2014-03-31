package gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1794408037623962800L;
	String[] tablehead;
	protected ArrayList<Object[]> rows;

	public void setTablehead(String[] s) {
		this.tablehead = s;
	}

	public String[] getTablehead() {
		return this.tablehead;
	}

	public int getColumnCount() {
		return this.tablehead.length;
	}

	public int getRowCount() {
		return this.rows.size();
	}

	public Object getValueAt(int row, int col) {
		Object[] obj = (Object[]) this.rows.get(row);
		return obj[col];
	}

	public String getColumnName(int col) {
		return this.tablehead[col];
	}

	public void addRow(Object[] obj) {
		this.rows.add(obj);
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	public void deleteRow(int row) {
		this.rows.remove(row);
		fireTableRowsDeleted(row - 1, row - 1);
	}

	public void updateRow(int row, Object[] obj) {
		this.rows.set(row, obj);
		fireTableRowsUpdated(row - 1, row - 1);
	}
}
