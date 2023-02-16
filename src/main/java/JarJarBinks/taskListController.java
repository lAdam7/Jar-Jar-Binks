package JarJarBinks;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class taskListController extends Controller {

    @FXML
    private TableView<Task> tableAllMy;
    @FXML
    private TableColumn<Task, String> taskNameAllMy;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadlineAllMY;
    @FXML
    private TableColumn<Task, String> taskCompletedAllMy;

    @FXML
    private TableView<Task> tableRequireSigning;
    @FXML
    private TableColumn<Task, String> taskNameRequireSigning;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadlineRequireSigning;

    @FXML
    private TableView<Comment> tableComments;
    @FXML
    private TableColumn<Comment, String> commentName;
    @FXML
    private TableColumn<Comment, String> commentString;
    @FXML
    private TextField commentField;

    @FXML
    private Label name;
    @FXML
    private Label description;
    @FXML
    private Label priority;
    @FXML
    private Label duration;
    @FXML
    private Label type;
    @FXML
    private Label locationMyTasks;
    @FXML
    private Button completeBtn;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab myTasks;
    @FXML
    private Tab tasksNeedingSigning;

    @FXML
    private void initialize() {
        taskNameAllMy.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskDeadlineAllMY.setCellValueFactory(data -> data.getValue().getDeadlineProperty());
        taskCompletedAllMy.setCellValueFactory(data -> data.getValue().getStatusStringProperty());

        taskNameRequireSigning.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskDeadlineRequireSigning.setCellValueFactory(data -> data.getValue().getDeadlineProperty());

        tableAllMy.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    displayTaskDetails(newVal);
                    displayComments(newVal);
                    if(newVal != null)
                        completeBtn.setDisable(newVal.getStatus() != taskStatus.WIP);
                }
        );
        tableRequireSigning.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    displayTaskDetails(newVal);
                    displayComments(newVal);
                }
        );
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTab, newTab) -> {
                    displayTaskDetails(null);
                    displayComments(null);
                    tableRequireSigning.getSelectionModel().clearSelection();
                    tableAllMy.getSelectionModel().clearSelection();
                    tableComments.getItems().clear();
                    switch (getSelectedTab()) {
                        case myTasks:
                            completeBtn.setText("Complete task");
                            break;
                        case tasksNeedSigning:
                            completeBtn.setText("Sign off task");
                            completeBtn.setDisable(false);
                            break;
                    }
                }
        );

        displayTaskDetails(null);
        displayComments(null);
        tableRequireSigning.getSelectionModel().clearSelection();
        tableAllMy.getSelectionModel().clearSelection();
        completeBtn.setDisable(true);
        System.out.println(getSelectedTab());
    }

    @FXML
    private void completeTask() {
        switch (getSelectedTab()) {
            case myTasks:
                controller.completeTask(tableAllMy.getSelectionModel().getSelectedItem());
                break;
            case tasksNeedSigning:
                controller.signOffTask(tableRequireSigning.getSelectionModel().getSelectedItem());
                tableRequireSigning.getItems().remove(tableRequireSigning.getSelectionModel().getSelectedIndex());
                break;
        }
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
        switch (getSelectedTab()) {
            case myTasks:
                controller.addComment(newComment, tableAllMy.getSelectionModel().getSelectedItem());
                break;
            case tasksNeedSigning:
                controller.addComment(newComment, tableRequireSigning.getSelectionModel().getSelectedItem());
                break;
        }
        tableComments.getItems().add(newComment);
        commentField.setText("");
        fillComments();
    }

    @FXML
    private void goToMenu() {
        controller.goToMenu();
    }

    public void setMyTasks(List<Task> myTasks) {
        if(myTasks != null)
            tableAllMy.setItems(FXCollections.observableArrayList(myTasks));
    }

    public void setSigningTasks(List<Task> notSignedTasks) {
        if(notSignedTasks != null)
            tableRequireSigning.setItems(FXCollections.observableArrayList(notSignedTasks));
    }

    private void displayComments(Task task) {
        if(task != null) {
            List<Comment> comments = controller.getComments(task);
            if(comments != null) {
                tableComments.setItems(FXCollections.observableArrayList(comments));
                fillComments();
            }
        }
    }

    private void fillComments() {
        commentName.setCellValueFactory(data -> data.getValue().getNameProperty());
        commentString.setCellValueFactory(data -> data.getValue().getCommentProperty());
    }

    private void displayTaskDetails(Task task) {
        if(task == null) {
            name.setText("");
            description.setText("");
            priority.setText("");
            duration.setText("");
            type.setText("");
            locationMyTasks.setText("");
        }
        else {
            name.setText(task.getName());
            description.setText(task.getDescription());
            priority.setText(task.getPriorityString());
            duration.setText(task.getDurationString());
            type.setText(task.getTypeString());
            locationMyTasks.setText(task.getLocation());
        }
    }

    @Override
    public void show() {

    }

    private selectedTab getSelectedTab() {
        if(tabPane.getSelectionModel().getSelectedItem() == myTasks)
            return selectedTab.myTasks;
        else if (tabPane.getSelectionModel().getSelectedItem() == tasksNeedingSigning)
            return selectedTab.tasksNeedSigning;
        else
            return null;
    }

    private enum selectedTab {
        myTasks, tasksNeedSigning
    }
}

