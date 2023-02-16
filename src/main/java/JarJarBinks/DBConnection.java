package JarJarBinks;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBConnection {

    private Connection conn;

    public DBConnection() {
        conn = null;
    }

    public Boolean Connect(String filename) {
        try {
            String url = "jdbc:sqlite:src/" + filename;
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Boolean RunSQL(String sql) {
        if (conn == null) {
            System.out.println("No database connection");
            return false;
        }
        try {
            Statement sqlStatement = conn.createStatement();
            sqlStatement.executeUpdate("PRAGMA foreign_keys = ON; " + sql);
            sqlStatement.close();
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public ResultSet RunSQLQuery(String sql) {
        ResultSet result;
        if (conn == null) {
            System.out.println("No database connection");
            return null;
        }

        try {
            Statement sqlStatement = conn.createStatement();
            result = sqlStatement.executeQuery(sql);
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            return null;
        }
        return result;
    }
}