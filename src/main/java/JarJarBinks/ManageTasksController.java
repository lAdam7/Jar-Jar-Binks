package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ManageTasksController extends Controller {

    @FXML
    private TableView<Task> table;
    @FXML
    private TableColumn<Task, String> tNameColumn;
    @FXML
    private TableColumn<Task, Number> tDeadlineColumn;

    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label description;
    @FXML
    private Label duration;
    @FXML
    private Label priority;
    @FXML
    private Label frequency;
    @FXML
    private Label category;
    @FXML
    private Label deadlineInDays;
    @FXML
    private Label llocation;
    @FXML
    private Label signingBy;

    @FXML
    private void initialize(){
        tNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        tDeadlineColumn.setCellValueFactory(cellData -> cellData.getValue().getDaysToFinishProperty() );

        showTask(null);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showTask(newVal)
        );
    }


    public void setData(List<Task> list) {
        table.setItems(FXCollections.observableArrayList(list));
    }

    private void showTask(Task task){
        if (task != null){
            name.setText(task.getName());
            type.setText(task.getTypeString());
            category.setText(task.getCategoryString());
            description.setText(task.getDescription());
            duration.setText(task.getDurationString());
            priority.setText(task.getPriorityString());
            if (task.getDaysToFinish() >= 1) {
                deadlineInDays.setText("Due to " + task.getDaysToFinish() + " days");
            }else {
                deadlineInDays.setText("Due today!");
            }
            llocation.setText(task.getLocation());
            if (task.getRequiresSigningOff()) {
                signingBy.setText(task.getRequireSigningByString());
            } else {
                signingBy.setText("Nobody");
            }
            if (task.getFrequency() >= 1){
                frequency.setText("Every " + task.getFrequency() + " days");
            }else{
                frequency.setText("Not applicable");
            }

        }
        else {
            name.setText("");
            type.setText("");
            category.setText("");
            description.setText("");
            duration.setText("");
            priority.setText("");
            deadlineInDays.setText("");
            llocation.setText("");
            signingBy.setText("");
            frequency.setText("");
        }
    }

    @FXML
    private void handleDeleteTask(ActionEvent actionEvent) {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Delete confirmation");
        int deletionIndex = table.getSelectionModel().getSelectedIndex();
        Task taskDeletion = table.getItems().get(deletionIndex);
        conf.setContentText("Are you sure you want to delete task: \nName: " + taskDeletion.getName() + ", Category: " + taskDeletion.getCategory() + ", Type: "+ taskDeletion.getType() + "?");
        Optional<ButtonType> result = conf.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.deleteTask(taskDeletion);
            table.getItems().remove(deletionIndex);
        }
        //if (deletionIndex==-1){
          //Alert deletionAlert = new Alert(Alert.AlertType.WARNING);
          //deletionAlert.setTitle("No task selected");
          //deletionAlert.setContentText("Select task to delete");
          //deletionAlert.showAndWait();}
    }

    @FXML
    private void handleEditTask(ActionEvent actionEvent) {
        Task taskEdition = table.getSelectionModel().getSelectedItem();
        if (taskEdition == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No task selected");
            alert.setContentText("Select task to edit");
            alert.showAndWait();
        }
        else {
            controller.editTask(taskEdition);
            showTask(taskEdition);
        }
    }


    @FXML
    private void handleAddTask(ActionEvent actionEvent) {
        controller.addTask();
    }

    public void addTaskToList(Task task) {
        table.getItems().add(task);
    }

    @FXML
    private void goToMenu(){ controller.goToMenu(); }

    @Override
    public void show() {

    }

}