package JarJarBinks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.text.BreakIterator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainController extends Application {
    private User loggedUser;
    private static BorderPane root;
    private Stage mainStage;
    private Controller currentUI;
    private DialogController currentDialog;
    private TaskSystemDatabase DB;

    @Override
    public void start(Stage stage) {
        try {
            DB = new TaskSystemDatabase();
            mainStage = stage;
            mainStage.setTitle("Jar Jar Binks");
            FXMLLoader loader = getLoader("root");
            root = loader.load();
            mainStage.setScene(new Scene(root, 640, 480));
            mainStage.setMinWidth(650);
            mainStage.setMinHeight(500);
            mainStage.show();
            showScreen("login");
        }
        catch (Exception e) {
            unexpectedError(e);
        }
    }


    public boolean userLogin(String username, String password) {
        try {
            String hashedPass = DB.getPassword(username);
            if(hashedPass == null)
                return false;
            if (new Password().validatePassword(password, hashedPass)) {
                loggedUser = DB.getUser(username);
                switch (loggedUser.getType()) {
                    case HR:
                        showScreen("hrMenu");
                        break;
                    case Admin:
                        showScreen("adminMenu");
                        break;
                    case Caretaker:
                        showScreen("caretakerMenu");
                        break;
                    default:
                        throw new InvalidParameterException("Invalid user type");
                }
                return true;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid credentials");
                alert.setContentText("Invalid username or password. Please try again");
                return false;
            }
        }
        catch (Exception e) {
            unexpectedError(e);
            return false;
        }
    }
    
    public void showPasswordChangeScreen() {
        showScreen("changePassword");
    }

    // manageUsers functions
    public void showManageUsersScreen() {
        showScreen("manageUsers");
        ((ManageUsersController)currentUI).setData(DB.getAllUsers());
    }

    public void AddUser() {
        User temp = new User();
        initDialog("addEditUser");
        ((addEditUserController)currentDialog).setEditedUser(temp);
        Stage diaStage = currentDialog.getDialogStage();
        diaStage.setTitle("Add user");
        diaStage.showAndWait();

        if(currentDialog.isOkClicked()) {
            try {
                temp.setType(((addEditUserController) currentDialog).getNewUserLevel());
                temp.setSurname(((addEditUserController) currentDialog).getNewLastName());
                temp.setName(((addEditUserController) currentDialog).getNewFirstName());
                temp.setUsername(((addEditUserController) currentDialog).getNewUsername());

                Password pass = new Password();
                String password = pass.generatePassword(8);
                String hashed = pass.createHash(password);

                if(DB.insertUser(temp, hashed)) {
                    ((ManageUsersController) currentUI).addUserToList(temp);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User added");
                    alert.setContentText("User was successfully added. Stress to them to change the password on their first login. Their login details are:\n username: " + temp.getUsername() + "\n password: " + password);
                    alert.showAndWait();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Could not add user");
                    alert.setContentText("Something went wrong while adding the user. Please restart the application");
                    alert.showAndWait();
                }
            }
            catch (Exception e) {
                unexpectedError(e);
            }
        }
    }

    public void EditUser(User editedUser) {
        initDialog("addEditUser");
        ((addEditUserController)currentDialog).setEditedUser(editedUser);
        Stage diaStage = currentDialog.getDialogStage();
        diaStage.setTitle("Edit user");
        ((addEditUserController) currentDialog).setUsernameReadOnly();
        ((addEditUserController) currentDialog).DisableUsernameChanging();
        diaStage.showAndWait();
        if(currentDialog.isOkClicked()) {
            editedUser.setName(((addEditUserController) currentDialog).getNewFirstName());
            editedUser.setSurname(((addEditUserController) currentDialog).getNewLastName());
            editedUser.setType(((addEditUserController) currentDialog).getNewUserLevel());

            if(!DB.updateUser(editedUser)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not edit user");
                alert.setContentText("Something went wrong while editing the user. Please restart the application");
                alert.showAndWait();
            }
        }
    }

    public void DeleteUser(User deletedUser) {
        DB.deleteUser(deletedUser.getUsername());
    }

    public void ResetPassword(User target) {
        try {
            Password pass = new Password();
            String password = pass.generatePassword(8);
            String hash = pass.createHash(password);

            if(DB.setUserPassword(target.getUsername(), hash)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password reset");
                alert.setContentText("Tha password for " + target.getName() + " " + target.getSurname() + " (" + target.getUsername() + ") was reset. Their current password is '" + password + "' (without quotes, the uppercase validation: " + password.toUpperCase() + "). Tell them the password and stress to them to change it on the next login");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error resetting password");
                alert.setContentText("Something went wrong while resetting password. Please restart the application and try again");
                alert.showAndWait();
            }
        }
        catch (Exception e) {
            unexpectedError(e);
        }
    }

    public boolean changePassword(String oldPass, String newPass) {
        try {
            Password pass = new Password();
            String currentPass = DB.getPassword(loggedUser.getUsername());
            if(pass.validatePassword(oldPass, currentPass)) {
                String newHashedPassword = pass.createHash(newPass);
                DB.setUserPassword(loggedUser.getUsername(), newHashedPassword);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            unexpectedError(e);
            return false;
        }
    }

    public Boolean checkUsernameExists(String username) {
        User test = DB.getUser(username);
        return test != null;
    }

    public boolean removePreference(Category category) {
        Boolean test = DB.unselectPreference(loggedUser.getUsername(), category);
        return test;
    }

    public boolean insertPreference(Category category) {
        Boolean test = DB.selectPreference(loggedUser.getUsername(), category);
        if (!test) {
            System.out.println("ooof");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Preference Limit Reached");
            alert.setContentText("Maximum of 2 Preferences!");
            alert.showAndWait();
        }
        return test;
    }

    public boolean insertAssigned(Task task){
        task.setCaretaker(loggedUser.getUsername());
        Boolean test = DB.assignTask(task);
        return test;
    }

    public boolean overrideAssigned(Task task){
        Boolean test = DB.assignTask(task);
        return test;
    }

    public boolean removeAssigned(Task task){
       Boolean test = DB.unassignTask(task);
       return test;
    }

    // manageTasks functions

    public void showManageTasksScreen(){
        showScreen("manageTasks");
        ((ManageTasksController)currentUI).setData(DB.getAllTasks());
    }

    public void addTask(){
        Task a = new Task();
        initDialog("addEditTask");
        ((addEditTaskController) currentDialog).setTitle("Add Task");
        ((addEditTaskController) currentDialog).setSigningByDisabled();
        //((addEditTaskController) currentDialog).setFrequencyHidden();
        ((addEditTaskController)currentDialog).setTaskToEdit(a);
        Stage diaStage = currentDialog.getDialogStage();
        diaStage.setTitle("Add task");
        diaStage.showAndWait();

        if (currentDialog.isOkClicked()){
            try {
                a.setName(((addEditTaskController) currentDialog).getNewTaskName());
                a.setDescription(((addEditTaskController) currentDialog).getNewDescription());
                a.setDuration(((addEditTaskController) currentDialog).getNewDuration());
                a.setFrequency(((addEditTaskController) currentDialog).getNewFrequency());
                a.setType(((addEditTaskController) currentDialog).getNewTaskType());
                a.setCategory(((addEditTaskController) currentDialog).getNewTaskCategory());
                a.setPriority(((addEditTaskController) currentDialog).getNewTaskPriority());
                a.setDaysToFinish(((addEditTaskController) currentDialog).getNewDeadline());
                a.setRequiresSigningOff(((addEditTaskController) currentDialog).getNewRequiresSigning());
                a.setRequireSigningBy(((addEditTaskController) currentDialog).getNewTaskSigningBy());
                a.setLocation(((addEditTaskController) currentDialog).getNewTaskLocation());

                if(DB.insertTask(a)) {
                    ((ManageTasksController) currentUI).addTaskToList(a);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Task added");
                    alert.setContentText("Task was successfully added." );
                    alert.showAndWait();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Could not add task");
                    alert.setContentText("Something went wrong while adding the task. Please restart the application");
                    alert.showAndWait();
                }
            }catch (Exception e) {
                    unexpectedError(e);
                }
        }

    }

    public void editTask(Task editedTask){
        initDialog("addEditTask");
        ((addEditTaskController) currentDialog).setTitle("Edit Task");
        ((addEditTaskController)currentDialog).setTaskToEdit(editedTask);
        ((addEditTaskController) currentDialog).setSigningByDisabled();
        Stage diaStage = currentDialog.getDialogStage();
        diaStage.setTitle("Edit task");
        diaStage.showAndWait();
        if(currentDialog.isOkClicked()) {
            editedTask.setName(((addEditTaskController) currentDialog).getNewTaskName());
            editedTask.setDescription(((addEditTaskController) currentDialog).getNewDescription());
            editedTask.setDuration(((addEditTaskController) currentDialog).getNewDuration());
            editedTask.setFrequency(((addEditTaskController) currentDialog).getNewFrequency());
            editedTask.setType(((addEditTaskController) currentDialog).getNewTaskType());
            editedTask.setCategory(((addEditTaskController) currentDialog).getNewTaskCategory());
            editedTask.setPriority(((addEditTaskController) currentDialog).getNewTaskPriority());
            editedTask.setDaysToFinish(((addEditTaskController) currentDialog).getNewDeadline());  
            editedTask.setRequiresSigningOff(((addEditTaskController) currentDialog).getNewRequiresSigning());
            editedTask.setRequireSigningBy(((addEditTaskController) currentDialog).getNewTaskSigningBy());
            editedTask.setLocation(((addEditTaskController) currentDialog).getNewTaskLocation());


            if(DB.updateTask(editedTask)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Task edited");
                alert.setContentText("Task was successfully edited." );
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not edit task");
                alert.setContentText("Something went wrong while editing the task. Please restart the application");
                alert.showAndWait();
            }
        }

    }

    public void deleteTask(Task deleteTk){
        DB.deleteTask(deleteTk.getTaskID());
    }

    //---- Reports ----
    public void showReportMenu(){
        showScreen("reportMenu");
    }

    public void showCaretakerPerformanceReportScreen(){
        showScreen("reportCaretakerPerformance");
        mainStage.setWidth(900);
        mainStage.setMinHeight(600);
        ArrayList<CaretakerAssignedTasks> caretakerPerformanceReport = DB.getAllUsersAndTasks(true);
        ((ReportCaretakerPerformanceController) currentUI).setCaretakerPerformanceReport(caretakerPerformanceReport);

    }
    public void showCompletionHistoryReportScreen(){
        showScreen("reportCompletionHistory");
        mainStage.setWidth(900);
        mainStage.setMinHeight(600);
        ArrayList<Task> completionHistory = DB.getAllAssignedTasks();
        ((ReportCompletionHistoryController) currentUI).setCompletionHistoryReport(completionHistory);

    }
    public void showCurrentStatusReportScreen(){
        showScreen("reportCurrentStatus");
        mainStage.setWidth(900);
        mainStage.setMinHeight(600);
        ArrayList<Task> currentStatusReportList = DB.getAllAssignedTasks();
        ((ReportCurrentStatusController) currentUI).setCurrentTaskReport(currentStatusReportList);

    }

    public void showUnlistedTaskScreen() {
        showScreen("logUnlistedTask");
    }

    public void showMyTasksScreen() {
        try {
            showScreen("myTasksCaretaker");
            mainStage.setWidth(1000);
            mainStage.setMinHeight(800);
            ArrayList<Task> signingList = DB.getTasksNeedingSignature(loggedUser.getUsername());

            for (int i = signingList.size() -1 ; i >= 0; i--)
                if(signingList.get(i).getRequireSigningBy() != UserLevel.Caretaker)
                    signingList.remove(i);

            Util.sortTasksByDeadline(false, signingList);

            ArrayList<Task> listOfMyTasks = DB.getUsersAssignedTasks(loggedUser.getUsername(), true);

            Util.sortTasksByDeadline(false, listOfMyTasks);
            Util.sortTasksByStatus(true, listOfMyTasks);

            ((taskListController) currentUI).setMyTasks(listOfMyTasks);
            ((taskListController) currentUI).setSigningTasks(signingList);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void showAdminLogTaskScreen() {
        showScreen("logTaskAdmin");
        mainStage.setWidth(1000);
        mainStage.setHeight(800);
        ArrayList<Task> taskList = DB.getTasksNeedingSignature(loggedUser.getUsername());

        for (int i = taskList.size() -1 ; i >= 0; i--)
            if(taskList.get(i).getRequireSigningBy() != UserLevel.Admin)
                taskList.remove(i);
        Util.sortTasksByDeadline(false, taskList); //here
        Util.sortTasksByStatus(true, taskList); //here

        ((LogTaskAdminController) currentUI).setTasksRequiringManagerSign(taskList);
    }

    public void showPreferencesScreen() {
        showScreen("selectPreferences");
        ((PreferencesController)currentUI).setData(DB.getUsersCategories(loggedUser.getUsername()));
    }

    public void showTodaysTaskScreen() {
        showScreen("todaysTasksCaretaker");
        ((CaretakerClassController)currentUI).setData(DB.getAllUsersAndTasks(false),DB.getUnAssignedTasks());

    }

    public void showOverrideTaskScreen(){
        showScreen("overrideTask");
        ((overrideTaskController)currentUI).setData(DB.getAllUsersAndTasks(false),DB.getUnAssignedTasks());
    }

    public void showSelectTasksScreen(){
        showScreen("selectTasks");
        ArrayList<Task> unassignedList = DB.getUnAssignedTasks();
        ArrayList<Category> preferenceList = DB.getUsersCategories(loggedUser.getUsername());

        Iterator<Category> iterPrefList = preferenceList.iterator();
        while (iterPrefList.hasNext()) {
            Category category = iterPrefList.next();
            if (category.getEnabled())
            {
                int count = 0;
                while (unassignedList.size() > count) {
                    Task task = unassignedList.get(count);
                    if (task.getCategory() == category.getCategoryType()) {
                        unassignedList.remove(count);
                        unassignedList.add(0, task);
                    }
                    count++;
                }

            }
        }
        ((selectTasksController)currentUI).setData(unassignedList,DB.getUsersAssignedTasks(loggedUser.getUsername(), false));
    }

    public void goToMenu() {
        switch (loggedUser.getType()) {
            case Caretaker:
                showScreen("caretakerMenu");
                break;
            case Admin:
                showScreen("adminMenu");
                break;
            case HR:
                showScreen("hrMenu");
                break;
        }
    }

    public void userLogout() {
        loggedUser = null;
        showScreen("login");
    }

    public void signOffTask(Task target) {
        if (target == null) {
            alertNoItemSelected();
        }
        else {
            target.setStatus(taskStatus.completed);
            target.setSignedBy(loggedUser.getUsername());
            target.setDateSignedOff(LocalDate.now());
            DB.updateAssignedTask(target);
        }
    }

    public void completeTask(Task task) {
        if(task == null) {
            alertNoItemSelected();
        }
        else {
            task.setDateCompleted(LocalDate.now());
            task.setStatus(task.getRequiresSigningOff() ? taskStatus.pendingReview : taskStatus.completed);
            DB.updateAssignedTask(task);
        }
    }

    public List<Comment> getComments(Task task) {
        return DB.getComments(task);
    }

    public void addUnlistedTask(Task task) {
        task.setCaretaker(loggedUser.getUsername());
        DB.caretakerAssignCompleteTask(task);
    }

    public void addComment(Comment comm, Task currTask) {
        comm.setUsername(loggedUser.getUsername());
        comm.setfName(loggedUser.getName());
        comm.setlName(loggedUser.getSurname());

        DB.addComment(currTask, comm);
    }

    private void alertNoItemSelected() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Please select an item");
        alert.setTitle("No item selected");
        alert.showAndWait();
    }

    private void initDialog(String fxml) {
        try {
            FXMLLoader loader = getLoader(fxml);
            Parent pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initOwner(mainStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene dialogScene = new Scene(pane);

            dialogStage.setScene(dialogScene);

            currentDialog = loader.getController();
            currentDialog.setDialogStage(dialogStage);
            currentDialog.setController(this);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showScreen(String fxml) {
        try {
            FXMLLoader loader = getLoader(fxml);
            Parent pane = loader.load();
            root.setCenter(pane);

            currentUI = loader.getController();
            currentUI.setController(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FXMLLoader getLoader(String fxml) throws IOException {
        return new FXMLLoader(MainController.class.getResource(fxml + ".fxml"));
    }

    private void unexpectedError(Exception p) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unexpected error");
        alert.setContentText("Unexpected error has occured inside the application. The application will now exit");
        alert.showAndWait();
        System.out.println("Stop information");
        System.out.println(p.getMessage());
        p.printStackTrace();
        try {
            stop();
        }
        catch (Exception e) {
            System.out.println("TERMINATING APP FAILED");
        }

    }

    public static void main(String[] args) {
        launch();
    }

}