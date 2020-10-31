package processing;

import TextAnalyser.TextProcessor;
import VideoAnalyser.DataProcessor;
import VideoAnalyser.Main;
import VideoAnalyser.VideoProcessing;

import java.io.File;
import java.io.IOException;

public class MainProcessor {

    private Main imageProcessor;
    private VideoProcessing videoProcessing;
    private TextProcessor textProcessor;

    public MainProcessor() {
        imageProcessor = new Main();
        videoProcessing = new VideoProcessing();
        textProcessor = new TextProcessor();
    }

    public void process(File file) throws IOException {
        String fileName = file.toString();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            String extension = fileName.substring(index + 1);
            switch (extension.toLowerCase()) {
                case "txt":
                    textProcessor.processText(file);
                    break;
                case "mp4":
                case "avi":
                    videoProcessing.convert(file);
                    break;
                case "png":
                case "jpg":
                    imageProcessor.processImage(file);
                    break;
                default:
                    System.err.println("Just filetype error");
            }
            System.out.println("File extension is " + extension);
        } else {
            System.err.println("File type isn't detected");
        }
    }

}
//D:\IdeaProjects\hackathonProject\src\resources\video.mp4
//D:\IdeaProjects\hackathonProject\src\resources\video.mp4