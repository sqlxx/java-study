package ind.sq.study.opencv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FxHelloCVController {
    @FXML
    private Button startCameraButton;
    @FXML
    private Button chooseImgButton;
    @FXML
    private ImageView currentFrame;
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;
    @FXML
    private CheckBox cannyEdgeDetector;
    @FXML
    private Slider edThreshold;
    @FXML
    private CheckBox dilateErode;
    @FXML
    private CheckBox inverse;
    @FXML
    private CheckBox absRemoval;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;
    private static int cameraId = 0;

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private Mat oldFrame;

    Point clickedPoint = new Point(0, 0);

    protected void init() {
        this.capture = new VideoCapture();
        this.cameraActive = false;
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        currentFrame.setFitWidth(1280);
        currentFrame.setPreserveRatio(true);
    }
    @FXML
    protected void startCamera(ActionEvent event) {
        currentFrame.setOnMouseClicked(e -> {
            clickedPoint.x = e.getX();
            clickedPoint.y = e.getY();
        });

        if (!cameraActive) {
            this.haarClassifier.setDisable(true);
            this.lbpClassifier.setDisable(true);

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

                this.startCameraButton.setText("Stop Camera");
            } else {
                System.out.println("Can't open the camera");
            }
        } else {
            this.cameraActive = false;
            this.startCameraButton.setText("Start Camera");
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);
            this.stopAcquisition();
        }
    }

    @FXML
    public void chooseImg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.showOpenDialog(this.chooseImgButton.g);
    }
    @FXML
    public void haarSelected() {
        this.lbpClassifier.setSelected(false);
        this.cannyEdgeDetector.setSelected(false);
        this.edThreshold.setDisable(true);

        this.checkBoxSelection("target/classes/haarcascades/haarcascade_frontalface_alt.xml");
    }

    private void checkBoxSelection(String loc) {
        System.out.println(this.faceCascade.load(loc));
    }

    @FXML
    public void lbpSelected() {
        this.haarClassifier.setSelected(false);
        this.cannyEdgeDetector.setSelected(false);
        this.edThreshold.setDisable(true);


        this.checkBoxSelection("target/classes/lbpcascades/lbpcascade_frontalface_improved.xml");
    }

    @FXML
    public void cannySelected() {
        this.haarClassifier.setSelected(false);
        this.lbpClassifier.setSelected(false);

        this.edThreshold.setDisable(false);
    }

    @FXML
    public void dilateErodeSelected() {
        if (dilateErode.isSelected()) {
            inverse.setDisable(false);
        } else {
            inverse.setSelected(false);
            inverse.setDisable(true);
        }
    }


    private Mat grabFrame() {
        Mat frame = new Mat();
        if (this.capture.isOpened()) {
            try {
                this.capture.read(frame);

                if (!frame.empty()) {
                    if(this.haarClassifier.isSelected() || this.lbpClassifier.isSelected()) {
                        this.detectAndDisplay(frame);
                    }

                    if (this.cannyEdgeDetector.isSelected()) {
                        frame = this.doCanny(frame);
                    } else if (this.dilateErode.isSelected()) {
                        if (this.absRemoval.isSelected()) {
                            frame = this.doBackgroundRemovalFloodFill(frame);
                        } else {
                            frame = this.doBackgroundRemoval(frame);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Exception during the image elaboration: " + ex);
            }
        }

        return frame;
    }

    private Mat doBackgroundRemoval(Mat frame) {
        var hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();

        int thresh_type = Imgproc.THRESH_BINARY_INV;
        if (this.inverse.isSelected()) {
            thresh_type = Imgproc.THRESH_BINARY;
        }

        // hsvImg.create(frame.size(), CvType.CV_8U);
        Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
        Core.split(hsvImg, hsvPlanes);

        double threshValue = this.getHistAverage(hsvImg, hsvPlanes.get(0));
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, thresh_type);

        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));

        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);

        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        frame.copyTo(foreground, thresholdImg);

        return foreground;
    }

    private Mat doBackgroundRemovalFloodFill(Mat frame) {
        Scalar newVal = new Scalar(255, 255, 255);
        Scalar loDiff = new Scalar(50, 50, 50);
        Scalar upDiff = new Scalar(50, 50, 50);

        Point seedPoint = clickedPoint;
        Mat mask = new Mat();
        Rect rect = new Rect();

        Imgproc.floodFill(frame, mask, seedPoint, newVal, rect, loDiff, upDiff, Imgproc.FLOODFILL_FIXED_RANGE);
        return frame;

    }

    private double getHistAverage(Mat hsvImg, Mat hueValues) {
        double average = 0.0;
        Mat hist_hue = new Mat();
        MatOfInt histSize = new MatOfInt(180);
        var hue = new ArrayList<Mat>();
        hue.add(hueValues);

        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

        for (int h = 0; h < 180; h++) {
            average += (hist_hue.get(h, 0)[0] * h);
        }

        return  average / hsvImg.size().height / hsvImg.size().width;
    }

    private Mat doCanny(Mat frame) {
        Mat grayFrame = new Mat();
        Mat detectEdges = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(grayFrame, detectEdges, new Size(3, 3));
        Imgproc.Canny(detectEdges, detectEdges, this.edThreshold.getValue(), this.edThreshold.getValue()*3);
        var result = new Mat();
        frame.copyTo(result, detectEdges);

        return result;
    }

    private Mat doBackgroundRemovalAbsDiff(Mat currentFrame) {
        Mat greyImage = new Mat();
        Mat foregroundImg = new Mat();

        if (oldFrame == null) {
            oldFrame = currentFrame;
        }

        Core.absdiff(currentFrame, oldFrame, foregroundImg);
        Imgproc.cvtColor(foregroundImg, greyImage, Imgproc.COLOR_BGR2GRAY);

        int thresh_type = Imgproc.THRESH_BINARY_INV;
        if (this.inverse.isSelected()) {
            thresh_type = Imgproc.THRESH_BINARY;
        }
        Imgproc.threshold(greyImage, greyImage, 10, 255, thresh_type);
        currentFrame.copyTo(foregroundImg, greyImage);

        oldFrame = currentFrame;
        return foregroundImg;

    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        Imgproc.equalizeHist(grayFrame, grayFrame);

        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i ++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
        }
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
