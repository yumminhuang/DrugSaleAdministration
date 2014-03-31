package sql;

public class BaseSQL {
	protected static String url = "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=.\\database\\DrugAdministation.accdb";

	BaseSQL() {
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
	}

	public static int CountRecord() {
		return 0;
	}

	public static Object[][] getAll() {
		return null;
	}

	public static Object[] getRecord(String id) {
		return null;
	}

	public static void Insert(Object[] o) {
	}

	public static void Update(String id, Object[] o) {
	}

	public static void Delete(String id) {
	}

	public static Object[][] Query(Object[] s) {
		return null;
	}
}
