package JarJarBinks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class CaretakerClassController extends Controller{


    @FXML
    private TableView<CaretakerAssignedTasks> userTable;
    @FXML
    private TableView<Task> taskTable;
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
    private TableColumn<Task, String> UnAssTaskPriority;

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
    private void initialize() {
        fNameColumn.setCellValueFactory(data -> data.getValue().getUser().getNameProperty());
        lNameColumn.setCellValueFactory(data -> data.getValue().getUser().getSurnameProperty());
        UnAssTaskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        UnAssTaskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());
        UnAssTaskPriority.setCellValueFactory(data -> data.getValue().getPriorityProperty().asString());

        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showTaskDetails(newVal.getTask())
       );
    }


     private void showTaskDetails(ArrayList<Task> assignedTasks){
        taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskType.setCellValueFactory(data -> data.getValue().getTypeProperty().asString());
        taskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());
        taskDuration.setCellValueFactory(data -> data.getValue().getDurationStringProperty());
        taskPriority.setCellValueFactory(data -> data.getValue().getPriorityProperty().asString());
        taskFrequency.setCellValueFactory(data -> data.getValue().getFrequencyProperty().asString());
        taskTable.setItems(FXCollections.observableArrayList(assignedTasks));
    }


    @FXML
    private void goToMenu(){ controller.goToMenu(); }



    public void setData(List<CaretakerAssignedTasks> list, List<Task> unassignedList) {
        userTable.setItems(FXCollections.observableArrayList(list));
        unAssignedTable.setItems(FXCollections.observableArrayList(unassignedList));
    }




    @Override
    public void show() {

    }
}
