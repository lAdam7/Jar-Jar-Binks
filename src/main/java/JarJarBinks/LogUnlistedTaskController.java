package JarJarBinks;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.Collections;
import java.util.List;

public class LogUnlistedTaskController extends Controller {

    @FXML
    private TextField taskName;
    @FXML
    private TextField description;
    @FXML
    private TextField length;
    @FXML
    private ChoiceBox<CategoryType> category;
    @FXML
    private TextField taskLocation;

    @FXML
    private void initialize() {
        ObservableList<CategoryType> list = category.getItems();
        Collections.addAll(list, CategoryType.values());
    }

    @FXML
    private void logTask() {
        if(checkForm()) {
            Task task = new Task();
            task.setDescription(description.getText());
            task.setName(taskName.getText());
            task.setDuration(Integer.parseInt(length.getText()));
            task.setPriority(taskPriority.Low);
            task.setCategory(category.getValue());
            task.setLocation(taskLocation.getText());

            controller.addUnlistedTask(task);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task logged");
            alert.setContentText("Task has been successfully logged");
            alert.showAndWait();

            cleanForm();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form not filled correctly");
            alert.setContentText("Please fill in all of the fields. If that is the case, then check that duration is a number > 0 and you have selected a category");
            alert.showAndWait();
        }
    }

    private boolean checkForm() {
        try {
            if (taskName.getText() != null && !taskName.getText().equals("") &&
                    description.getText() != null && !description.getText().equals("") &&
                    length.getText() != null && !length.getText().equals("") &&
                    taskLocation.getText() != null && !taskLocation.getText().equals("") &&
                    category.getValue() != null) {
                int i = Integer.parseInt(length.getText());
                return i > 0;
            }
            return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private void cleanForm() {
        taskName.setText("");
        taskLocation.setText("");
        length.setText("");
        category.getSelectionModel().select(null);
        description.setText("");
    }

    @Override
    public void show() {

    }
}
