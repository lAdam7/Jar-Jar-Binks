package JUnit;

import JarJarBinks.*;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TaskSystemDatabaseTest {

    private TaskSystemDatabase TSD;

    public TaskSystemDatabaseTest() {
        TSD = new TaskSystemDatabase();
    }

    @Test
    public void getUserCorrect() {
        User userInfo = TSD.getUser("caretaker");

        assertEquals("username not the same", userInfo.getUsername(), "caretaker");
        assertEquals("type not the same", userInfo.getType(), UserLevel.Caretaker);
    }

    @Test
    public void getUserIncorrect() {
        User userInfo = TSD.getUser("hta");
        assertNull("User returned?", userInfo);
    }

    @Test
    public void getAllUsers() {
        ArrayList<User> allUsers = TSD.getAllUsers();

        assertNotNull("No users retrieved", allUsers);

        for (User user : allUsers) {
            assertNotNull("No username", user.getUsername());
            assertEquals("type incorrect", user.getType(), UserLevel.valueOf(user.getType().toString()));
            assertNotNull("No name", user.getName());
            assertNotNull("No surname", user.getSurname());
        }
    }

    @Test
    public void deleteInsertUserCorrect() {
        Boolean deleteUser = TSD.deleteUser("theodoraD");
        assertTrue("User not deleted", deleteUser);

        User ud = new User();
        ud.setUsername("theodoraD");
        ud.setType(UserLevel.Admin);
        ud.setName("Daniel");
        ud.setSurname("Theodora");

        Boolean insertUser = TSD.insertUser(ud, "theo");
        assertTrue("User not inserted", insertUser);
    }

    @Test
    public void deleteUserIncorrect() {
        Boolean deleteUser = TSD.deleteUser("aczxa");
        assertTrue("SQL structure is valid", deleteUser);
    }

    @Test
    public void updateUserCorrect() {
        User ud = new User();
        ud.setUsername("marjaG");
        ud.setType(UserLevel.Admin);
        ud.setName("Gust");
        ud.setSurname("Marja");

        Boolean updateUser = TSD.updateUser(ud);
        assertTrue("User hasn't updated", updateUser);
    }

    @Test
    public void updateUserIncorrect() {
        User ud = new User();
        ud.setUsername("caretaker");
        ud.setType(UserLevel.Caretaker);
        ud.setName("Tom");
        ud.setSurname("Smith");

        Boolean updateUser = TSD.updateUser(ud);
        assertTrue("User already exists", updateUser);
    }

    @Test
    public void getUserPasswordCorrect() {
        String getPassword = TSD.getPassword("caretaker");
        assertEquals("Correct password not returned", getPassword, "33F4BDA9956A43254A4985BF61A70B9FD1991351F0100899:5862EBC09749C956550139866D596693661F048CD9190D44320838DDA809D205DACD4C52DC9AEE5E2F6652C451C2D675A0E351AE1DDF8687356E4C02AF816F8C39FD1D5F4BB6105A6C383C4ABA89DA1FEA47495BE2EA85D06887A0CACC911E6957848CFD6410348EA3803D8CEF865B78AAFB0CF3ABBB37F89D783B8255F5F480:2500");
    }

    @Test
    public void getUserPasswordIncorrect() {
        String getPassword = TSD.getPassword("fas");
        assertNull("User shouldn't exist", getPassword);
    }

    @Test
    public void setUserPasswordCorrect() {
        Boolean setPassword = TSD.setUserPassword("marjaG", "aaaaa");
        assertTrue("Password hasn't been changed", setPassword);
    }

    @Test
    public void setUserPasswordIncorrect() {
        Boolean setPassword = TSD.setUserPassword("smith3123", "smith");
        assertTrue("SQL structure is invalid", setPassword);
    }

    @Test
    public void getAllTasks() {
        ArrayList<Task> allTasks = TSD.getAllTasks();

        assertNotNull("No tasks retrieved", allTasks);

        for (Task task : allTasks) {
            assertNotNull("no taskID", task.getTaskID());
            assertNotNull("no type", task.getType());
            assertTrue("frequency error", task.getFrequency() >= 0);
            assertNotNull("no category", task.getCategory());
            assertNotNull("no description", task.getDescription());
            assertTrue("duration error", task.getDuration() > 0);
            assertNotNull("no priority", task.getPriority());
            assertNotNull("no location", task.getLocation());
            assertNotNull("no requiresSigningOff", task.getRequiresSigningOff());
            assertNotNull("no name", task.getName());
        }
    }

    @Test
    public void deleteInsertTaskCorrect() throws SQLException {
        Task td = new Task();
        td.setName("JUnit Test");
        td.setDescription("Used for testing");
        td.setDaysToFinish(2);
        td.setDuration(30);
        td.setPriority(taskPriority.High);
        td.setFrequency(3);
        td.setCategory(CategoryType.Tidy);
        td.setLocation("Block B");
        td.setRequireSigningBy(UserLevel.Admin);

        Boolean insertTask = TSD.insertTask(td);
        assertTrue("Task not inserted", insertTask);

        DBConnection DBConn = new DBConnection();
        DBConn.Connect("assignmentData.db");
        ResultSet getTaskID = DBConn.RunSQLQuery("SELECT taskID FROM tblTask WHERE taskName = 'JUnit Test';");
        Integer taskID = getTaskID.getInt(1);
        getTaskID.close();

        Boolean deleteTask = TSD.deleteTask(taskID);
        assertTrue("Task not deleted", deleteTask);
    }

    @Test
    public void category() {
        ArrayList<String> getCategories = TSD.getCategories();

        assertTrue("No categories returned", getCategories.size() >= 5);
    }

    @Test
    public void getUsersCategoriesCorrect() {
        ArrayList<Category> usersCategories = TSD.getUsersCategories("fleggM");

        assertNotNull("No category retrieved", usersCategories);

        for (Category category : usersCategories) {
            assertNotNull(category.getCategoryType());
            for (Task task : category.getTask()) {
                assertNotNull("no taskID", task.getTaskID());
                assertNotNull("no type", task.getType());
                assertTrue("frequency error", task.getFrequency() >= 0);
                assertNotNull("no description", task.getDescription());
                assertTrue("duration error", task.getDuration() > 0);
                assertNotNull("no priority", task.getPriority());
                assertNotNull("no location", task.getLocation());
                assertNotNull("no requiresSigningOff", task.getRequiresSigningOff());
                assertNotNull("no name", task.getName());

                assertEquals("category doesn't match", task.getCategory(), category.getCategoryType());
            }
        }
    }

    @Test
    public void getUsersCategoriesIncorrect() {
        ArrayList<Category> usersCategories = TSD.getUsersCategories("vad");

        assertTrue("categories not returned", usersCategories.size() >= 5);
    }

    @Test
    public void getUsersAssignedTasksCorrect() {
        ArrayList<Task> allTasks = TSD.getUsersAssignedTasks("vCzechmate", false);

        assertNotNull("Null received", allTasks);

        for (Task task : allTasks) {
            assertNotNull("no taskID", task.getTaskID());
            assertNotNull("no type", task.getType());
            assertTrue("frequency error", task.getFrequency() >= 0);
            assertNotNull("no category", task.getCategory());
            assertNotNull("no description", task.getDescription());
            assertNotNull("no deadline", task.getDeadline());
            assertTrue("duration error", task.getDuration() > 0);
            assertNotNull("no priority", task.getPriority());
            assertNotNull("no location", task.getLocation());
            assertNotNull("no requiresSigningOff", task.getRequiresSigningOff());
            assertNotNull("no name", task.getName());
        }
    }

    @Test
    public void getUsersAssignedTasksIncorrect() {
        ArrayList<Task> allTasks = TSD.getUsersAssignedTasks("alf", false);
        assertTrue("user doesn't exist", allTasks.size() == 0);
    }

    @Test
    public void getSignaturesRequiredCorrect() {
        ArrayList<Task> allTasks = TSD.getTasksNeedingSignature("caretaker");

        assertNotNull("Null received", allTasks);

        for (Task task : allTasks) {
            assertNotNull("no taskID", task.getTaskID());
            assertNotNull("no type", task.getType());
            assertTrue("frequency error", task.getFrequency() >= 0);
            assertNotNull("no category", task.getCategory());
            assertNotNull("no description", task.getDescription());
            assertTrue("duration error", task.getDuration() > 0);
            assertNotNull("no priority", task.getPriority());
            assertNotNull("no location", task.getLocation());
            assertNotNull("no name", task.getName());

            assertTrue("requiresSigningOff should be true", task.getRequiresSigningOff());
        }
    }

    @Test
    public void getSignaturesRequiredIncorrect() {
        ArrayList<Task> allTasks = TSD.getTasksNeedingSignature("caretaker");
        assertTrue("HR can't sign", allTasks.size() >= 0);
    }

    @Test
    public void selectUnselectPreferences() {
        Category category = new Category();
        category.setCategoryType(CategoryType.Fix);

        Boolean unselectPreference = TSD.unselectPreference("gPeck", category);
        assertTrue("Preference SQL unselect", unselectPreference);

        Boolean selectPreference = TSD.selectPreference("gPeck", category);
        assertTrue("Preference SQL select", selectPreference);
    }

    @Test
    public void getAllUsersAndTasks() {
        ArrayList<CaretakerAssignedTasks> assignedUserAndTasks = TSD.getAllUsersAndTasks(false);

        assertNotNull("Null received", assignedUserAndTasks);

        for (CaretakerAssignedTasks temp : assignedUserAndTasks) {
            assertNotNull("No username", temp.getUser().getUsername());
            assertNotNull("No name", temp.getUser().getName());
            assertNotNull("No surname", temp.getUser().getSurname());
            for (Task task : temp.getTask()) {
                assertNotNull("no taskID", task.getTaskID());
                assertNotNull("no type", task.getType());
                assertTrue("frequency error", task.getFrequency() >= 0);
                assertNotNull("no category", task.getCategory());
                assertNotNull("no description", task.getDescription());
                assertTrue("duration error", task.getDuration() > 0);
                assertNotNull("no priority", task.getPriority());
                assertNotNull("no location", task.getLocation());
                assertNotNull("no requiresSigningOff", task.getRequiresSigningOff());
                assertNotNull("no name", task.getName());
            }
        }
    }

    @Test
    public void preferenceLimitTrigger() {
        Category categoryFix = new Category();
        categoryFix.setCategoryType(CategoryType.Fix);
        Category categoryClean = new Category();
        categoryFix.setCategoryType(CategoryType.Clean);
        Category categoryTidy = new Category();
        categoryFix.setCategoryType(CategoryType.Tidy);

        TSD.selectPreference("caretaker", categoryFix);
        TSD.selectPreference("caretaker", categoryClean);
        Boolean selectPreferenceTidy = TSD.selectPreference("caretaker", categoryTidy);

        assertFalse("Max preferences breached", selectPreferenceTidy);
    }

    @Test
    public void caretakerCheckTrigger() {
        Task task = new Task();
        task.setTaskID(1);
        task.setCaretaker("admin");

        Boolean addTask = TSD.assignTask(task);
        assertFalse("Admin can't be assigned to a task", addTask);
    }

    @Test
    public void taskApprovalTrigger() {
        Task task = new Task();
        task.setTaskID(4);
        task.setCaretaker("caretaker");
        task.setDateAssigned(LocalDate.now());

        Boolean addTask = TSD.assignTask(task);
        assertTrue("Task failed to insert?", addTask);

        Boolean unassignTask = TSD.unassignTask(task);
        assertTrue("Task not deleted?", unassignTask);

    }

}
