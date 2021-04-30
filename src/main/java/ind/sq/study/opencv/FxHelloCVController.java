package ind.sq.study.opencv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FxHelloCVController {
    @FXML
    private Button button;

    @FXML
    private ImageView currentFrame;
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;
    private static int cameraId = 0;

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    protected void init() {
        this.capture = new VideoCapture();
        this.cameraActive = false;
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        currentFrame.setFitHeight(500);
    }
    @FXML
    protected void startCamera(ActionEvent event) {

        if (!cameraActive) {
            this.haarClassifier.setDisable(true);
            this.haarClassifier.setDisable(true);

            this.capture.open(cameraId);

            if (this.capture.isOpened()) {
                this.cameraActive = true;
                var frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        Mat frame = grabFrame();
                        var image = Utils.mat2Image(frame);
                        updateImageView(currentFrame, image);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                this.button.setText("Stop Camera");
            } else {
                System.out.println("Can't open the camera");
            }
        } else {
            this.cameraActive = false;
            this.button.setText("Start Camera");
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);
            this.stopAcquisition();
        }
    }
    @FXML
    public void haarSelected() {
        System.out.println("haarSelected");
    }

    @FXML
    public void lbpSelected() {
        System.out.println("lbpSelected");
    }


    private Mat grabFrame() {
        Mat frame = new Mat();
        if (this.capture.isOpened()) {
            try {
                this.capture.read(frame);

                if (!frame.empty()) {
                    this.detectAndDisplay(frame);
                }
            } catch (Exception ex) {
                System.out.println("Exception during the image elaboration: " + ex);
            }
        }

        return frame;
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

        Imgproc.equalizeHist(frame, frame);


    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                System.out.println("Exception in stopping the frame capture, trying to release the camera now..." + ex);
            }
        }

        if (this.capture.isOpened()) {
            this.capture.release();
        }
    }

    protected void setClosed() {
        this.stopAcquisition();
    }
}
