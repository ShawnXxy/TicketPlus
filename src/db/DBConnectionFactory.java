package db;

public class DBConnectionFactory {
	private static final String DEFAULT_DB = "mysql";
	
	// Create a DBConnection based on given db type
	public static DBConnection getDBConnection (String db) {
		switch (db) {
			default:
				throw new IllegalArgumentException ("Invalid db " + db);
		}
	}
	
	// Overloading
	public static DBConnection getDBConnection() {
		return getDBConnection (DEFAULT_DB);
	}
}
