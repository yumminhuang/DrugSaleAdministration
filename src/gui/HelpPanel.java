package gui;

import fun.Backup;
import fun.SimpleFileFilter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class HelpPanel extends JPanel {
	private static final long serialVersionUID = 7792005453460313310L;
	private JFrame frame;
	private JButton but_backup;
	private JButton but_import;
	private JButton but_help;
	private static final String mailaddress = "yumminhuang@yahoo.com.cn";

	HelpPanel(JFrame F) {
		this.frame = F;
		setLayout(new BorderLayout());

		this.but_help = new JButton("打开帮助文件");
		this.but_help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String[] cmd = new String[4];
					cmd[0] = "cmd.exe";
					cmd[1] = "/C";
					cmd[2] = "start";
					cmd[3] = ".\\Manual.pdf";
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(HelpPanel.this.frame,
							"帮助文件打开失败!", "警告", 2);
				}
			}
		});
		this.but_backup = new JButton("备份");
		this.but_backup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(1);
				int option = chooser.showOpenDialog(HelpPanel.this);
				if (option == 0) {
					boolean b = Backup.BackupFile(chooser.getSelectedFile()
							.getAbsolutePath());
					if (!b)
						JOptionPane.showMessageDialog(HelpPanel.this.frame,
								"备份文件失败!", "警告", 2);
				}
			}
		});
		this.but_import = new JButton("导入备份");
		this.but_import.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] fileTypes = { "accdb", "mdb" };
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new SimpleFileFilter(fileTypes,
						"Microsoft Access(*.accdb , *.mdb)"));
				int option = chooser.showOpenDialog(HelpPanel.this);
				if (option == 0) {
					boolean b = Backup.importBackupFile(chooser
							.getSelectedFile());
					if (!b)
						JOptionPane.showMessageDialog(HelpPanel.this.frame,
								"导入备份文件失败!", "警告", 2);
				}
			}
		});
		JPanel panel_help = new JPanel();
		panel_help.setBorder(BorderFactory.createTitledBorder("帮助"));
		panel_help.add(this.but_help);

		JPanel panel_backup = new JPanel();
		panel_backup.setBorder(BorderFactory.createTitledBorder("备份"));
		panel_backup.add(this.but_backup);
		panel_backup.add(this.but_import);

		JPanel panel_about = new JPanel();
		panel_about.setLayout(new GridLayout(3, 1));
		panel_about.setBorder(BorderFactory.createTitledBorder("关于"));
		panel_about.add(new JLabel("药品销售管理系统"));
		panel_about.add(new JLabel("版本号: V1.0 beta"));
		panel_about.add(new JLabel("开发者：黄亚铭\tE-Mail:yumminhuang@yahoo.com.cn"));

		add(panel_help, "North");
		add(panel_backup, "Center");
		add(panel_about, "South");
	}
}
