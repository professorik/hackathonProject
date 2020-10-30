package VideoAnalyser;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Main extends Application {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    static Image result;
    static ArrayList<Rect> rects;

    private static int blurPwr = 10;
//test
    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane root = new AnchorPane();
        ImageView imageView = new ImageView();
        imageView.setImage(result);
        root.getChildren().add(imageView);
        /*for (Rect i: rects) {
            Rectangle rectangle = new Rectangle(i.x, i.y, 1, i.height);
            Rectangle rectangle2 = new Rectangle(i.x + i.width, i.y, 1, i.height);
            Rectangle rectangle3 = new Rectangle(i.x, i.y, i.width, 1);
            Rectangle rectangle4 = new Rectangle(i.x, i.y + i.height, i.width, 1);
            root.getChildren().addAll(rectangle,rectangle2,rectangle3,rectangle4);
        }*/
        primaryStage.setTitle("Broccoli detector");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        rects = new ArrayList<>();
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());

        CascadeClassifier broccoliDetector = new CascadeClassifier(Main.class.getResource("cascade.xml").getPath().substring(1));
        //Mat image = imageToMat(new Image("/sample/12112.jpg"));
        Mat image = Imgcodecs.imread(Main.class.getResource("test.jpg").getPath().substring(1));
        MatOfRect detector = new MatOfRect();
        //Result list
        broccoliDetector.detectMultiScale(image, detector);

        for (Rect rect : detector.toArray()) {
            rects.add(rect);
            System.out.println(rect.x + " " + rect.y);
            Imgproc.rectangle(image, new Point(rect.x-1, rect.y-1), new Point(rect.x+rect.width, rect.y+ rect.height), new Scalar(0,0,255));
            Imgproc.GaussianBlur(image.submat(rect), image.submat(rect), new Size(55, 55), blurPwr);
        }

        result = matToImage(image);
        System.out.println("Succesfull");
        launch(args);
    }
    //https://www.kaggle.com/jessicali9530/caltech256?

    private MatOfRect detectInImage(CascadeClassifier detector, Image image) {
        MatOfRect items = new MatOfRect();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        detector.detectMultiScale(mat, items, 1.1, 3, 0, new Size(25, 25), new Size());
        return items;
    }

    private static Mat imageToMat(Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }

    public static Image matToImage(Mat input) throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", input, mob);
        byte ba[]=mob.toArray();
        BufferedImage bi= ImageIO.read(new ByteArrayInputStream(ba));
        return SwingFXUtils.toFXImage(bi, null);
    }
}
