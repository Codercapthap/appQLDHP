package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {
	public static Connection getConnection() throws SQLException{
		String url = "jdbc:sqlserver://DESKTOP-BQF2JVN\\SQLEXPRESS;databaseName=std";
	    String user = new String("nlcs");
	    String password = new String("123");
	    
	    Connection connection = DriverManager.getConnection(url, user, password);
	    return connection;
    }

    public static void closeConnection(Connection con) {
        try {
            if(con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	// Connect to your database.
    // Replace server name, username, and password with your credentials
    
}
