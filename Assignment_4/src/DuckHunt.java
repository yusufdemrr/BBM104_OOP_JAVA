import javafx.application.Application;
import javafx.stage.Stage;


public class DuckHunt extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewManager manager = new ViewManager();
        primaryStage = manager.getMainStage();
        primaryStage.show();

    }
}
