package gui;

import com.toedter.calendar.JDateChooser;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import report.DrugReport;
import sql.DrugSQL;
import sql.SupplerSQL;

public class DrugPanel extends MyPanel implements ActionListener {
	private static final long serialVersionUID = -5847879857590138610L;
	private JComboBox combo_qua;
	private JComboBox combo_fac;
	private JTable table;
	private JTextField text_name;
	private JDateChooser date_verification;
	private DrugTableModel tablemodel;
	private NewDrugDialog dialog;
	private JasperViewer viewer = null;

	DrugPanel(JFrame f) {
		super(f);

		String[] str_quality = { "", "原研类", "专利类", "GMP类", "GMP单独定价类", "其它" };
		this.combo_qua = new JComboBox(str_quality);
		this.combo_fac = new JComboBox(SupplerSQL.getAllFactoryname());
		this.text_name = new JTextField();
		this.date_verification = new JDateChooser();

		this.panel_main.setBorder(BorderFactory.createTitledBorder("药品表"));
		this.panel_queryInput.setLayout(new GridLayout(2, 2));
		this.panel_queryInput.add(new TagPanel("药品名称", this.text_name));
		this.panel_queryInput.add(new TagPanel("生产厂家", this.combo_fac));
		this.panel_queryInput.add(new TagPanel("年审下限", this.date_verification));
		this.panel_queryInput.add(new TagPanel("质量类别", this.combo_qua));
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.but_new) {
			this.dialog = new NewDrugDialog(this.frame, "新建药品", 900912, null);
			this.dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_edit) {
			if (this.table.getSelectedRow() != -1) {
				this.dialog = new NewDrugDialog(this.frame, "编辑药品", 900913,
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
					DrugSQL.Delete((String) this.tablemodel.getValueAt(
							this.table.getSelectedRow(), 0));
					this.tablemodel.deleteRow(this.table.getSelectedRow());
				}
			} else
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
		}

		if ((arg0.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = DrugReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					DrugPanel.this.viewer = null;
				}
			});
			this.viewer.setVisible(true);
		}

		if (arg0.getSource() == this.but_query) {
			Object[] data = new Object[4];
			if (this.text_name.getText().length() == 0)
				data[0] = "";
			else
				data[0] = this.text_name.getText();
			if (this.combo_fac.getSelectedIndex() <= 0)
				data[1] = "";
			else
				data[1] = convert(this.combo_fac.getSelectedIndex());
			if (this.combo_qua.getSelectedIndex() <= 0)
				data[2] = null;
			else
				data[2] = Integer.valueOf(this.combo_qua.getSelectedIndex());
			if (this.date_verification.getDate() == null)
				data[3] = null;
			else
				data[3] = new java.sql.Date(this.date_verification.getDate()
						.getTime());
			Object[][] result = DrugSQL.Query(data);
			QueryResultDialog dialog = new QueryResultDialog(this.frame,
					result, this.tablemodel.getTablehead());
			dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_clean) {
			this.text_name.setText(null);
			this.combo_fac.setSelectedIndex(-1);
			this.combo_qua.setSelectedIndex(-1);
			this.date_verification.setDate(null);
		}
		if (arg0.getSource() == this.but_show) {
			Object[][] data = DrugSQL.getAll();
			String[] tablehead = { "药品编号", "药品名称", "商品名", "批准文号", "规格", "包装",
					"质量类别", "厂家名称", "药品年审" };
			this.tablemodel = new DrugTableModel(data);
			this.tablemodel.setTablehead(tablehead);
			this.table = new JTable(this.tablemodel);
			fitTableColumns(this.table);
			JScrollPane s = new JScrollPane(this.table);
			this.panel_main.add(s, "Center");
			validate();
		}
		if (arg0.getSource() == this.but_close) {
			this.table.setVisible(false);
			this.table = null;
		}
	}

	private String convert(int num) {
		char[] c = { 'F', '0', '0', '0' };
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

	private class DrugTableModel extends MyTableModel {
		private static final long serialVersionUID = -3448229643907612271L;

		DrugTableModel(Object[][] data) {
			this.rows = new ArrayList();
			for (int i = 0; i < data.length; i++)
				this.rows.add(data[i]);
		}
	}

	private class NewDrugDialog extends MyDialog implements ActionListener {
		private static final long serialVersionUID = -6636091812639808188L;
		public static final int NEW_MODE = 900912;
		public static final int EDIT_MODE = 900913;
		public static final int DIALOG_MODE = 900914;
		public static final int INSERT_MODE = 900915;
		private JComboBox combo_supname;
		private JComboBox combo_qua;
		private JTextField text_name;
		private JTextField text_id;
		private JTextField text_appnum;
		private JTextField text_spec;
		private JTextField text_pack;
		private JTextField text_conname;
		private JDateChooser jdate_verification;
		private String[] str_quality = { "原研类", "专利类", "GMP类", "GMP单独定价类", "其它" };
		private String[] str_supname = SupplerSQL.getAllFactoryname();
		private String selectIndex;
		private int mode;

		NewDrugDialog(JFrame f, String s, int MODE, String selectIndex) {
			super(s);
			this.mode = MODE;
			setBounds(200, 300, 800, 200);

			this.text_id = new JTextField(8);
			this.text_appnum = new JTextField(8);
			this.text_spec = new JTextField(8);
			this.text_name = new JTextField(8);
			this.text_conname = new JTextField(8);
			this.text_pack = new JTextField(8);
			this.combo_supname = new JComboBox(this.str_supname);
			this.combo_qua = new JComboBox(this.str_quality);
			this.jdate_verification = new JDateChooser();
			if (MODE == 900913) {
				setDataToDialog(selectIndex);
				this.selectIndex = selectIndex;
			}
			this.info_panel.setBorder(BorderFactory.createTitledBorder("药品信息"));
			this.info_panel.setLayout(new GridLayout(3, 3));
			this.info_panel.add(new TagPanel("药品编号", this.text_id, "例如：D010"));
			this.info_panel.add(new TagPanel("药品名称", this.text_name));
			this.info_panel
					.add(new TagPanel("商品名称", this.text_conname, "该项可为空"));
			this.info_panel.add(new TagPanel("生产批号", this.text_appnum));
			this.info_panel.add(new TagPanel("规        格", this.text_spec));
			this.info_panel.add(new TagPanel("包        装", this.text_pack));
			this.info_panel.add(new TagPanel("质量类别", this.combo_qua));
			this.info_panel.add(new TagPanel("生产厂家", this.combo_supname));
			this.info_panel.add(new TagPanel("药品年审", this.jdate_verification));

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					DrugPanel.NewDrugDialog.this.dispose();
					DrugPanel.NewDrugDialog.this.text_name = (DrugPanel.NewDrugDialog.this.text_id = DrugPanel.NewDrugDialog.this.text_appnum = DrugPanel.NewDrugDialog.this.text_spec = DrugPanel.NewDrugDialog.this.text_pack = DrugPanel.NewDrugDialog.this.text_conname = null);
					DrugPanel.NewDrugDialog.this.but_apply = (DrugPanel.NewDrugDialog.this.but_cancel = null);
					DrugPanel.NewDrugDialog.this.jdate_verification = null;
					DrugPanel.NewDrugDialog.this.combo_supname = (DrugPanel.NewDrugDialog.this.combo_qua = null);
					DrugPanel.NewDrugDialog.this.str_quality = (DrugPanel.NewDrugDialog.this.str_supname = null);
				}
			});
		}

		private String convert(int num) {
			char[] c = { 'F', '0', '0', '0' };
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

		private void setDataToDialog(String id) {
			this.text_id.setText(id);
			this.text_id.setEditable(false);
			Object[] obj = DrugSQL.getRecord(id);
			this.text_name.setText((String) obj[1]);
			this.text_conname.setText((String) obj[2]);
			this.text_appnum.setText((String) obj[3]);
			this.text_spec.setText((String) obj[4]);
			this.text_pack.setText((String) obj[5]);
			this.combo_qua.setSelectedItem(obj[6]);
			this.combo_supname.setSelectedItem(obj[7]);
			this.jdate_verification.setDate((java.util.Date) obj[8]);
		}

		public Object[] getDataFromDialog(int mode) {
			Object[] a = new Object[9];
			a[0] = this.text_id.getText();
			a[1] = this.text_name.getText();
			a[2] = this.text_conname.getText();
			a[3] = this.text_appnum.getText();
			a[4] = this.text_spec.getText();
			a[5] = this.text_pack.getText();
			if (mode == 900915) {
				a[6] = Integer.valueOf(this.combo_qua.getSelectedIndex());
				a[7] = convert(this.combo_supname.getSelectedIndex());
			} else {
				a[6] = this.str_quality[this.combo_qua.getSelectedIndex()];
				a[7] = SupplerSQL.getFactoryname(convert(this.combo_supname
						.getSelectedIndex()));
			}
			a[8] = new java.sql.Date(this.jdate_verification.getDate()
					.getTime());
			return a;
		}

		public Object[] getEditData() {
			Object[] obj = new Object[8];
			obj[0] = this.text_name.getText();
			obj[1] = this.text_conname.getText();
			obj[2] = this.text_appnum.getText();
			obj[3] = this.text_spec.getText();
			obj[4] = this.text_pack.getText();
			obj[5] = Integer.valueOf(this.combo_qua.getSelectedIndex());
			obj[6] = convert(this.combo_supname.getSelectedIndex() + 1);
			obj[7] = new java.sql.Date(this.jdate_verification.getDate()
					.getTime());
			return obj;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == this.but_apply) {
				if (this.mode == 900912) {
					Object[] a = getDataFromDialog(900915);
					DrugSQL.Insert(a);
					DrugPanel.this.tablemodel.addRow(DrugPanel.this.dialog
							.getDataFromDialog(900914));
				}
				if (this.mode == 900913) {
					Object[] obj = getEditData();
					DrugSQL.Update(this.selectIndex, obj);
					DrugPanel.this.tablemodel.updateRow(
							DrugPanel.this.table.getSelectedRow(),
							DrugPanel.this.dialog.getDataFromDialog(900914));
				}
				dispose();
			}

			if (e.getSource() == this.but_cancel)
				dispose();
		}
	}
}
