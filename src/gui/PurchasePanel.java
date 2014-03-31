package gui;

import com.toedter.calendar.JDateChooser;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.sf.jasperreports.view.JasperViewer;
import report.PurchaseReport;
import sql.DrugSQL;
import sql.PurchaseSQL;

public class PurchasePanel extends MyPanel implements ActionListener {
	private static final long serialVersionUID = -4747761704366745047L;
	private JTable table;
	private JComboBox text_name;
	private JTextField text_batchnum;
	private JDateChooser date_validation;
	private JDateChooser date;
	private PurchaseTableModel tablemodel;
	private NewPurchaseDialog dialog;
	private JasperViewer viewer = null;

	PurchasePanel(JFrame f) {
		super(f);

		String[] Drugnames = DrugSQL.getAllDrugnames();
		this.text_name = new JComboBox(Drugnames);
		this.text_batchnum = new JTextField();
		this.date_validation = new JDateChooser();
		this.date = new JDateChooser();

		this.panel_main.setBorder(BorderFactory.createTitledBorder("进货表"));
		this.panel_queryInput.setLayout(new GridLayout(2, 2));
		this.panel_queryInput.add(new TagPanel("药品名称", this.text_name));
		this.panel_queryInput.add(new TagPanel("生产批号", this.text_batchnum));
		this.panel_queryInput.add(new TagPanel("有效期至", this.date_validation));
		this.panel_queryInput.add(new TagPanel("日        期", this.date));
	}

	private String convert(int num) {
		char[] c = { 'D', '0', '0', '0' };
		int tmp26_25 = 3;
		char[] tmp26_24 = c;
		tmp26_24[tmp26_25] = ((char) (tmp26_24[tmp26_25] + num % 10));
		num /= 10;
		int tmp42_41 = 2;
		char[] tmp42_40 = c;
		tmp42_40[tmp42_41] = ((char) (tmp42_40[tmp42_41] + num % 10));
		int tmp53_52 = 1;
		char[] tmp53_51 = c;
		tmp53_51[tmp53_52] = ((char) (tmp53_51[tmp53_52] + num / 10));
		String s = new String(c);
		return s;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.but_new) {
			this.dialog = new NewPurchaseDialog(this.frame, "新建进货记录", 900912,
					null);
			this.dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_edit) {
			if (this.table.getSelectedRow() != -1) {
				this.dialog = new NewPurchaseDialog(this.frame, "编辑进货记录",
						900913, (String) this.tablemodel.getValueAt(
								this.table.getSelectedRow(), 0));
				this.dialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
			}
		}
		if (arg0.getSource() == this.but_delete) {
			if (this.table.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(this, "确认删除吗?", "确认对话框", 0) == 0) {
					PurchaseSQL.Delete((String) this.tablemodel.getValueAt(
							this.table.getSelectedRow(), 0));
					this.tablemodel.deleteRow(this.table.getSelectedRow());
				}
			} else
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
		}

		if ((arg0.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = PurchaseReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					PurchasePanel.this.viewer = null;
				}
			});
			this.viewer.setVisible(true);
		}

		if (arg0.getSource() == this.but_query) {
			Object[] data = new Object[4];
			if (this.text_name.getSelectedIndex() <= 0)
				data[0] = "";
			else
				data[0] = convert(this.text_name.getSelectedIndex());
			if (this.text_batchnum.getText().length() == 0)
				data[1] = "";
			else
				data[1] = this.text_batchnum.getText();
			if (this.date_validation.getDate() == null)
				data[2] = null;
			else
				data[2] = new java.sql.Date(this.date_validation.getDate()
						.getTime());
			if (this.date.getDate() == null)
				data[3] = null;
			else
				data[3] = new java.sql.Date(this.date.getDate().getTime());
			Object[][] result = PurchaseSQL.Query(data);
			QueryResultDialog dialog = new QueryResultDialog(this.frame,
					result, this.tablemodel.getTablehead());
			dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_clean) {
			this.text_name.setSelectedIndex(-1);
			this.text_batchnum.setText(null);
			this.date.setDate(null);
			this.date_validation.setDate(null);
		}
		if (arg0.getSource() == this.but_show) {
			Object[][] data = PurchaseSQL.getAll();
			String[] tablehead = { "进货编号", "药品名称", "生产批号", "有效期至", "数量", "单价",
					"金额", "日期" };
			this.tablemodel = new PurchaseTableModel(data);
			this.tablemodel.setTablehead(tablehead);
			this.table = new JTable(this.tablemodel);
			JScrollPane s = new JScrollPane(this.table);
			this.panel_main.add(s, "Center");
			validate();
		}
		if (arg0.getSource() == this.but_close) {
			this.table.setVisible(false);
			this.table = null;
		}
	}

	private class NewPurchaseDialog extends MyDialog implements ActionListener {
		private static final long serialVersionUID = -2266233597853635699L;
		public static final int NEW_MODE = 900912;
		public static final int EDIT_MODE = 900913;
		public static final int DIALOG_MODE = 900914;
		public static final int INSERT_MODE = 900915;
		private JComboBox combo_name;
		private JTextField text_id;
		private JTextField text_batchnum;
		private JTextField text_amount;
		private JTextField text_price;
		private JDateChooser date;
		private JDateChooser valid_period;
		private String selectIndex;
		private int mode;

		NewPurchaseDialog(JFrame f, String s, int MODE, String selectIndex) {
			super(s);
			setBounds(200, 200, 550, 260);
			this.mode = MODE;

			String[] str_drugname = DrugSQL.getAllDrugnames();

			java.util.Date today = new java.util.Date();

			this.combo_name = new JComboBox(str_drugname);
			this.text_id = new JTextField(8);
			this.text_batchnum = new JTextField(10);
			this.text_amount = new JTextField(8);
			this.text_price = new JTextField(8);
			this.date = new JDateChooser(today);
			this.valid_period = new JDateChooser();
			if (MODE == 900913) {
				setDataToDialog(selectIndex);
				this.selectIndex = selectIndex;
			}
			this.info_panel.setBorder(BorderFactory.createTitledBorder("药品信息"));
			this.info_panel.setLayout(new GridLayout(3, 1));
			this.text_id.setText(getPurchaseID(PurchaseSQL
					.CountTodayRecord(new java.util.Date())));
			this.info_panel.add(new TagPanel("进货编号", this.text_id));
			this.info_panel.add(new TagPanel("药品名称", this.combo_name));
			this.info_panel.add(new TagPanel("生产批号", this.text_batchnum));
			JPanel text_panel2 = new JPanel();
			text_panel2.setBorder(BorderFactory.createTitledBorder("进货信息"));
			text_panel2.setLayout(new GridLayout(2, 2));
			text_panel2.add(new TagPanel("数       量", this.text_amount,
					"例如:500"));
			text_panel2.add(new TagPanel("购入价格", this.text_price, "例如:10.00"));
			text_panel2.add(new TagPanel("进货时间", this.date));
			text_panel2.add(new TagPanel("有效期至", this.valid_period));
			this.contentPane.add(text_panel2, "North");

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					PurchasePanel.NewPurchaseDialog.this.dispose();
					PurchasePanel.NewPurchaseDialog.this.text_id = (PurchasePanel.NewPurchaseDialog.this.text_batchnum = PurchasePanel.NewPurchaseDialog.this.text_amount = PurchasePanel.NewPurchaseDialog.this.text_price = null);
					PurchasePanel.NewPurchaseDialog.this.but_apply = (PurchasePanel.NewPurchaseDialog.this.but_cancel = null);
					PurchasePanel.NewPurchaseDialog.this.date = (PurchasePanel.NewPurchaseDialog.this.valid_period = null);
					PurchasePanel.NewPurchaseDialog.this.combo_name = null;
				}
			});
		}

		private String getPurchaseID(int num) {
			String str = null;
			char[] c = { '0', '0', '0' };
			num++;
			int tmp26_25 = 2;
			char[] tmp26_24 = c;
			tmp26_24[tmp26_25] = ((char) (tmp26_24[tmp26_25] + num % 10));
			num /= 10;
			int tmp42_41 = 1;
			char[] tmp42_40 = c;
			tmp42_40[tmp42_41] = ((char) (tmp42_40[tmp42_41] + num % 10));
			int tmp53_52 = 0;
			char[] tmp53_51 = c;
			tmp53_51[tmp53_52] = ((char) (tmp53_51[tmp53_52] + num / 10));
			String s = new String(c);
			SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
			str = "P" + ft.format(new java.util.Date()) + s;
			return str;
		}

		private String convert(int num) {
			char[] c = { 'D', '0', '0', '0' };
			int tmp26_25 = 3;
			char[] tmp26_24 = c;
			tmp26_24[tmp26_25] = ((char) (tmp26_24[tmp26_25] + num % 10));
			num /= 10;
			int tmp42_41 = 2;
			char[] tmp42_40 = c;
			tmp42_40[tmp42_41] = ((char) (tmp42_40[tmp42_41] + num % 10));
			int tmp53_52 = 1;
			char[] tmp53_51 = c;
			tmp53_51[tmp53_52] = ((char) (tmp53_51[tmp53_52] + num / 10));
			String str = new String(c);
			return str;
		}

		private void setDataToDialog(String id) {
			this.text_id.setText(id);
			this.text_id.setEditable(false);
			Object[] obj = PurchaseSQL.getRecord(id);
			this.combo_name.setSelectedItem(obj[1]);
			this.text_batchnum.setText((String) obj[2]);
			this.valid_period.setDate((java.util.Date) obj[3]);
			this.text_amount.setText(obj[4].toString());
			this.text_price.setText(obj[5].toString());
			this.date.setDate((java.util.Date) obj[7]);
		}

		public Object[] getDataFromDialog(int mode) {
			Object[] a = new Object[8];
			a[0] = this.text_id.getText();
			if (mode == 900915)
				a[1] = convert(this.combo_name.getSelectedIndex());
			else
				a[1] = this.combo_name.getSelectedItem();
			a[2] = this.text_batchnum.getText();
			a[3] = new java.sql.Date(this.valid_period.getDate().getTime());
			a[4] = this.text_amount.getText();
			a[5] = this.text_price.getText();
			int amount = Integer.parseInt(a[4].toString());
			double price = Double.parseDouble(a[5].toString());
			a[6] = Double.valueOf(amount * price);
			a[7] = new java.sql.Date(this.date.getDate().getTime());
			return a;
		}

		public Object[] getEditData() {
			Object[] obj = new Object[7];
			obj[0] = convert(this.combo_name.getSelectedIndex());
			obj[1] = this.text_batchnum.getText();
			obj[2] = new java.sql.Date(this.valid_period.getDate().getTime());
			obj[3] = this.text_amount.getText();
			obj[4] = this.text_price.getText();
			int amount = Integer.parseInt(obj[3].toString());
			double price = Double.parseDouble(obj[4].toString());
			obj[5] = Double.valueOf(amount * price);
			obj[6] = new java.sql.Date(this.date.getDate().getTime());
			return obj;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == this.but_apply) {
				if (this.mode == 900912) {
					Object[] a = getDataFromDialog(900915);
					PurchaseSQL.Insert(a);
					PurchasePanel.this.tablemodel
							.addRow(PurchasePanel.this.dialog
									.getDataFromDialog(900914));
				}
				if (this.mode == 900913) {
					Object[] obj = getEditData();
					PurchaseSQL.Update(this.selectIndex, obj);
					PurchasePanel.this.tablemodel
							.updateRow(PurchasePanel.this.table
									.getSelectedRow(),
									PurchasePanel.this.dialog
											.getDataFromDialog(900914));
				}
				dispose();
			}

			if (arg0.getSource() == this.but_cancel)
				dispose();
		}
	}

	private class PurchaseTableModel extends MyTableModel {
		private static final long serialVersionUID = -3448229643907612271L;

		PurchaseTableModel(Object[][] data) {
			this.rows = new ArrayList();
			for (int i = 0; i < data.length; i++)
				this.rows.add(data[i]);
		}
	}
}
