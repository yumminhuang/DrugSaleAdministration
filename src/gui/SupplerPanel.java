package gui;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.sf.jasperreports.view.JasperViewer;
import report.SupplerReport;
import sql.SupplerSQL;

public class SupplerPanel extends MyPanel implements ActionListener {
	private static final long serialVersionUID = 2014135673287167458L;
	private JTable table;
	private JTextField text_name;
	private JDateChooser date_verification;
	private SupplerTableModel tablemodel;
	private NewSupplerDialog dialog;
	private JasperViewer viewer = null;

	SupplerPanel(JFrame f) {
		super(f);

		this.text_name = new JTextField();
		this.date_verification = new JDateChooser();

		this.panel_main.setBorder(BorderFactory.createTitledBorder("供应商表"));
		this.panel_queryInput.setLayout(new BorderLayout());
		this.panel_queryInput.setLayout(new GridLayout(1, 2));
		this.panel_queryInput.add(new TagPanel("供应商名称", this.text_name));
		this.panel_queryInput.add(new TagPanel("年审下限", this.date_verification));
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.but_new) {
			this.dialog = new NewSupplerDialog(this.frame, "新建供应商", 900912,
					null);
			this.dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_edit) {
			if (this.table.getSelectedRow() != -1) {
				this.dialog = new NewSupplerDialog(this.frame, "编辑供应商", 900913,
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
					SupplerSQL.Delete((String) this.tablemodel.getValueAt(
							this.table.getSelectedRow(), 0));
					this.tablemodel.deleteRow(this.table.getSelectedRow());
				}
			} else
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
		}

		if ((arg0.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = SupplerReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					SupplerPanel.this.viewer = null;
				}
			});
			this.viewer.setVisible(true);
		}

		if (arg0.getSource() == this.but_query) {
			Object[] data = new Object[2];
			if (this.text_name.getText().length() == 0)
				data[0] = "";
			else
				data[0] = this.text_name.getText();
			if (this.date_verification.getDate() == null)
				data[1] = null;
			else
				data[1] = new java.sql.Date(this.date_verification.getDate()
						.getTime());
			Object[][] result = SupplerSQL.Query(data);
			QueryResultDialog dialog = new QueryResultDialog(this.frame,
					result, this.tablemodel.getTablehead());
			dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_clean) {
			this.date_verification.setDate(null);
			this.text_name.setText(null);
		}
		if (arg0.getSource() == this.but_show) {
			Object[][] data = SupplerSQL.getAll();
			String[] tablehead = { "供应商编号", "供应商名称", "联系人", "电话", "地址", "银行账号",
					"开户地址", "企业年审" };
			this.tablemodel = new SupplerTableModel(data);
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

	private class NewSupplerDialog extends MyDialog implements ActionListener {
		private static final long serialVersionUID = 1060536110627784711L;
		public static final int NEW_MODE = 900912;
		public static final int EDIT_MODE = 900913;
		private JDateChooser date_verification;
		private JTextField text_name;
		private JTextField text_id;
		private JTextField text_linkman;
		private JTextField text_phone;
		private JTextField text_address;
		private JTextField text_account;
		private JTextField text_accadd;
		private String selectIndex;
		private int mode;

		NewSupplerDialog(JFrame f, String s, int MODE, String selectIndex) {
			super(s);
			setBounds(200, 200, 500, 250);
			this.mode = MODE;

			this.text_name = new JTextField();
			this.text_id = new JTextField();
			this.text_linkman = new JTextField();
			this.text_phone = new JTextField();
			this.text_account = new JTextField();
			this.text_address = new JTextField(35);
			this.text_accadd = new JTextField(35);
			this.date_verification = new JDateChooser();
			if (MODE == 900913) {
				setDataToDialog(selectIndex);
				this.selectIndex = selectIndex;
			}
			this.info_panel.setBorder(BorderFactory.createTitledBorder("企业信息"));
			this.info_panel.setLayout(new GridLayout(4, 2));
			this.info_panel.add(new TagPanel("企业编号", this.text_id));
			this.info_panel.add(new TagPanel("企业名称", this.text_name));
			this.info_panel.add(new TagPanel("联  系  人", this.text_linkman));
			this.info_panel.add(new TagPanel("电        话", this.text_phone));
			this.info_panel.add(new TagPanel("账        号", this.text_account));
			this.info_panel.add(new TagPanel("企业年审", this.date_verification));
			this.info_panel.add(new TagPanel("开户银行", this.text_accadd));
			this.info_panel.add(new TagPanel("企业地址", this.text_address));
		}

		private void setDataToDialog(String id) {
			this.text_id.setText(id);
			this.text_id.setEditable(false);
			Object[] obj = SupplerSQL.getRecord(id);
			this.text_name.setText((String) obj[1]);
			this.text_address.setText((String) obj[2]);
			this.text_linkman.setText((String) obj[3]);
			this.text_phone.setText((String) obj[4]);
			this.text_accadd.setText((String) obj[5]);
			this.text_account.setText((String) obj[6]);
			this.date_verification.setDate((java.util.Date) obj[7]);
		}

		public Object[] getDataFromDialog() {
			Object[] a = new Object[8];
			a[0] = this.text_id.getText();
			a[1] = this.text_name.getText();
			a[2] = this.text_address.getText();
			a[3] = this.text_linkman.getText();
			a[4] = this.text_phone.getText();
			a[5] = this.text_accadd.getText();
			a[6] = this.text_account.getText();
			a[7] = new java.sql.Date(this.date_verification.getDate().getTime());
			return a;
		}

		public Object[] getEditData() {
			Object[] a = new Object[7];
			a[0] = this.text_name.getText();
			a[1] = this.text_address.getText();
			a[2] = this.text_linkman.getText();
			a[3] = this.text_phone.getText();
			a[4] = this.text_accadd.getText();
			a[5] = this.text_account.getText();
			a[6] = new java.sql.Date(this.date_verification.getDate().getTime());
			return a;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == this.but_apply) {
				if (this.mode == 900912) {
					Object[] a = getDataFromDialog();
					SupplerSQL.Insert(a);
					SupplerPanel.this.tablemodel
							.addRow(SupplerPanel.this.dialog
									.getDataFromDialog());
				}
				if (this.mode == 900913) {
					Object[] obj = getEditData();
					SupplerSQL.Update(this.selectIndex, obj);
					SupplerPanel.this.tablemodel.updateRow(
							SupplerPanel.this.table.getSelectedRow(),
							SupplerPanel.this.dialog.getDataFromDialog());
				}
				dispose();
			}

			if (e.getSource() == this.but_cancel)
				dispose();
		}
	}

	private class SupplerTableModel extends MyTableModel {
		private static final long serialVersionUID = -3448229643907612271L;

		SupplerTableModel(Object[][] data) {
			this.rows = new ArrayList();
			for (int i = 0; i < data.length; i++)
				this.rows.add(data[i]);
		}
	}
}
