package UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private MenuItem runAuto;
    @FXML
    private MenuItem param;

    public static ArrayList<File> inputFiles = new ArrayList<>();

    private int currentElem = 0;

    private static double delay = 1500;

    private final FileChooser fileChooser = new FileChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Подготовил FileChooser для загрузки файлов, установив фильры
        fileChooser.setTitle("Select Files");
        fileChooser.setInitialDirectory(new File("C:/Users"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Photo", "*.jpg", "*.png"));

        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/UI/filesUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //node.setPickOnBounds(true);
        anchor.getChildren().add(node);
       // node.prefHeight(400);
        //Menu analyzing
        uploadFiles.setOnAction(event -> FilesUIController.uploadFilesAndUpdate(false));
        addFiles.setOnAction(event -> FilesUIController.uploadFilesAndUpdate(true));
        run.setOnAction(event -> startPlay());
        runAuto.setOnAction(event -> startPlayWith());
        param.setOnAction(event -> setParam());
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

    //TODO : проверка , что все входящие файлы ТОЛЬКО фото
    private void startPlay(){
        if (!inputFiles.isEmpty()) {

        }
    }

    private void startPlayWith(){
        if (!inputFiles.isEmpty()) {

        }
    }
}
