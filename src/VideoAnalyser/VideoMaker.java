package VideoAnalyser;

import org.opencv.core.*;
import org.opencv.videoio.*;

public class VideoMaker {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        int fourcc = VideoWriter.fourcc('m','j','p','g');
        Mat frame = new Mat(480, 640, CvType.CV_8UC3, Scalar.all(127));
        VideoWriter videoWriter = new VideoWriter();
        videoWriter.open("test_video.avi", fourcc, 20, frame.size(), true);
        /*
        Mat frame = new Mat(480, 640, CvType.CV_8UC3, Scalar.all(127));
        int fourcc = VideoWriter.fourcc('m', 'j', 'p', 'g');
        VideoWriter writer = new VideoWriter("/test_video.avi", fourcc, 20, frame.size(), true);
        if (!writer.isOpened()) {
            System.out.println("bummer!");
            return;
        }
        for (int i = 0; i < 100; i++) {
            Mat f = frame.clone();
            Imgproc.putText(f, ("frame" + i), new Point(100, 100), Imgproc.FONT_HERSHEY_PLAIN, 2, new Scalar(200, 0, 0), 3);
            writer.write(f);
        }*/
    }
}
