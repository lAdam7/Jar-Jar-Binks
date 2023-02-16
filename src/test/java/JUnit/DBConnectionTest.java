package JUnit;

import JarJarBinks.DBConnection;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionTest {

    private DBConnection database;

    public DBConnectionTest() {
        database = new DBConnection();
        database.Connect("assignmentData.db");
    }

    @Test
    public void RunSQLCorrect() {
        Boolean sql = database.RunSQL("SELECT username FROM tblStaff");

        assertTrue("SQL is invalid", sql);
    }

    @Test
    public void RunSQLIncorrect() {
        Boolean sql = database.RunSQL("INSERT INTO tblStaff (password, hierarchy, forename, surname) VALUES ('a', 1, 'Jeff', 'Way')");

        assertFalse("SQL is valid", sql);
    }

    @Test
    public void RunSQLQueryCorrect() {
        ResultSet udDB = database.RunSQLQuery("SELECT username, hierarchyDesc, forename, surname FROM tblStaff INNER JOIN tblHierarchy on tblHierarchy.hierarchyID = tblStaff.hierarchy WHERE username = 'marjaG'");
        assertNotNull("SQL query is invalid", udDB);

        if (udDB != null) { // ResultSet needs to be closed
            try {
                udDB.close();
            } catch (SQLException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    public void RunSQLQueryIncorrect() {
        ResultSet udDB = database.RunSQLQuery("SLECT * from tblTask");
        assertNull("SQL query is valid", udDB);

        if (udDB != null) { // ResultSet needs to be closed
            try {
                udDB.close();
            } catch (SQLException e) {
                fail(e.getMessage());
            }
        }
    }

}