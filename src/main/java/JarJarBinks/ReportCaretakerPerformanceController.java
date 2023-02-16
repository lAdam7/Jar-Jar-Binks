package JarJarBinks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportCaretakerPerformanceController extends Controller {


    @FXML
    private TableView<CaretakerAssignedTasks> reportTable2;
    @FXML
    private TableView<Task> reportTaskTable;
    @FXML
    private TableColumn<CaretakerAssignedTasks, String> tNameColumn;
    @FXML
    private TableColumn<CaretakerAssignedTasks, String> tSurnameColumn;
    @FXML
    private TableColumn<Task, String> taskName;
    @FXML
    private TableColumn<Task, String> taskCategory;
    @FXML
    private TableColumn<Task, String> taskDuration;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadline;
    @FXML
    private TableColumn<Task, LocalDate> taskCompleted;



    @FXML
    private void initialize(){
        tNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().getNameProperty());
        tSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().getSurnameProperty());

        showDetails(null);

        reportTable2.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldVal, newVal) -> showDetails(newVal.getTask())
        );
    }


    private void showDetails(ArrayList<Task> tasks ){
        if (tasks != null){
            taskName.setCellValueFactory(data -> data.getValue().getNameProperty());
            taskCategory.setCellValueFactory(data -> data.getValue().getCategoryProperty().asString());
            taskDuration.setCellValueFactory(data -> data.getValue().getDurationStringProperty());
            taskDeadline.setCellValueFactory(data -> data.getValue().getDeadlineProperty());
            taskCompleted.setCellValueFactory(data -> data.getValue().getDateCompletedProperty());
            reportTaskTable.setItems(FXCollections.observableArrayList(tasks));
        }
        else {
            taskName.setText("");
            taskCategory.setText("");
            taskDuration.setText("");
            taskDeadline.setText("");
            taskCompleted.setText("");
        }
    }



    public void setCaretakerPerformanceReport(List<CaretakerAssignedTasks> caretakerPerf) {
        if(caretakerPerf != null)
            reportTable2.setItems(FXCollections.observableArrayList(caretakerPerf));
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
                saveFile(reportTaskTable.getItems(), file);
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
            ioAlert.setContentText("Sorry. An eror occured.");
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
