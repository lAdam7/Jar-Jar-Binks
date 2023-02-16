package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class overrideTaskController extends Controller {

    @FXML
    private TableView<CaretakerAssignedTasks> userTable;
    @FXML
    private TableView<Task> assignedTaskTable;
    @FXML
    private TableView<Task> unAssignedTable;
    @FXML
    private TableColumn<CaretakerAssignedTasks, String> fNameColumn;
    @FXML
    private TableColumn<CaretakerAssignedTasks, String> lNameColumn; //User Display

    @FXML
    private TableColumn<Task, String> UnAssTaskName;

    @FXML
    private TableColumn<Task, String> UnAssTaskDesc;

    @FXML
    private TableColumn<Task, String> taskName; //Task Display for Users

    @FXML
    private TableColumn<Task, String> taskType;

    @FXML
    private TableColumn<Task, String> taskDesc;

    @FXML
    private TableColumn<Task, String> taskDuration;

    @FXML
    private TableColumn<Task, String> taskPriority;

    @FXML
    private TableColumn<Task, String> taskFrequency;

    @FXML
    private Button assignBtn;

    @FXML
    private Button unAssignBtn;

    @FXML
    private void initialize() {
        fNameColumn.setCellValueFactory(data -> data.getValue().getUser().getNameProperty());
        lNameColumn.setCellValueFactory(data -> data.getValue().getUser().getSurnameProperty());
        UnAssTaskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        UnAssTaskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());

        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showTaskDetails(newVal.getTask())
        );

        assignedTaskTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> hideUBtn() );

        unAssignedTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal ) -> hideABtn() );
    }

    private void showTaskDetails(ArrayList<Task> assignedTasks){
        taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskType.setCellValueFactory(data -> data.getValue().getTypeProperty().asString());
        taskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());
        taskDuration.setCellValueFactory(data -> data.getValue().getDurationStringProperty());
        taskPriority.setCellValueFactory(data -> data.getValue().getPriorityProperty().asString());
        taskFrequency.setCellValueFactory(data -> data.getValue().getFrequencyProperty().asString());
        assignedTaskTable.setItems(FXCollections.observableArrayList(assignedTasks));
    }

    @FXML
    private void goToMenu(){ controller.goToMenu(); }

    public void setData(ArrayList<CaretakerAssignedTasks> list, List<Task> unassignedList) {
        userTable.setItems(FXCollections.observableArrayList(list));
        unAssignedTable.setItems(FXCollections.observableArrayList(unassignedList));
    }

    @FXML
    private void storeAssigned(){
        Task unAssTask = unAssignedTable.getSelectionModel().getSelectedItem();
        CaretakerAssignedTasks user = userTable.getSelectionModel().getSelectedItem();

        if (unAssTask.getCaretaker() == null)
        {
            unAssTask.setCaretaker(user.getUser().getUsername());
            controller.overrideAssigned(unAssTask);
            user.getTask().add(unAssTask);   //Add task to the live list
            moveUnAssTask();
        }
    }
    @FXML
    private void removeAssigned(){
        Task AssTask = assignedTaskTable.getSelectionModel().getSelectedItem();

        if (AssTask.getCaretaker() != null)
        {
            controller.removeAssigned(AssTask);
            AssTask.setCaretaker(null);
            moveAssTask();
        }
    }
    private void moveUnAssTask(){
        Task task = unAssignedTable.getSelectionModel().getSelectedItem();
        assignedTaskTable.getItems().add(task);
        unAssignedTable.getItems().remove(unAssignedTable.getSelectionModel().getSelectedItem());
    }

    private void moveAssTask(){
        unAssignedTable.getItems().add(assignedTaskTable.getSelectionModel().getSelectedItem());
        assignedTaskTable.getItems().remove(assignedTaskTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void hideUBtn(){
        assignBtn.setVisible(false);
        assignBtn.setDisable(true);
        unAssignBtn.setVisible(true);
        unAssignBtn.setDisable(false);
    }

    @FXML
    private void hideABtn(){
        assignBtn.setVisible(true);
        assignBtn.setDisable(false);
        unAssignBtn.setVisible(false);
        unAssignBtn.setDisable(true);
    }



    public void show(){ }
}
