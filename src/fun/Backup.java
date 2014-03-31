package fun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Backup {
	private static boolean MoveFile(File originFile, String newDir)
			throws IOException {
		File newFile = new File(newDir);
		byte[] buf = new byte[1024];
		try {
			FileInputStream read = new FileInputStream(originFile);
			FileOutputStream write = new FileOutputStream(newFile);
			int byteRead;
			while ((byteRead = read.read(buf)) != -1) {
				write.write(buf, 0, byteRead);
			}
			read.close();
			write.close();
			buf = (byte[]) null;
			newFile = null;
		} catch (FileNotFoundException e) {
			buf = (byte[]) null;
			newFile = null;
			throw e;
		} catch (IOException e) {
			buf = (byte[]) null;
			newFile = null;
			throw e;
		}
		return true;
	}

	public static boolean BackupFile(String newDir) {
		File orignalFile = new File(".\\database\\DrugAdministation.accdb");
		try {
			return MoveFile(orignalFile, newDir + "\\BackupFile.accdb");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean importBackupFile(File backupfile) {
		String dir = ".\\database\\DrugAdministation.accdb";
		try {
			return MoveFile(backupfile, dir);
		} catch (Exception e) {
		}
		return false;
	}

	public static void recordLog(String str) {
		String fileName = ".\\Record.log";
		Date d = new Date();
		String content = d.toString() + "\n" + str + "\n";
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void recordLog(boolean b) {
		String fileName = ".\\Record.log";
		String content = null;
		if (b)
			content = "Execute Succeed\n\n";
		else
			content = "Execute Failed\n\n";
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}