package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5973040106135241853L;
	protected JButton but_apply;
	protected JButton but_cancel;
	protected JPanel info_panel;
	protected Container contentPane;

	MyDialog(JFrame f, String s) {
		super(f, s);
		this.contentPane = getContentPane();
		this.contentPane.setLayout(new BorderLayout());
		setModal(true);
		setResizable(false);

		this.but_apply = new JButton("确认");
		this.but_apply.addActionListener(this);
		this.but_cancel = new JButton("取消");
		this.but_cancel.addActionListener(this);
		JPanel but_panel = new JPanel();
		but_panel.add(this.but_apply);
		but_panel.add(this.but_cancel);

		this.info_panel = new JPanel();
		this.contentPane.add(this.info_panel, "Center");
		this.contentPane.add(but_panel, "South");
	}

	public void actionPerformed(ActionEvent e) {
	}
}
