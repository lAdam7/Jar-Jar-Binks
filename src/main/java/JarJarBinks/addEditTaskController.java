package JarJarBinks;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class addEditTaskController extends DialogController {

    @FXML
    private TextField tName;

    @FXML
    private TextField tDescription;

    @FXML
    private TextField tDuration;

    @FXML
    private TextField tFrequency;

    @FXML
    private ChoiceBox<taskType> tType;

    @FXML
    private ChoiceBox<CategoryType> tCategory;

    @FXML
    private ChoiceBox<taskPriority> tPriority;

    @FXML
    private TextField tDeadline;

    @FXML
    private CheckBox tRequiresSigning;

    @FXML
    private ChoiceBox<UserLevel> tSigningBy;

    @FXML
    private TextField tLocation;

    @FXML
    private Label addOrEditTitle;

    private Task editedTask;



    @FXML
    private void initialize() {
        ObservableList<taskType> typeList = tType.getItems();
        typeList.add(0, taskType.OneOff);
        typeList.add(1, taskType.Regular);

        ObservableList<CategoryType> categoryList = tCategory.getItems();
        categoryList.add(0, CategoryType.Fix);
        categoryList.add(1, CategoryType.Clean);
        categoryList.add(2, CategoryType.Paint);
        categoryList.add(3, CategoryType.Tidy);
        categoryList.add(4, CategoryType.Inspect);

        ObservableList<taskPriority> priorityList = tPriority.getItems();
        priorityList.add(0, taskPriority.Low);
        priorityList.add(1, taskPriority.Medium);
        priorityList.add(2, taskPriority.High);

        ObservableList<UserLevel> signByList = tSigningBy.getItems();
        signByList.add(0, UserLevel.Caretaker);
        signByList.add(1, UserLevel.Admin);

    }


    @FXML
    private void handleConfirm() {
        if(verify() && verifySigningBy() && verifyTaskTypeRegular() && verifyTaskTypeOneOff()){
                okClicked = true;
                dialogStage.close();
            }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid data");
            alert.setHeaderText("Invalid data entered, make sure you fill all windows and stick to the boundaries");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    private boolean verify() {
        if(tName.getText() != null && tName.getText().length() > 1
                && tType.getValue() != null
                && tCategory.getValue() != null
                && tDescription.getText() != null && tDescription.getText().length() > 1
                && Integer.parseInt(tDuration.getText()) > 5 && Integer.parseInt(tDuration.getText()) < 200
                && tPriority.getValue() != null
                && Integer.parseInt(tDeadline.getText()) >= 0 && Integer.parseInt(tDeadline.getText()) < 30
                && tLocation.getText() != null && tLocation.getText().length() > 1
                && Integer.parseInt(tFrequency.getText()) >=0)
        return true;
        return false;
    }
    private boolean verifySigningBy(){
        if (tRequiresSigning.isSelected()){
            if (tSigningBy.getValue() != null)
                return true;
        }
        return true;
    }
    private boolean verifyTaskTypeRegular(){
        if(tType.getValue().equals(taskType.Regular)){
            return Integer.parseInt(tFrequency.getText()) > 0;
        }
        return true;
    }

    private boolean verifyTaskTypeOneOff(){
        if (tType.getValue().equals(taskType.OneOff)){
            return Integer.parseInt(tFrequency.getText()) == 0;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    void setTitle(String title){
        addOrEditTitle.setText(title);
    }

    //need fixes
    /*void setFrequencyHidden(){
        tFrequency.setVisible(tType.getValue() != taskType.Regular);
    }*/

    void setSigningByDisabled(){
        tSigningBy.disableProperty().bind(tRequiresSigning.selectedProperty().not());
    }

    public void setTaskToEdit(Task taskToEdit) {
        this.editedTask = taskToEdit;

        tName.setText(editedTask.getName());
        tDescription.setText(editedTask.getDescription());
        tDuration.setText(String.valueOf(editedTask.getDuration()));
        tFrequency.setText(String.valueOf(editedTask.getFrequency()));
        tType.getSelectionModel().select(editedTask.getType());
        tCategory.getSelectionModel().select(editedTask.getCategory());
        tPriority.getSelectionModel().select(editedTask.getPriority());
        tDeadline.setText(String.valueOf(editedTask.getDaysToFinish()));
        if(tRequiresSigning.isSelected()){tSigningBy.getSelectionModel().select(editedTask.getRequireSigningBy());}
        tLocation.setText(editedTask.getLocation());
    }


    public String getNewTaskName() {
        return tName.getText();
    }
    public String getNewDescription() {
        return tDescription.getText();
    }
    public Integer getNewDuration() {
        return Integer.parseInt(tDuration.getText());
    }
    public Integer getNewFrequency() {return Integer.parseInt(tFrequency.getText());}
    public taskType getNewTaskType() {
        return tType.getValue();
    }
    public CategoryType getNewTaskCategory() {
        return tCategory.getValue();
    }
    public taskPriority getNewTaskPriority() {
        return tPriority.getValue();
    }
    public Integer getNewDeadline() {
        return Integer.parseInt(tDeadline.getText());
    }
    public Boolean getNewRequiresSigning() {
        return tRequiresSigning.isSelected();
    }
    public UserLevel getNewTaskSigningBy() {
        return tSigningBy.getValue();
    }
    public String getNewTaskLocation() {
        return tLocation.getText();
    }


    @Override
    public void show() {

    }
}
