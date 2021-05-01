package ind.sq.study.opencv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class FxHelloCV extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            var loader = new FXMLLoader(getClass().getClassLoader().getResource("FXHelloCV.fxml"));

            var rootElement = (BorderPane) loader.load();
            rootElement.setStyle("-fx-background-color: whitesmoke;");

            var scene = new Scene(rootElement, 1280, 820);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            primaryStage.setTitle("Face Detection");
            primaryStage.setScene(scene);
            primaryStage.show();


            FxHelloCVController controller = loader.getController();
            controller.init();
            System.out.println("Controller init.");
            primaryStage.setOnCloseRequest((event) -> {
                controller.setClosed();

            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        launch(args);
    }
}
