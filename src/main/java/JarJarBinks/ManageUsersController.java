package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class ManageUsersController extends Controller {

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> fNameColumn;
    @FXML
    private TableColumn<User, String> lNameColumn;

    @FXML
    private Label username;
    @FXML
    private Label name;
    @FXML
    private Label surname;
    @FXML
    private Label position;


    @FXML
    private void initialize() {
        fNameColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
        lNameColumn.setCellValueFactory(data -> data.getValue().getSurnameProperty());

        showUserDetails(null);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showUserDetails(newVal)
        );
    }

    @FXML
    private void HandleDeleteUser() {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Delete confirmation");
        int targetIndex = table.getSelectionModel().getSelectedIndex();
        User target = table.getItems().get(targetIndex);
        conf.setContentText("Are you sure you want to delete " + target.getName() + " " + target.getSurname() + " (" + target.getUsername() + ")?");
        Optional<ButtonType> result = conf.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.DeleteUser(target);
            table.getItems().remove(targetIndex);
        }
    }

    @FXML
    private void HandleAddUser() {
        controller.AddUser();
    }

    @FXML
    private void HandleEditUser() {
        User target = table.getSelectionModel().getSelectedItem();
        if (target == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No user selected");
            alert.setContentText("Please select a user to edit");
            alert.showAndWait();
        }
        else {
            controller.EditUser(target);
            showUserDetails(target);
        }
    }

    @FXML
    private void HandlePassReset() {
        User target = table.getSelectionModel().getSelectedItem();
        controller.ResetPassword(target);
    }

    public void setData(List<User> list) {
        table.setItems(FXCollections.observableArrayList(list));
    }

    public void addUserToList(User user) {
        table.getItems().add(user);
    }

    private void showUserDetails(User user) {
        if(user != null) {
            username.setText(user.getUsername());
            name.setText(user.getName());
            surname.setText(user.getSurname());
            position.setText(user.getTypeString());
        }
        else {
            username.setText("");
            name.setText("");
            surname.setText("");
            position.setText("");
        }
    }

    @Override
    public void show() {

    }
}
