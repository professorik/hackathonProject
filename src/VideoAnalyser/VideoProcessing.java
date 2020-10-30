package VideoAnalyser;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.io.File;

public class VideoProcessing {

    private VideoCapture videoCapture;
    private VideoWriter videoWriter;

    private static int blurPwr = 10;

    private void initOpenCv(String filename) {
        videoCapture = new VideoCapture(filename);
        videoCapture.open(filename);
        Size frameSize = new Size((int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        videoWriter = new VideoWriter("result.avi", VideoWriter.fourcc('x', '2', '6', '4'), videoCapture.get(Videoio.CAP_PROP_FPS), frameSize, true);
        Writer.println("Video is open: " + videoCapture.isOpened(), 35);
    }

    public void convert(String filename){
        initOpenCv(filename);
        int len = (int)videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT), stage = 0;
        Mat mat = new Mat();
        while (videoCapture.isOpened()) {
            videoCapture.read(mat);
            Writer.printProcess(++stage, len, 33);
            MatOfRect matOfRect = new MatOfRect();
            DataProcessor.face_detector_def.detectMultiScale(mat, matOfRect);
            for (Rect rect : matOfRect.toArray()) {
                Imgproc.rectangle(mat, new Point(rect.x - 1, rect.y - 1), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
                Imgproc.GaussianBlur(mat.submat(rect), mat.submat(rect), new Size(55, 55), blurPwr);
            }
            if (stage < len){
                Write(mat);
            }else{
                videoCapture.release();
                videoWriter.release();
                Writer.println("\nVideo has been closed", 35);
            }
        }
    }

    private void Write(Mat frame) {
        if (videoWriter.isOpened() == false) {
            videoWriter.release();
            throw new IllegalArgumentException("Video Writer Exception: VideoWriter not opened, check parameters.");
        }
        videoWriter.write(frame);
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary("opencv_videoio_ffmpeg440_64");
        System.loadLibrary("openh264-1.8.0-win64");
        new VideoProcessing().convert("src/resources/video.mp4");
    }
}
