package JarJarBinks;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

public class ChangePasswordController extends Controller {

    @FXML
    private PasswordField currentPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField newPassConf;

    @FXML
    private void handleCancel() {
        controller.goToMenu();
    }

    @FXML
    private void handleConfirm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(checkForm()){
            if(!newPass.getText().equals(newPassConf.getText())) {
                alert.setTitle("Passwords don't match");
                alert.setContentText("New passwords don't match");
                alert.showAndWait();
                clearFields();
            }
            else {
                if(!controller.changePassword(currentPass.getText(), newPass.getText())) {
                    alert.setTitle("Wrong password");
                    alert.setContentText("Your current password did not match");
                    alert.showAndWait();
                    clearFields();
                }
                else {
                    Alert someAlert = new Alert(Alert.AlertType.INFORMATION);
                    someAlert.setTitle("Password successfully changed");
                    someAlert.setContentText("Your password has been changed");
                    someAlert.showAndWait();
                    controller.goToMenu();
                }
            }
        }
        else {
            alert.setTitle("Empty fields");
            alert.setContentText("One or more fields is empty or new password is shorter than 8 characters");
            alert.showAndWait();
            clearFields();
        }
    }

    private boolean checkForm() {
        if((currentPass.getText() == null || currentPass.getText().length() == 0) && (newPass.getText() == null || newPass.getText().length() < 8) && (newPassConf.getText() == null || newPassConf.getText().length() < 8)) {
            return false;
        }
        return true;
    }

    private void clearFields() {
        currentPass.setText("");
        newPass.setText("");
        newPassConf.setText("");
    }

    @Override
    public void show() {

    }
}
