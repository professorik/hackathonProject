package UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FilesUIController implements Initializable {
    @FXML
    private ScrollPane root;

    private static GridPane gridPane;

    private static int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(30);
        //из-за этого не видно все
        gridPane.setTranslateX(10);
        gridPane.setTranslateY(10);
        root.setFitToHeight(true);
        root.setContent(gridPane);
    }


    public static void uploadFilesAndUpdate(boolean isAdding) {
        final int max_row = 8;
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");
        fileChooser.setInitialDirectory(new File("C:/"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Photo", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Video", "*.mp4", ".avi"),
                new FileChooser.ExtensionFilter("Text" , "*.txt"),
                new FileChooser.ExtensionFilter("All", "*.jpg", "*.png", "*.mp4", "*.avi", "*.txt"));

        List<File> files = fileChooser.showOpenMultipleDialog(gridPane.getScene().getWindow());
        if (files.size() > 0) {
            if (!isAdding) {
                gridPane.getChildren().clear();
                i = 0;
                SortImage.inputFiles.clear();
            }
            SortImage.inputFiles.addAll(files);

            for (File file : files) {
                int count = 90;
                VBox vBox = new VBox();
                String filePath = file.toURI().toString();

                try (InputStream input = new FileInputStream(file)) {
                    try {
                        ImageIO.read(input).toString();
                    } catch (Exception e) {
                        filePath = "/resources/unknown.png";
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Image image = new Image(filePath, (count * 4032) / 3024, count, true, true, true);
                ImageView iv = new ImageView();
                iv.setImage(image);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);
                iv.setCache(true);
                vBox.getChildren().addAll(iv, new Label(file.getName().length() <= 20 ? file.getName() : file.getName().substring(0, 18) + "..."));
                gridPane.add(vBox, i % max_row, i / max_row);
                ++i;
            }
            HBox hBox = new HBox();
            hBox.setPrefHeight(100);
            gridPane.add(hBox, (i + 3) % max_row, (i + 3) / max_row);
        }
    }
}
