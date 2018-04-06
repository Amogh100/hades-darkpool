package eventprocessing;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper{

    private static final String url = "jdbc:postgresql://localhost:5432/hadesmaster";
    private static final String username = System.getenv("POSTGRES_USERNAME");
    private static final String password = System.getenv("POSTGRES_PASSWORD");

    public synchronized static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


}

