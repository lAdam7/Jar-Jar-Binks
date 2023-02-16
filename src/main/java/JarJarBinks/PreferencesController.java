package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class PreferencesController extends Controller {
    @Override
    public void show() {

    }

    @FXML
    private void goToMenu() {
        controller.goToMenu();
    }


    @FXML
    private TableView<Category> table;

    @FXML
    private TableColumn<Category, String> CategoryName;

    @FXML
    private TableView<Task> catTasks;

    @FXML
    private TableColumn<Task, String> taskName;

    @FXML
    private TableColumn<Task, String> taskType;

    @FXML
    private TableColumn<Task, String> taskDesc;

    @FXML
    private Button btnSubmit;

    @FXML
    private void initialize() {
        CategoryName.setCellValueFactory(data -> data.getValue().getCategoryTypeProperty().asString());


        showCategoryDetails(null);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showCategoryDetails(newVal)
        );
    }

    private void updateButtonText() {
        int targetIndex = table.getSelectionModel().getSelectedIndex();
        Category target = table.getItems().get(targetIndex);
        if (target.getEnabled()) {
            btnSubmit.setText("Unselect");
        } else {
            btnSubmit.setText("Select");
        }
    }

    @FXML
    private void storeCategory() {
        int targetIndex = table.getSelectionModel().getSelectedIndex();
        Category target = table.getItems().get(targetIndex);

        if (target.getEnabled()) {
            Boolean removePref = controller.removePreference(target);
            if (removePref) {
                target.setEnabled(false);
            }
            updateButtonText();
        } else {
            Boolean insertPref = controller.insertPreference(target);
            if (insertPref) {
                target.setEnabled(true);
            }
            updateButtonText();
        }
    }

    public void setData(List<Category> list) {
        table.setItems(FXCollections.observableArrayList(list));
    }


    private void showCategoryDetails(Category category) {
        if (category != null) {
            taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
            taskType.setCellValueFactory(data -> data.getValue().getTypeProperty().asString());
            taskDesc.setCellValueFactory(data -> data.getValue().getDescriptionProperty());
            catTasks.setItems(FXCollections.observableArrayList(category.getTask()));
            updateButtonText();
        }
        else {
            btnSubmit.setText("Loading...");
        }
    }
}






