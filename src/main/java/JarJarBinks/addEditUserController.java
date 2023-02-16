package JarJarBinks;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collections;

public class addEditUserController extends DialogController {
    @FXML
    private TextField username;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private ChoiceBox<UserLevel> userType;

    private Boolean userTypedUsername;

    private User editedUser;

    @FXML
    private void initialize() {
        ObservableList<UserLevel> list = userType.getItems();
        Collections.addAll(list, UserLevel.values());

        userTypedUsername = false;

        firstName.textProperty().addListener( (observableValue, oldVal, newVal) -> {
            if(!userTypedUsername)
                username.setText(generateUsername());
        });
        lastName.textProperty().addListener( (observableValue, oldVal, newVal) -> {
            if(!userTypedUsername)
                username.setText(generateUsername());
        });
        username.textProperty().addListener( (observableValue, oldVal, newVal) -> {
            if(newVal != null && !newVal.equals(generateUsername()))
                userTypedUsername = true;
        });
    }

    public void setEditedUser(User userToEdit) {
        this.editedUser = userToEdit;

        username.setText(editedUser.getUsername());
        firstName.setText(editedUser.getName());
        lastName.setText(editedUser.getSurname());
        userType.getSelectionModel().select(editedUser.getType());
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        if(verifyData()) {
            if(editedUser.getUsername() == null) {
                if(controller.checkUsernameExists(username.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Username already in use");
                    alert.setContentText("Username already exists. Please choose different one");
                    alert.showAndWait();
                }
                else {
                    okClicked = true;
                    dialogStage.close();
                }
            }
            else {
                okClicked = true;
                dialogStage.close();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid data");
            alert.setHeaderText("Invalid data entered");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    private boolean verifyData() {
        if(username.getText() != null && username.getText().length() > 1
        && firstName.getText() != null && firstName.getText().length() > 1
        && lastName.getText() != null && lastName.getText().length() > 1
        && userType.getValue() != null
        )
            return true;
        return false;
    }

    public void setUsernameReadOnly() {
        username.setEditable(false);
    }
    public void DisableUsernameChanging() {
        userTypedUsername = true;
    }

    public String getNewUsername() {
        return username.getText();
    }
    public String getNewFirstName() {
        return firstName.getText();
    }
    public String getNewLastName() {
        return lastName.getText();
    }
    public UserLevel getNewUserLevel() {
        return userType.getValue();
    }

    private String generateUsername() {
        String usrn = "";
        if(firstName.getText() != null && firstName.getText().length() > 0)
            usrn += String.valueOf(Character.toLowerCase(firstName.getText().charAt(0)));
        if(lastName.getText() != null && lastName.getText().length() > 0)
            usrn += lastName.getText().replace(" ", "");
        return usrn;
    }

    @Override
    public void show() {}
}
