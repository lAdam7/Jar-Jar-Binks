package JarJarBinks;

import javafx.fxml.FXML;

public abstract class Controller {
    protected MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public abstract void show();

    @FXML
    private void goToMenu() {
        controller.goToMenu();
    }

    @FXML
    private void showReportMenu(){
        controller.showReportMenu();
    }
}
