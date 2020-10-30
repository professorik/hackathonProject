package VideoAnalyser;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayInputStream;

public class DataProcessor {
    protected static CascadeClassifier
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

    protected static Image mat2Image(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", mat, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }


}
