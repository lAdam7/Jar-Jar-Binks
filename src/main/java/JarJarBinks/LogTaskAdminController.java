package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogTaskAdminController extends Controller {

    @FXML
    private Label name;
    @FXML
    private Label description;
    @FXML
    private Label priority;
    @FXML
    private Label locationTask;
    @FXML
    private Label duration;
    @FXML
    private Label type;

    @FXML
    private TableView<Task> table;
    @FXML
    private TableColumn<Task, String> taskName;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadline;

    @FXML
    private TableView<Comment> tableComment;
    @FXML
    private TableColumn<Comment, String> commentName;
    @FXML
    private TableColumn<Comment, String> commentMessage;
    @FXML
    private TextField commentField;

    @FXML
    private void initialize() {
        taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskDeadline.setCellValueFactory(data -> data.getValue().getDeadlineProperty());

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    displayTaskDetails(newVal);
                    displayComments(newVal);
                }
        );
        displayTaskDetails(null);
        displayComments(null);
    }

    @FXML
    private void goToMenu() {
        controller.goToMenu();
    }

    @FXML
    public void addComment() {
        if(commentField.getText() == null || commentField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Comment field is empty");
            alert.setContentText("Please enter text before commenting");
            alert.showAndWait();
            return;
        }
        Comment newComment = new Comment();
        newComment.setComment(commentField.getText());
        controller.addComment(newComment, table.getSelectionModel().getSelectedItem());
        tableComment.getItems().add(newComment);
        commentField.setText("");
        fillComments();
    }

    @FXML
    private void signOffTask() {
        controller.signOffTask(table.getSelectionModel().getSelectedItem());
        table.getItems().remove(table.getSelectionModel().getSelectedIndex());
    }

    public void setTasksRequiringManagerSign(List<Task> taskList) {
        if(taskList != null)
            table.setItems(FXCollections.observableArrayList(taskList));
    }

    private void displayTaskDetails(Task task) {
        if(task == null) {
            name.setText("");
            description.setText("");
            priority.setText("");
            locationTask.setText("");
            duration.setText("");
            type.setText("");
        }
        else {
            name.setText(task.getName());
            description.setText(task.getDescription());
            priority.setText(task.getPriorityString());
            locationTask.setText(task.getLocation());
            duration.setText(task.getDurationString());
            type.setText(task.getTypeString());
        }
    }

    private void displayComments(Task task) {
        if(task != null) {
            List<Comment> comments = controller.getComments(task);
            if(comments != null) {
                tableComment.setItems(FXCollections.observableArrayList(comments));
                fillComments();
            }
        }
    }

    private void fillComments() {
        commentName.setCellValueFactory(data -> data.getValue().getNameProperty());
        commentMessage.setCellValueFactory(data -> data.getValue().getCommentProperty());
    }

    @Override
    public void show() {

    }
}
