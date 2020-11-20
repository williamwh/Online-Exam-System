
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JDBCPostgresSQLConnection {
	private final String url    = "jdbc:postgresql://localhost:5433/myDB";
	private final String user = "postgres";
	private final String password = "password";
	/*** Connect to the PostgreSQL database * 
	 * @return a Connection object */ 
	public Connection connect() { 
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null) { 
				System.out.println("Connected to   the    PostgreSQL server successfully.");
				} else { 
					System.out.println("Failed to   make connection!");
					} 
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				} return conn;		
	}
	
	public static void main(String[] args) {
		JDBCPostgresSQLConnection app = new JDBCPostgresSQLConnection();
		app.connect(); 
		} 
	}
