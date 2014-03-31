package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MyWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2196540117576619670L;
	private JTabbedPane tabPane;

	MyWindow() {
		super("药品销售管理");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setBounds(100, 100, 1000, 500);

		this.tabPane = new JTabbedPane();
		this.tabPane.setTabPlacement(1);
		add(this.tabPane);

		DrugPanel drugPanel = new DrugPanel(this);
		this.tabPane.addTab("药品页面", createImageIcon("drug"), drugPanel);
		HospitalPanel hospitalPanel = new HospitalPanel(this);
		this.tabPane.addTab("医院页面", createImageIcon("hospital"), hospitalPanel);
		SupplerPanel supplerPanel = new SupplerPanel(this);
		this.tabPane.addTab("供应商页面", createImageIcon("factory"), supplerPanel);
		PurchasePanel purchasePanel = new PurchasePanel(this);
		this.tabPane.addTab("进货页面", createImageIcon("in"), purchasePanel);
		SalePanel salePanel = new SalePanel(this);
		this.tabPane.addTab("销售页面", createImageIcon("out"), salePanel);
		BalancePanel balancePanel = new BalancePanel();
		this.tabPane.addTab("统计页面", createImageIcon("statistic"), balancePanel);
		HelpPanel helpPanel = new HelpPanel(this);
		this.tabPane.addTab("帮助页面", createImageIcon("welcome"), helpPanel);

		setVisible(true);
		validate();
	}

	protected static ImageIcon createImageIcon(String name) {
		String path = "./pic/" + name + ".png";
		return new ImageIcon(path);
	}

	public void actionPerformed(ActionEvent arg0) {
	}
}
