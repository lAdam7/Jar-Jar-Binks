package JarJarBinks;

import javafx.stage.Stage;

public abstract class DialogController extends Controller{
    protected Stage dialogStage;
    protected Boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public Boolean isOkClicked() {
        return okClicked;
    }
}
