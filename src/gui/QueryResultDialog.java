package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class QueryResultDialog extends JDialog {
	private static final long serialVersionUID = -1970235066194001753L;
	private JTable table;

	QueryResultDialog(JFrame f, Object[][] data, String[] head) {
		super(f, "²éÑ¯½á¹û", true);
		setBounds(100, 300, 800, 200);
		setLayout(new BorderLayout());
		this.table = new JTable(data, head);
		fitTableColumns(this.table);
		this.table.setPreferredScrollableViewportSize(new Dimension(780, 190));
		JScrollPane s = new JScrollPane(this.table);
		add(s, "Center");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				QueryResultDialog.this.dispose();
				QueryResultDialog.this.table = null;
			}
		});
	}

	private static void fitTableColumns(JTable table) {
		JTableHeader header = table.getTableHeader();
		int rowCount = table.getRowCount();
		Enumeration columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(
					column.getIdentifier());
			int width = (int) table
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(table,
							column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) table
						.getCellRenderer(row, col)
						.getTableCellRendererComponent(table,
								table.getValueAt(row, col), false, false, row,
								col).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column);
			column.setWidth(width + table.getIntercellSpacing().width);
		}
	}
}
