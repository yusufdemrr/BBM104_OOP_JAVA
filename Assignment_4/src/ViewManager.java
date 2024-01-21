import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;




public class ViewManager {
    public static final int Scale = 3;
    public static final int gameWidth = 800/3*Scale;
    public static final int gameHeight = 600/3*Scale;
    public static final double Volume = 0.025;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private String faviconPath = "assets/favicon/1.png";
    public static AudioClip backgroundMusic;



    public ViewManager(){
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane,gameWidth,gameHeight);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setTitle("HUBBM Duck Hunt");
        mainStage.getIcons().add(new Image(faviconPath));
        createBackground();
        createTextLabel();
        createMainMusic();
    }

    public Stage getMainStage(){
        return mainStage;
    }

    /**
     * Creates a text label with specified text, font, and style, and adds it to the main pane.
     * The label is positioned using layout coordinates.
     * When the ENTER key is pressed, a new game is started by creating a GameViewManager instance and calling the startGame() method.
     * When the ESCAPE key is pressed, the main stage is closed.
     */
    private void createTextLabel(){
        Label titleLabel = new Label("PRESS ENTER TO PLAY\nPRESS ESC TO EXIT");
        titleLabel.setFont(Font.font("Arial", 10*Scale));
        titleLabel.setStyle("-fx-text-fill: yellow");
        mainPane.getChildren().add(titleLabel);
        titleLabel.setLayoutX(250/3*Scale);
        titleLabel.setLayoutY(380/3*Scale);

        mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GameViewManager gameManager = new GameViewManager();
                gameManager.startGame();
                mainStage.close();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                mainStage.close();
            }
        });

    }

    private void createMainMusic(){
        backgroundMusic = new AudioClip(getClass().getResource("assets/effects/Title.mp3").toString());
        backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
        backgroundMusic.setVolume(Volume);
        backgroundMusic.play();
    }

    private void createBackground(){
        Image backgroundImage = new Image("assets/welcome/1.png",800/3*Scale,600/3*Scale,false,true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,null);
        mainPane.setBackground(new Background(background));
    }


}

