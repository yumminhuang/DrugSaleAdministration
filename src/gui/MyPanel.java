package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MyPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1044845950433922522L;
	protected JButton but_new;
	protected JButton but_edit;
	protected JButton but_delete;
	protected JButton but_report;
	protected JButton but_query;
	protected JButton but_clean;
	protected JButton but_show;
	protected JButton but_close;
	protected JFrame frame;
	protected JPanel panel_main;
	protected JPanel panel_queryInput;

	MyPanel(JFrame F) {
		this.frame = F;
		setLayout(new BorderLayout());
		JPanel panel_but1 = new JPanel();
		JPanel panel_but2 = new JPanel();
		JPanel panel_query = new JPanel();
		panel_query.setLayout(new BorderLayout());
		this.panel_main = new JPanel();
		this.panel_queryInput = new JPanel();

		this.but_show = new JButton("打开表格");
		this.but_show.addActionListener(this);
		this.but_close = new JButton("关闭表格");
		this.but_close.addActionListener(this);
		this.but_new = new JButton("新建记录");
		this.but_new.addActionListener(this);
		this.but_edit = new JButton("修改记录");
		this.but_edit.setToolTipText("先选择一条记录");
		this.but_edit.addActionListener(this);
		this.but_delete = new JButton("删除记录");
		this.but_delete.setToolTipText("先选择一条记录");
		this.but_delete.addActionListener(this);
		this.but_report = new JButton("导出报表");
		this.but_report.addActionListener(this);
		this.but_query = new JButton("查询");
		this.but_query.addActionListener(this);
		this.but_clean = new JButton("清空");
		this.but_clean.addActionListener(this);
		panel_but1.add(this.but_show);
		panel_but1.add(this.but_close);
		panel_but1.add(this.but_new);
		panel_but1.add(this.but_edit);
		panel_but1.add(this.but_delete);
		panel_but1.add(this.but_report);
		panel_but2.add(this.but_query);
		panel_but2.add(this.but_clean);

		this.panel_main.setLayout(new BorderLayout());
		this.panel_main.add(panel_but1, "South");

		panel_query.setBorder(BorderFactory.createTitledBorder("查询"));
		panel_query.add(this.panel_queryInput, "Center");
		panel_query.add(panel_but2, "South");

		add(this.panel_main, "Center");
		add(panel_query, "South");
	}

	protected void fitTableColumns(JTable table) {
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

	public void actionPerformed(ActionEvent arg0) {
	}
}
