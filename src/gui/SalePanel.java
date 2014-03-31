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
import report.SaleReport;
import sql.DrugSQL;
import sql.HospitalSQL;
import sql.SaleSQL;

public class SalePanel extends MyPanel implements ActionListener {
	private static final long serialVersionUID = -7425926606565947799L;
	private JComboBox combo_hospital;
	private JTable table;
	private JComboBox text_name;
	private JTextField text_batchnum;
	private JDateChooser date_validation;
	private JDateChooser date;
	private SaleTableModel tablemodel;
	private NewSaleDialog dialog;
	private JasperViewer viewer = null;

	SalePanel(JFrame f) {
		super(f);
		String[] str_hospital = HospitalSQL.getAllHospitalname();
		String[] Drugnames = DrugSQL.getAllDrugnames();
		this.combo_hospital = new JComboBox(str_hospital);
		this.text_name = new JComboBox(Drugnames);

		this.text_batchnum = new JTextField();
		this.date_validation = new JDateChooser();
		this.date = new JDateChooser();

		this.panel_main.setBorder(BorderFactory.createTitledBorder("销售表"));
		this.panel_queryInput.setLayout(new GridLayout(2, 3));
		this.panel_queryInput.add(new TagPanel("药品名称", this.text_name));
		this.panel_queryInput.add(new TagPanel("生产批号", this.text_batchnum));
		this.panel_queryInput.add(new TagPanel("有效期至", this.date_validation));
		this.panel_queryInput.add(new TagPanel("医院名称", this.combo_hospital));
		this.panel_queryInput.add(new TagPanel("日        期", this.date));
	}

	private String convert(int num, char choose) {
		char[] c = { 'D', '0', '0', '0' };
		if (choose == 'H')
			c[0] = 'H';
		int tmp37_36 = 3;
		char[] tmp37_35 = c;
		tmp37_35[tmp37_36] = ((char) (tmp37_35[tmp37_36] + num % 10));
		num /= 10;
		int tmp53_52 = 2;
		char[] tmp53_51 = c;
		tmp53_51[tmp53_52] = ((char) (tmp53_51[tmp53_52] + num % 10));
		int tmp64_63 = 1;
		char[] tmp64_62 = c;
		tmp64_62[tmp64_63] = ((char) (tmp64_62[tmp64_63] + num / 10));
		String str = new String(c);
		return str;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.but_new) {
			this.dialog = new NewSaleDialog(this.frame, "新建销售记录", 900912, null);
			this.dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_edit) {
			if (this.table.getSelectedRow() != -1) {
				this.dialog = new NewSaleDialog(this.frame, "编辑销售记录", 900913,
						(String) this.tablemodel.getValueAt(
								this.table.getSelectedRow(), 0));
				this.dialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
			}
		}
		if (arg0.getSource() == this.but_delete) {
			if (this.table.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(this, "确认删除吗?", "确认对话框", 0) == 0) {
					SaleSQL.Delete((String) this.tablemodel.getValueAt(
							this.table.getSelectedRow(), 0));
					this.tablemodel.deleteRow(this.table.getSelectedRow());
				}
			} else
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
		}

		if ((arg0.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = SaleReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					SalePanel.this.viewer = null;
				}
			});
			this.viewer.setVisible(true);
		}

		if (arg0.getSource() == this.but_query) {
			Object[] data = new Object[5];
			if (this.text_name.getSelectedIndex() <= 0)
				data[0] = "";
			else
				data[0] = convert(this.text_name.getSelectedIndex(), 'D');
			if (this.text_batchnum.getText().length() == 0)
				data[1] = "";
			else
				data[1] = this.text_batchnum.getText();
			if (this.date_validation.getDate() == null)
				data[2] = null;
			else
				data[2] = new java.sql.Date(this.date_validation.getDate()
						.getTime());
			if (this.combo_hospital.getSelectedIndex() <= 0)
				data[3] = "";
			else
				data[3] = convert(this.combo_hospital.getSelectedIndex(), 'H');
			if (this.date.getDate() == null)
				data[4] = null;
			else
				data[4] = new java.sql.Date(this.date.getDate().getTime());
			Object[][] result = SaleSQL.Query(data);
			QueryResultDialog dialog = new QueryResultDialog(this.frame,
					result, this.tablemodel.getTablehead());
			dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_clean) {
			this.text_name.setSelectedIndex(-1);
			this.text_batchnum.setText(null);
			this.combo_hospital.setSelectedIndex(-1);
			this.date.setDate(null);
			this.date_validation.setDate(null);
		}
		if (arg0.getSource() == this.but_show) {
			Object[][] data = SaleSQL.getAll();
			String[] tablehead = { "销售编号", "药品名称", "生产批号", "质量", "有效期至",
					"医院名称", "数量", "单价", "金额", "日期" };
			this.tablemodel = new SaleTableModel(data);
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

	private class NewSaleDialog extends MyDialog implements ActionListener {
		private static final long serialVersionUID = -8641047013518773896L;
		public static final int NEW_MODE = 900912;
		public static final int EDIT_MODE = 900913;
		public static final int DIALOG_MODE = 900914;
		public static final int INSERT_MODE = 900915;
		private JComboBox combo_name;
		private JComboBox hospital_name;
		private JTextField text_id;
		private JTextField text_batchnum;
		private JTextField text_amount;
		private JTextField text_qua;
		private JTextField text_price;
		private JDateChooser date;
		private JDateChooser valid_period;
		private String selectIndex;
		private int mode;

		NewSaleDialog(JFrame frame, String s, int MODE, String selectIndex) {
			super(s);
			setBounds(200, 200, 550, 260);
			this.mode = MODE;

			String[] str_drugname = DrugSQL.getAllDrugnames();
			String[] str_hospitalname = HospitalSQL.getAllHospitalname();

			java.util.Date today = new java.util.Date();

			this.combo_name = new JComboBox(str_drugname);
			this.hospital_name = new JComboBox(str_hospitalname);
			this.text_id = new JTextField(8);
			this.text_qua = new JTextField(8);
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
			this.info_panel.setLayout(new GridLayout(2, 2));
			this.text_id.setText(getSaleID(SaleSQL
					.CountTodayRecord(new java.util.Date())));
			this.info_panel.add(new TagPanel("销售编号", this.text_id));
			this.info_panel.add(new TagPanel("药品名称", this.combo_name));
			this.info_panel.add(new TagPanel("生产批号", this.text_batchnum));
			this.info_panel.add(new TagPanel("产品质量", this.text_qua,
					"\"合格\"或\"不合格\""));
			JPanel text_panel2 = new JPanel();
			text_panel2.setBorder(BorderFactory.createTitledBorder("销售信息"));
			text_panel2.setLayout(new GridLayout(3, 2));
			text_panel2.add(new TagPanel("医       院", this.hospital_name));
			text_panel2.add(new TagPanel("有效期至", this.valid_period));
			text_panel2.add(new TagPanel("销售价格", this.text_amount, "例如:10.00"));
			text_panel2
					.add(new TagPanel("数       量", this.text_price, "例如:500"));
			text_panel2.add(new TagPanel("销售时间", this.date));
			this.contentPane.add(text_panel2, "North");

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					SalePanel.NewSaleDialog.this.dispose();
					SalePanel.NewSaleDialog.this.text_id = (SalePanel.NewSaleDialog.this.text_batchnum = SalePanel.NewSaleDialog.this.text_amount = SalePanel.NewSaleDialog.this.text_price = null);
					SalePanel.NewSaleDialog.this.but_apply = (SalePanel.NewSaleDialog.this.but_cancel = null);
					SalePanel.NewSaleDialog.this.date = (SalePanel.NewSaleDialog.this.valid_period = null);
					SalePanel.NewSaleDialog.this.combo_name = (SalePanel.NewSaleDialog.this.hospital_name = null);
				}
			});
		}

		private String convert(int num, char choose) {
			char[] c = { 'D', '0', '0', '0' };
			if (choose == 'H')
				c[0] = 'H';
			int tmp37_36 = 3;
			char[] tmp37_35 = c;
			tmp37_35[tmp37_36] = ((char) (tmp37_35[tmp37_36] + num % 10));
			num /= 10;
			int tmp53_52 = 2;
			char[] tmp53_51 = c;
			tmp53_51[tmp53_52] = ((char) (tmp53_51[tmp53_52] + num % 10));
			int tmp64_63 = 1;
			char[] tmp64_62 = c;
			tmp64_62[tmp64_63] = ((char) (tmp64_62[tmp64_63] + num / 10));
			String str = new String(c);
			return str;
		}

		private String getSaleID(int num) {
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
			str = "S" + ft.format(new java.util.Date()) + s;
			return str;
		}

		private void setDataToDialog(String id) {
			this.text_id.setText(id);
			this.text_id.setEditable(false);
			Object[] obj = SaleSQL.getRecord(id);
			this.combo_name.setSelectedItem(obj[1]);
			this.text_batchnum.setText((String) obj[2]);
			this.text_qua.setText((String) obj[3]);
			this.valid_period.setDate((java.util.Date) obj[4]);
			this.hospital_name.setSelectedItem(obj[5]);
			this.text_amount.setText(obj[6].toString());
			this.text_price.setText(obj[7].toString());
			this.date.setDate((java.util.Date) obj[9]);
		}

		public Object[] getDataFromDialog(int m) {
			Object[] a = new Object[10];
			a[0] = this.text_id.getText();
			if (m == 900915) {
				a[1] = convert(this.combo_name.getSelectedIndex(), 'D');
				a[5] = convert(this.hospital_name.getSelectedIndex(), 'H');
			} else {
				a[1] = this.combo_name.getSelectedItem();
				a[5] = this.hospital_name.getSelectedItem();
			}
			a[2] = this.text_batchnum.getText();
			a[3] = this.text_qua.getText();
			a[4] = new java.sql.Date(this.valid_period.getDate().getTime());
			a[6] = this.text_amount.getText();
			a[7] = this.text_price.getText();
			int amount = Integer.parseInt(a[6].toString());
			double price = Double.parseDouble(a[7].toString());
			a[8] = Double.valueOf(amount * price);
			a[9] = new java.sql.Date(this.date.getDate().getTime());
			return a;
		}

		public Object[] getEditData() {
			Object[] obj = new Object[9];
			obj[0] = convert(this.combo_name.getSelectedIndex(), 'D');
			obj[1] = this.text_batchnum.getText();
			obj[2] = this.text_qua.getText();
			obj[3] = new java.sql.Date(this.valid_period.getDate().getTime());
			obj[4] = convert(this.hospital_name.getSelectedIndex(), 'H');
			obj[5] = this.text_amount.getText();
			obj[6] = this.text_price.getText();
			int amount = Integer.parseInt(obj[5].toString());
			double price = Double.parseDouble(obj[6].toString());
			obj[7] = Double.valueOf(amount * price);
			obj[8] = new java.sql.Date(this.date.getDate().getTime());
			return obj;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == this.but_apply) {
				if (this.mode == 900912) {
					Object[] a = getDataFromDialog(900915);
					SaleSQL.Insert(a);
					SalePanel.this.tablemodel.addRow(SalePanel.this.dialog
							.getDataFromDialog(900914));
				}
				if (this.mode == 900913) {
					Object[] obj = getEditData();
					SaleSQL.Update(this.selectIndex, obj);
					SalePanel.this.tablemodel.updateRow(
							SalePanel.this.table.getSelectedRow(),
							SalePanel.this.dialog.getDataFromDialog(900914));
				}
				dispose();
			}
			if (arg0.getSource() == this.but_cancel)
				dispose();
		}
	}

	private class SaleTableModel extends MyTableModel {
		private static final long serialVersionUID = -3448229643907612271L;

		SaleTableModel(Object[][] data) {
			this.rows = new ArrayList();
			for (int i = 0; i < data.length; i++)
				this.rows.add(data[i]);
		}
	}
}
