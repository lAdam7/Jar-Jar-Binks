package JarJarBinks;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControl extends Controller {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void handleLogin() {
        if(!controller.userLogin(username.getText(), password.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid credentials");
            alert.setContentText("Username or password is incorrect");
            alert.showAndWait();
            clearPassword();
        }
        else {
            System.out.println("Logged in!");
        }
    }

    private void clearPassword() {
        password.setText("");
    }

    public void show() {

    }
}
