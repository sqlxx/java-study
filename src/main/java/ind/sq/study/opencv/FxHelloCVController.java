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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.io.File;
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
    @FXML
    private Button refreshImgButton;

    private boolean imgSelected;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;
    private static int cameraId = 0;

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private Mat oldFrame;

    Point clickedPoint = new Point(0, 0);

    protected void init() {
        this.refreshImgButton.setVisible(false);
        this.capture = new VideoCapture();
        this.cameraActive = false;
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        currentFrame.setFitWidth(1280);
        currentFrame.setFitHeight(720);
        currentFrame.setPreserveRatio(true);
        imgSelected = false;
    }
    @FXML
    protected void startCamera(ActionEvent event) {
        imgSelected = false;
        refreshImgButton.setVisible(false);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp"));
        var file = fileChooser.showOpenDialog(this.chooseImgButton.getScene().getWindow());

        if (file != null) {
            imgSelected = true;

            loadImg(file);
        }

    }

    private void loadImg(File file) {
        System.out.println(file.getPath());
        this.oldFrame = Imgcodecs.imread(file.getPath());
        if (!oldFrame.empty()) {
            resetAllController();
            refreshImgButton.setVisible(true);

            updateImageView(currentFrame, Utils.mat2Image(oldFrame));
        }

    }

    private void resetAllController() {
        this.stopAcquisition();
        this.lbpClassifier.setSelected(false);
        this.haarClassifier.setSelected(false);
        this.cannyEdgeDetector.setSelected(false);
        this.dilateErode.setSelected(false);
        this.inverse.setSelected(false);
        this.absRemoval.setSelected(false);
        this.cameraActive = false;
        this.startCameraButton.setText("Start Camera");
    }

    @FXML
    public void haarSelected() {
        this.lbpClassifier.setSelected(false);
        this.cannyEdgeDetector.setSelected(false);
        this.edThreshold.setDisable(true);

        this.checkBoxSelection("target/classes/haarcascades/haarcascade_frontalface_alt.xml");

        if (imgSelected) {
            refreshImg();
        }
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

        if (imgSelected) {
            refreshImg();
        }
    }

    @FXML
    public void applyToImg() {
        if (imgSelected) {
            refreshImg();
        }
    }

    @FXML
    public void cannySelected() {
        this.haarClassifier.setSelected(false);
        this.lbpClassifier.setSelected(false);

        this.edThreshold.setDisable(false);

        if (imgSelected) {
            refreshImg();
        }
    }

    @FXML
    public void dilateErodeSelected() {
        if (dilateErode.isSelected()) {
            inverse.setDisable(false);
        } else {
            inverse.setSelected(false);
            inverse.setDisable(true);
        }

        if (imgSelected) {
            refreshImg();
        }
    }


    private Mat grabFrame() {
        Mat frame = new Mat();
        if (this.capture.isOpened()) {
            try {
                this.capture.read(frame);

                if (!frame.empty()) {
                    frame = processFrame(frame);
                }
            } catch (Exception ex) {
                System.out.println("Exception during the image elaboration: " + ex);
            }
        }

        return frame;
    }

    private void refreshImg() {
        if (! oldFrame.empty()) {
            var frame = processFrame(oldFrame);

            updateImageView(currentFrame, Utils.mat2Image(frame));
        }

    }

    private Mat processFrame(Mat oriFrame) {
        var frame = oriFrame;
        if(this.haarClassifier.isSelected() || this.lbpClassifier.isSelected()) {
            frame = this.detectAndDisplay(oriFrame);
        } else if (this.cannyEdgeDetector.isSelected()) {
            frame = this.doCanny(oriFrame);
        } else if (this.dilateErode.isSelected()) {
            if (this.absRemoval.isSelected()) {
                frame = this.doBackgroundRemovalFloodFill(oriFrame);
            } else {
                frame = this.doBackgroundRemoval(oriFrame);
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

    private Mat detectAndDisplay(Mat oriFrame) {
        MatOfRect faces = new MatOfRect();
        Mat frame = new Mat();
        Mat result = new Mat();
        oriFrame.copyTo(result);
        Imgproc.cvtColor(oriFrame, frame, Imgproc.COLOR_BGR2GRAY);

        Imgproc.equalizeHist(frame, frame);

        if (this.absoluteFaceSize == 0) {
            int height = frame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        this.faceCascade.detectMultiScale(frame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i ++) {
            System.out.println(facesArray[i].tl() + ", " + facesArray[i].br());
            Imgproc.rectangle(result, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
        }

        return result;
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
