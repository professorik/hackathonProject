package UI;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import processing.MainProcessor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SortImage implements Initializable {

    @FXML
    private VBox anchor;

    @FXML
    private MenuItem uploadFiles;

    @FXML
    private MenuItem addFiles;

    @FXML
    private MenuItem run;

    @FXML
    private MenuItem processingSettings;

    @FXML
    private MenuItem viewSettings;

    public static ArrayList<File> inputFiles = new ArrayList<>();

    private int currentElem = 0;

    private static double delay = 1500;

    private final FileChooser fileChooser = new FileChooser();
    //final ProgressBar progressBar = new ProgressBar(0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.setTitle("Select Files");
        fileChooser.setInitialDirectory(new File("C:/Users"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All", "*.jpg", "*.png", "*.txt", "*.mp4", "*.avi"));

        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/UI/filesUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //node.setPickOnBounds(true);
        //anchor.getChildren().add(progressBar);
        anchor.getChildren().add(node);
       // node.prefHeight(400);
        //Menu analyzing
        uploadFiles.setOnAction(event -> FilesUIController.uploadFilesAndUpdate(false));
        addFiles.setOnAction(event -> FilesUIController.uploadFilesAndUpdate(true));
        run.setOnAction(event -> startPlay());
        viewSettings.setOnAction(event -> setParam());
    }

    private void setParam(){
        Stage dialog = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(10 ,10 ,10 ,10));
        javafx.scene.control.TextField temp = new TextField();
        temp.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            try {
                Integer.valueOf(temp.getText() + keyEvent.getCharacter());
            }catch (Exception e){
                keyEvent.consume();
            }
        });
        Button okBtn = new Button("OK");
        okBtn.setOnAction(event -> {
            try {
                if (Integer.valueOf(temp.getText()) > 0){
                    delay = Integer.valueOf(temp.getText());
                    dialog.close();
                }
            }catch (Exception e){
            }
        });

        okBtn.setPrefWidth(200);
        root.getChildren().addAll(temp, okBtn);
        dialog.initOwner(anchor.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root , 200 , 100));
        dialog.setTitle("Введите задержку!");
        dialog.showAndWait();
    }

    private void startPlay(){
        if (!inputFiles.isEmpty()) {
            MainProcessor mainProcessor = new MainProcessor();
            ProgressForm pForm = new ProgressForm();
            Task<Void> task = new Task<>() {
                @Override
                public Void call() {
                    for (int i = 0; i < inputFiles.size(); i++) {
                        updateProgress(i, inputFiles.size());
                        updateMessage(inputFiles.get(i).getName());
                        try {
                            mainProcessor.process(inputFiles.get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Thread.sleep(500);
                    }
                    updateProgress(inputFiles.size(), inputFiles.size());
                    return null;
                }
            };
            pForm.activateProgressBar(task);
            task.setOnSucceeded(event -> {
                pForm.getDialogStage().close();
            });
            pForm.getDialogStage().show();
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static class ProgressForm {
        private final Stage dialogStage;
        private final ProgressBar pb = new ProgressBar();
        private final Label label = new Label();

        public ProgressForm() {
            dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UNIFIED);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Wait, your files are processed...");
            dialogStage.setAlwaysOnTop(true);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            pb.setProgress(-1F);
            pb.setPrefHeight(30);
            pb.setPrefWidth(300);
            final HBox hb = new HBox();
            hb.setSpacing(5);
            hb.setAlignment(Pos.CENTER_LEFT);
            hb.getChildren().addAll(pb, label);
            Scene scene = new Scene(hb, 600, 50);
            dialogStage.setScene(scene);
            dialogStage.setOnCloseRequest(windowEvent -> windowEvent.consume());
        }

        public void activateProgressBar(final Task<?> task)  {
            pb.progressProperty().bind(task.progressProperty());
            label.textProperty().bind(task.messageProperty());
            dialogStage.show();
        }

        public Stage getDialogStage() {
            return dialogStage;
        }
    }
}