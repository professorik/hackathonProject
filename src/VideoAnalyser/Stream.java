package VideoAnalyser;

import java.util.List;
import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.opencv.core.*;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class Stream extends Application {

    private static final int SCENE_W = 1280;
    private static final int SCENE_H = 720;
    private static long countFrames = 0;
    private static long failed = 0;

    VideoCapture videoCapture;

    private CascadeClassifier
            face_detector = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_frontalface_alt.xml").getPath().substring(1)),
            face_detector_2 = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_frontalface_alt2.xml").getPath().substring(1)),
            face_detector_tree = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_frontalface_alt_tree.xml").getPath().substring(1)),
            face_detector_def = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_frontalface_default.xml").getPath().substring(1)),
            face_detector_profile = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_profileface.xml").getPath().substring(1)),
            upperbody_detector = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_upperbody.xml").getPath().substring(1)),
            head_detector = new CascadeClassifier(Main.class.getResource("/resources/HS.xml").getPath().substring(1)),
            mouth_detector = new CascadeClassifier(Main.class.getResource("/resources/Mouth.xml").getPath().substring(1)),
            nose_detector = new CascadeClassifier(Main.class.getResource("/resources/Nariz.xml").getPath().substring(1)),
            eyes_detector = new CascadeClassifier(Main.class.getResource("/resources/haarcascade_eye.xml").getPath().substring(1));

    Canvas canvas;
    GraphicsContext g2d;
    Stage stage;
    AnimationTimer timer;

    private static int blurPwr = 10;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        initOpenCv();

        canvas = new Canvas(SCENE_W, SCENE_H);
        g2d = canvas.getGraphicsContext2D();
        g2d.setStroke(Color.GREEN);

        Group group = new Group(canvas);

        Scene scene = new Scene(group, SCENE_W, SCENE_H);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        MatOfRect[] matOfRect = new MatOfRect[3];
        for (int i = 0; i < matOfRect.length; i++) {
            matOfRect[i] = new MatOfRect();
        }

        timer = new AnimationTimer() {
            Mat mat = new Mat();
            @Override
            public void handle(long now) {
                videoCapture.read(mat);
                ++countFrames;
                face_detector_def.detectMultiScale(mat, matOfRect[0]);
                head_detector.detectMultiScale(mat, matOfRect[1]);
                for (Rect rect : matOfRect[0].toArray()) {
                   Imgproc.rectangle(mat, new Point(rect.x-1, rect.y-1), new Point(rect.x+rect.width, rect.y+ rect.height), new Scalar(0,0,255));
                   Imgproc.GaussianBlur(mat.submat(rect), mat.submat(rect), new Size(55, 55), blurPwr);
                }
                for (Rect rect : matOfRect[1].toArray()) {
                    //Rect rect1 = new Rect(new Point(rect.x-50, rect.y-50), new Point(Math.min(640, rect.x+ rect.width*5), Math.min(rect.y+ rect.height*6, 480)));
                    Imgproc.rectangle(mat, new Point(rect.x-1, rect.y-1), new Point(rect.x+rect.width, rect.y+ rect.height), new Scalar(0,255,0));
                    Imgproc.GaussianBlur(mat.submat(rect), mat.submat(rect), new Size(55, 55), blurPwr);
                }
                if (matOfRect[0].toArray().length + matOfRect[1].toArray().length == 0) ++failed;
                Image image = DataProcessor.mat2Image(mat);
                g2d.drawImage(image, 0, 0);
            }
        };
        timer.start();
    }

    private void initOpenCv() {
        videoCapture = new VideoCapture();
        videoCapture.open(0);
        videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, SCENE_W);
        videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, SCENE_H);
        Writer.println("Camera open: " + videoCapture.isOpened(), 35);
        stage.setOnCloseRequest(we -> {
            timer.stop();
            videoCapture.release();
            Writer.println("Camera released", 34);
            Writer.println(String.valueOf(100.0*failed/countFrames), 33);
        });
    }

    private Optional<Rect> getResultRect(List<Rect> rectList){
        return Optional.empty();
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}