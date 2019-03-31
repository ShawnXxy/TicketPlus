package db.mysql;

public class MySQLDBUtil {
    private static final String HOSTNAME = "shawnxxy.mysql.database.azure.com";
    private static final String PORT_NUM = "3306";
    public static final String DB_NAME = "ticket";
    private static final String USERNAME = "x_xiao2_bnu@shawnxxy";
    private static final String PASSWORD = "XXy@@4592995";
    public static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD + "&autoreconnect=true";
}