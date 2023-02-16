package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReportCompletionHistoryController extends Controller{

    @FXML
    private TableView<Task> reportTable3;
    @FXML
    private TableColumn<Task, String> taskNameColumn;
    @FXML
    private TableColumn<Task, String> taskTypeColumn;
    @FXML
    private TableColumn<Task, String> taskCategoryColumn;
    @FXML
    private TableColumn<Task, String> caretakerColumn;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadline;
    @FXML
    private TableColumn<Task, LocalDate> taskCompletedDate;



    @FXML
    private void initialize() {
        taskNameColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
        taskTypeColumn.setCellValueFactory(data -> data.getValue().getTypeProperty().asString());
        taskCategoryColumn.setCellValueFactory(data -> data.getValue().getCategoryProperty().asString());
        caretakerColumn.setCellValueFactory(data -> data.getValue().getCaretakerProperty());
        taskDeadline.setCellValueFactory(data -> data.getValue().getDeadlineProperty());
        taskCompletedDate.setCellValueFactory(data -> data.getValue().getAssignedDateProperty());
    }

    public void setCompletionHistoryReport(List<Task> completionHistory) {
        if(completionHistory != null)
            reportTable3.setItems(FXCollections.observableArrayList(completionHistory));
    }

    ObservableList<Task> observableReportList = FXCollections.observableArrayList();

    public void handleSave(ActionEvent event) {
        Stage secondaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Current Tasks Report");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        /*if(observableReportList.isEmpty()){
           // secondaryStage.initOwner(this.fileMenu.getScene().getWindow());
        }*/
        Alert emptyTableAlert = new Alert(Alert.AlertType.ERROR, "Empty Table", ButtonType.OK);
        emptyTableAlert.setContentText("There is nothing to save");
        emptyTableAlert.initModality(Modality.APPLICATION_MODAL);
        //emptyTableAlert.initOwner(this.fileMenu.getScene().getWindow());
        emptyTableAlert.showAndWait();
        if (emptyTableAlert.getResult() == ButtonType.OK){
            emptyTableAlert.close();
        }
        else{
            File file = fileChooser.showSaveDialog(secondaryStage);
            if (file != null) {
                saveFile(reportTable3.getItems(), file);
            }   }
    }

    private void saveFile(ObservableList<Task> items, File file) {
        try {
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

            for (Task tasks: observableReportList) {
                outWriter.write(tasks.toString());
                outWriter.newLine();
            }
            System.out.println(observableReportList.toString());
            outWriter.close();
        }catch (IOException e){
            Alert ioAlert = new Alert(Alert.AlertType.ERROR, "Error",ButtonType.OK);
            ioAlert.setContentText("Sorry. An error occured.");
            ioAlert.showAndWait();
            if (ioAlert.getResult() == ButtonType.OK){
                ioAlert.close();
            }
        }
    }

    @Override
    public void show() {

    }
}
