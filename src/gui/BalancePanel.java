package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import report.BalanceReport;
import sql.Statistic;

public class BalancePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -7551780521959501082L;
	private JButton but_report;
	private JButton but_show;
	private JButton but_close;
	private JList chartlist;
	private JTable table;
	private JPanel panel_chart;
	private JPanel panel;
	private BalanceTableModel tablemodel;
	private JasperViewer viewer = null;

	BalancePanel() {
		setLayout(new BorderLayout());
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.panel.setBorder(BorderFactory.createTitledBorder("库存表"));

		String[] str_chart = { "月度利润统计图", "药品销量统计图", "医院销售额统计图" };
		this.chartlist = new JList(str_chart);
		this.chartlist.setBorder(BorderFactory.createTitledBorder("选择图表种类"));
		this.chartlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				int selection = BalancePanel.this.chartlist.getSelectedIndex();
				switch (selection) {
				case 0:
					BalancePanel.this.drawProf_chart();
					break;
				case 1:
					BalancePanel.this.drawDrug_chart();
					break;
				case 2:
					BalancePanel.this.drawHos_chart();
				}
			}
		});
		this.panel_chart = new JPanel();
		this.panel_chart.setLayout(new BorderLayout());
		this.panel_chart.add(this.chartlist, "West");
		this.panel_chart.setBorder(BorderFactory.createTitledBorder("统计图表"));

		this.but_show = new JButton("打开表格");
		this.but_show.addActionListener(this);
		this.but_close = new JButton("关闭表格");
		this.but_close.addActionListener(this);
		this.but_report = new JButton("导出报表");
		this.but_report.addActionListener(this);
		JPanel panel_but = new JPanel();
		panel_but.add(this.but_show);
		panel_but.add(this.but_close);
		panel_but.add(this.but_report);

		JSplitPane splitpane = new JSplitPane(0, true, this.panel,
				this.panel_chart);
		splitpane.setDividerLocation(130);
		add(splitpane, "Center");
		add(panel_but, "South");
	}

	private void drawDrug_chart() {
		Object[][] obj = Statistic.Statitic_Drug();
		String drugname = null;
		double num = 0.0D;
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < obj.length; i++) {
			drugname = obj[i][0].toString();
			num = Double.parseDouble(obj[i][1].toString());
			dataset.setValue(drugname, num);
		}
		JFreeChart piechart = ChartFactory.createPieChart("药品销量统计图", dataset,
				false, false, false);
		ChartPanel c = new ChartPanel(piechart);
		c.setPreferredSize(new Dimension(50, 50));
		this.panel_chart.add(c, "Center");
		validate();
	}

	private void drawHos_chart() {
		Object[][] obj = Statistic.Statitic_Hospital();
		String drugname = null;
		double num = 0.0D;
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < obj.length; i++) {
			drugname = obj[i][0].toString();
			num = Double.parseDouble(obj[i][1].toString());
			dataset.setValue(drugname, num);
		}
		JFreeChart piechart = ChartFactory.createPieChart("医院销量统计图", dataset,
				false, false, false);
		ChartPanel c = new ChartPanel(piechart);
		c.setPreferredSize(new Dimension(50, 50));
		this.panel_chart.add(c, "Center");
		validate();
	}

	private void drawProf_chart() {
		Double[] prof = Statistic.Statitic_Prof();
		int[] month = new int[6];
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int m = cal.get(2) + 1;
		for (int i = 5; i >= 0; m--) {
			month[i] = ((m + 12) % 12 != 0 ? (m + 12) % 12 : 12);

			i--;
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (i = 0; i < 6; i++) {
			dataset.addValue(prof[i], "利润", String.valueOf(month[i]));
		}
		JFreeChart pchart = ChartFactory.createLineChart("近半年利润统计图", "月份",
				"利润(元)", dataset, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel c = new ChartPanel(pchart);
		c.setPreferredSize(new Dimension(50, 50));
		this.panel_chart.add(c, "Center");
		validate();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.but_show) {
			Object[][] data = Statistic.Balance();
			String[] tablehead = { "药品名称", "生产批号", "有效期至", "库存数量" };
			this.tablemodel = new BalanceTableModel(data);
			this.tablemodel.setTablehead(tablehead);
			this.table = new JTable(this.tablemodel);
			JScrollPane s = new JScrollPane(this.table);
			this.panel.add(s, "Center");
			validate();
		}
		if (e.getSource() == this.but_close) {
			this.table.setVisible(false);
			this.table = null;
		}

		if ((e.getSource() == this.but_report) && (this.viewer == null)) {
			this.viewer = BalanceReport.printReport();
			this.viewer.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					BalancePanel.this.viewer = null;
				}
			});
			this.viewer.setVisible(true);
		}
	}
}
