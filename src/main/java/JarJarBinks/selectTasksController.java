package JarJarBinks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class selectTasksController extends Controller {



    @FXML
    private TableView<Task> unAssignedTable;
    @FXML
    private TableView<Task> assignedTable; //FXML for the 2 different tables

    @FXML
    private TableColumn<Task, String> assignedName;
    @FXML
    private TableColumn<Task, String> assignedDeadline;
    @FXML
    private TableColumn<Task, String> assignedDuration;
    //Name, Deadline, and Duration for the assigned table after selection



    @FXML
    private TableColumn<Task, String> taskName; //Task Display for unassigned tasks ready to be selected
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
    private Label DurationLabel;

    @FXML
    private void goToMenu() { controller.goToMenu(); }




    @FXML
    private void initialize() {

        taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskType.setCellValueFactory(data -> data.getValue().getTypeProperty().asString());
        taskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());

        taskDuration.setCellValueFactory(data -> data.getValue().getDurationStringProperty());
        taskPriority.setCellValueFactory(data -> data.getValue().getPriorityProperty().asString());
        taskFrequency.setCellValueFactory(data -> data.getValue().getFrequencyProperty().asString());


        assignedName.setCellValueFactory(data -> data.getValue().getNameProperty());
        assignedDeadline.setCellValueFactory(data -> data.getValue().getDeadlineProperty().asString());
        assignedDuration.setCellValueFactory(data -> data.getValue().getDurationStringProperty());

        assignedTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> hideUBtn() );

        unAssignedTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal ) -> hideABtn() );

        calcDuration();
    }


    public void setData(ArrayList<Task> unassignedList, ArrayList<Task> assignedList) {
        unAssignedTable.setItems(FXCollections.observableArrayList(unassignedList));
        assignedTable.setItems(FXCollections.observableArrayList(assignedList));
    }

    private void moveUnAssTask(){
        Task task = unAssignedTable.getSelectionModel().getSelectedItem();
        assignedTable.getItems().add(task);
        unAssignedTable.getItems().remove(unAssignedTable.getSelectionModel().getSelectedItem());
        calcDuration();
    }

    private void moveAssTask(){
        unAssignedTable.getItems().add(assignedTable.getSelectionModel().getSelectedItem());
        assignedTable.getItems().remove(assignedTable.getSelectionModel().getSelectedItem());
        calcDuration();
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

    @FXML
    private void calcDuration(){
        ObservableList<Task> allTasks = assignedTable.getItems();
        Integer totalTime = 0;
        for(Task task: allTasks){
            totalTime = totalTime + task.getDuration();
        }
        String p = "";
        if(totalTime >= 60)
            p += Integer.toString(totalTime / 60) + " hr ";
        if(totalTime % 60 != 0)
            p += Integer.toString(totalTime % 60) + " min";
        DurationLabel.setText(p);
    }



    @FXML
    private void storeAssigned(){
        Task unAssTask = unAssignedTable.getSelectionModel().getSelectedItem();

        if (unAssTask.getCaretaker() == null)
        {
            controller.insertAssigned(unAssTask);
            unAssTask.setDeadline(LocalDate.now());
            moveUnAssTask();
        }
    }

    @FXML
    private void removeAssigned(){
        Task AssTask = assignedTable.getSelectionModel().getSelectedItem();

        if (AssTask.getCaretaker() != null)
        {
            controller.removeAssigned(AssTask);
            AssTask.setCaretaker(null);
            moveAssTask();
        }
    }


    @Override
    public void show() {

    }

}
