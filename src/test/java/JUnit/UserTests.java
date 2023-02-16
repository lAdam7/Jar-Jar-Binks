package JUnit;

import JarJarBinks.MainController;
import JarJarBinks.Password;
import JarJarBinks.Task;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTests {
    public Password getPasswordClass() {
        return new Password();
    }
    public Task getTaskClass() {return new Task();}

    @Test
    public void validateCorrectPassword() {
        try {
            String passHash = getPasswordClass().createHash("This is some? good! password 69_");
            Boolean passValid = getPasswordClass().validatePassword("This is some? good! password 69_", passHash);
            assertTrue(passValid);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void validateIncorrectPassword() {
        try {
            String passHash = getPasswordClass().createHash("This is some? good! password 69_");
            Boolean passValid = getPasswordClass().validatePassword("This is some? good! password 69-", passHash);
            assertFalse(passValid);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPasswordGenerator() {
        String generatedPassword = getPasswordClass().generatePassword(8);
        System.out.println(generatedPassword);
        assertEquals(8, generatedPassword.length());
    }

    @Test
    public void testDurationToStringTask() {
        Task task = getTaskClass();
        task.setDuration(69);
        String durationString = task.getDurationString();
        assertEquals("1 hr 9 min", durationString);
    }
}
