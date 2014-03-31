package gui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TagPanel extends JPanel {
	private static final long serialVersionUID = -4787343554509512798L;

	TagPanel(String s, JComponent com) {
		JLabel label = new JLabel(s);
		setLayout(new BorderLayout());
		label.setLabelFor(com);
		add(label, "West");
		add(com, "Center");
	}

	TagPanel(String s, JComponent com, String tip) {
		setLayout(new BorderLayout());
		JLabel label = new JLabel(s);
		label.setLabelFor(com);
		com.setToolTipText(tip);
		add(label, "West");
		add(com, "Center");
	}
}
