package ind.sq.study.opencv;

import org.opencv.objdetect.CascadeClassifier;

public class DetectFaceDemo {
    public void run() {
        System.out.println("Running Detect Face Demo");

        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("lena.png").getPath());
        System.out.println("Running Detect Face Demo");
    }
}
