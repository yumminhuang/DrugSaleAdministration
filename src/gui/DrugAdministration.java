package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DrugAdministration {
	public static void main(String[] args) {
		MyWindow win = new MyWindow();
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
