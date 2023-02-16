package JarJarBinks;

import javafx.fxml.FXML;

public class MenuController extends Controller {

    @Override
    public void show() {
    }

    @FXML
    private void changePassword() {
        controller.showPasswordChangeScreen();
    }


    @FXML
    private void manageTasks() {
        controller.showManageTasksScreen();
    }

    @FXML
    private void overrideTaskAllocation() {controller.showOverrideTaskScreen();}

    @FXML
    private void logTask() {
        controller.showAdminLogTaskScreen();
    }


    @FXML
    private void logOut() {
        controller.userLogout();
    }

    @FXML
    private void selectPreferences() { controller.showPreferencesScreen();}

    @FXML
    private void showMyTasks() {
        controller.showMyTasksScreen();
    }

    @FXML
    private void showReportsAdmin() {
        controller.showReportMenu();
    }

    @FXML
    private void showSelectTasks() {controller.showSelectTasksScreen();}

    @FXML
    private void showTodaysTasks() { controller.showTodaysTaskScreen();}


    @FXML
    private void showLogUnlistedTask() {
        controller.showUnlistedTaskScreen();
    }

    @FXML
    private void manageUsers()
    {
        controller.showManageUsersScreen();
    }

    @FXML
    private void caretakerPerformance(){ controller.showCaretakerPerformanceReportScreen();}

    @FXML
    private void completionHistory(){controller.showCompletionHistoryReportScreen();}

    @FXML
    private void currentTaskStatus(){controller.showCurrentStatusReportScreen();}

}