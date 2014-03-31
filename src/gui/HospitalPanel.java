package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
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
import report.HospitalReport;
import sql.DrugSQL;
import sql.HospitalSQL;

public class HospitalPanel extends MyPanel implements ActionListener {
	private static final long serialVersionUID = -5685508717602732690L;
	private JComboBox combo_level;
	private JTable table;
	private JTextField text_name;
	private HospitalTableModel tablemodel;
	private NewHospitalDialog dialog;
	private JasperViewer viewer = null;

	HospitalPanel(JFrame f) {
		super(f);

		String[] str_level = { "", "三甲", "三乙", "二甲", "二乙", "社区" };
		this.combo_level = new JComboBox(str_level);
		this.text_name = new JTextField();

		this.panel_main.setBorder(BorderFactory.createTitledBorder("医院表"));
		this.panel_queryInput.setBorder(BorderFactory.createTitledBorder("查询"));
		this.panel_queryInput.setLayout(new GridLayout(1, 2));
		this.panel_queryInput.add(new TagPanel("医院名称", this.text_name));
		this.panel_queryInput.add(new TagPanel("医院等级", this.combo_level));
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.but_new) {
			this.dialog = new NewHospitalDialog(this.frame, "新建医院", 900912,
					null);
			this.dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_edit) {
			if (this.table.getSelectedRow() != -1) {
				this.dialog = new NewHospitalDialog(this.frame, "编辑医院", 900913,
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
					HospitalSQL.Delete((String) this.tablemodel.getValueAt(
							this.table.getSelectedRow(), 0));
					this.tablemodel.deleteRow(this.table.getSelectedRow());
				}
			} else
				JOptionPane.showMessageDialog(this, "请选择一条记录", "警告", 2);
		}

		if ((arg0.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = HospitalReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					HospitalPanel.this.viewer = null;
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
			if (this.combo_level.getSelectedIndex() <= 0)
				data[1] = null;
			else
				data[1] = Integer.valueOf(this.combo_level.getSelectedIndex());
			Object[][] result = HospitalSQL.Query(data);
			QueryResultDialog dialog = new QueryResultDialog(this.frame,
					result, this.tablemodel.getTablehead());
			dialog.setVisible(true);
		}
		if (arg0.getSource() == this.but_clean) {
			this.text_name.setText(null);
			this.combo_level.setSelectedIndex(-1);
		}
		if (arg0.getSource() == this.but_show) {
			Object[][] data = HospitalSQL.getAll();
			String[] tablehead = { "医院编号", "医院名称", "等级", "联系人", "电话", "地址" };
			this.tablemodel = new HospitalTableModel(data);
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

	private class HospitalTableModel extends MyTableModel {
		private static final long serialVersionUID = -3448229643907612271L;

		HospitalTableModel(Object[][] data) {
			this.rows = new ArrayList();
			for (int i = 0; i < data.length; i++)
				this.rows.add(data[i]);
		}
	}

	private class NewHospitalDialog extends MyDialog implements ActionListener {
		private static final long serialVersionUID = 1060536110627784711L;
		private final String[] level = { "三甲", "三乙", "二甲", "二乙", "社区" };
		public static final int NEW_MODE = 900912;
		public static final int EDIT_MODE = 900913;
		public static final int DIALOG_MODE = 900914;
		public static final int INSERT_MODE = 900915;
		private JTextField text_name;
		private JTextField text_id;
		private JTextField text_linkman;
		private JTextField text_phone;
		private JTextField text_address;
		private JComboBox combo_level;
		private String selectIndex;
		private int mode;

		NewHospitalDialog(JFrame f, String s, int MODE, String selectIndex) {
			super(s);
			setBounds(200, 300, 500, 180);

			this.combo_level = new JComboBox(this.level);
			this.text_name = new JTextField();
			this.text_id = new JTextField();
			this.text_linkman = new JTextField();
			this.text_phone = new JTextField();
			this.text_address = new JTextField(35);
			if (MODE == 900913) {
				setDataToDialog(selectIndex);
				this.selectIndex = selectIndex;
			}
			this.info_panel.setLayout(new GridLayout(3, 2));
			this.info_panel.add(new TagPanel("医院编号", this.text_id, "例如：H010"));
			this.info_panel.add(new TagPanel("医院名称", this.text_name));
			this.info_panel.add(new TagPanel("联  系  人", this.text_linkman));
			this.info_panel.add(new TagPanel("电        话", this.text_phone));
			this.info_panel.add(new TagPanel("地        址", this.text_address));
			this.info_panel.add(new TagPanel("等        级", this.combo_level));

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					HospitalPanel.NewHospitalDialog.this.dispose();
					HospitalPanel.NewHospitalDialog.this.text_name = (HospitalPanel.NewHospitalDialog.this.text_id = HospitalPanel.NewHospitalDialog.this.text_linkman = HospitalPanel.NewHospitalDialog.this.text_phone = HospitalPanel.NewHospitalDialog.this.text_address = null);
					HospitalPanel.NewHospitalDialog.this.but_apply = (HospitalPanel.NewHospitalDialog.this.but_cancel = null);
					HospitalPanel.NewHospitalDialog.this.combo_level = null;
				}
			});
		}

		private void setDataToDialog(String id) {
			this.text_id.setText(id);
			this.text_id.setEditable(false);
			Object[] obj = HospitalSQL.getRecord(id);
			this.text_name.setText((String) obj[1]);
			System.out.println(this.text_name.getText());
			this.combo_level.setSelectedItem(obj[2]);
			this.text_address.setText((String) obj[3]);
			this.text_linkman.setText((String) obj[4]);
			this.text_phone.setText((String) obj[5]);
		}

		public Object[] getDataFromDialog(int mode) {
			Object[] a = new Object[6];
			a[0] = this.text_id.getText();
			a[1] = this.text_name.getText();
			if (mode == 900915)
				a[2] = Integer.valueOf(this.combo_level.getSelectedIndex());
			else {
				a[2] = this.level[this.combo_level.getSelectedIndex()];
			}
			a[3] = this.text_address.getText();
			a[4] = this.text_linkman.getText();
			a[5] = this.text_phone.getText();
			return a;
		}

		public Object[] getEditData() {
			Object[] obj = new Object[8];
			obj[0] = this.text_name.getText();
			obj[1] = Integer.valueOf(this.combo_level.getSelectedIndex());
			obj[2] = this.text_address.getText();
			obj[3] = this.text_linkman.getText();
			obj[4] = this.text_phone.getText();
			return obj;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == this.but_apply) {
				if (this.mode == 900912) {
					Object[] a = getDataFromDialog(900915);
					HospitalSQL.Insert(a);
					HospitalPanel.this.tablemodel
							.addRow(HospitalPanel.this.dialog
									.getDataFromDialog(900914));
				}
				if (this.mode == 900913) {
					Object[] obj = getEditData();
					DrugSQL.Update(this.selectIndex, obj);
					HospitalPanel.this.tablemodel
							.updateRow(HospitalPanel.this.table
									.getSelectedRow(),
									HospitalPanel.this.dialog
											.getDataFromDialog(900914));
				}
				dispose();
			}
			if (arg0.getSource() == this.but_cancel)
				dispose();
		}
	}
}
