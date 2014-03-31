package fun;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter {
	String[] extensions;
	String description;

	public SimpleFileFilter(String ext) {
		this(new String[] { ext }, null);
	}

	public SimpleFileFilter(String[] exts, String descr) {
		this.extensions = new String[exts.length];
		for (int i = exts.length - 1; i >= 0; i--) {
			this.extensions[i] = exts[i].toLowerCase();
		}

		this.description = (descr == null ? exts[0] + " files" : descr);
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String name = f.getName().toLowerCase();
		for (int i = this.extensions.length - 1; i >= 0; i--) {
			if (name.endsWith(this.extensions[i])) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return this.description;
	}
}
