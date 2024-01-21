import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameViewManager {
    public static boolean gameViewFlag = true;
    private static final int width = 800/3*ViewManager.Scale;
    private static final int height = 600/3*ViewManager.Scale;
    public static AnchorPane gamePane;
    public static Scene gameScene;
    public static Stage gameStage;
    private AnimationTimer gameLoop;
    private String faviconPath = "assets/favicon/1.png";
    private List<Image> backgrounds;
    private List<Image> crosshairs;
    private List<Image> foregrounds;
    public static Image currentBackground;
    public static Image currentCrosshair;
    private static Image currentForeground;
    private int backgroundIndex;
    private int crosshairIndex;
    private AudioClip introMusic;
    private Label titleLabel;
    private boolean isCursorVisible = false;
    private static ImageView backgroundImageView;
    private static ImageView foregroundImageView;
    private static ImageView crosshairImageView;

    public GameViewManager() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, width, height);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setTitle("HUBBM Duck Hunt");
        gameStage.getIcons().add(new Image(faviconPath));

        backgrounds = new ArrayList<>();
        backgrounds.add(new Image("assets/background/1.png"));
        backgrounds.add(new Image("assets/background/2.png"));
        backgrounds.add(new Image("assets/background/3.png"));
        backgrounds.add(new Image("assets/background/4.png"));
        backgrounds.add(new Image("assets/background/5.png"));
        backgrounds.add(new Image("assets/background/6.png"));

        crosshairs = new ArrayList<>();
        crosshairs.add(new Image("assets/crosshair/1.png"));
        crosshairs.add(new Image("assets/crosshair/2.png"));
        crosshairs.add(new Image("assets/crosshair/3.png"));
        crosshairs.add(new Image("assets/crosshair/4.png"));
        crosshairs.add(new Image("assets/crosshair/5.png"));
        crosshairs.add(new Image("assets/crosshair/6.png"));
        crosshairs.add(new Image("assets/crosshair/7.png"));

        foregrounds = new ArrayList<>();
        foregrounds.add(new Image("assets/foreground/1.png"));
        foregrounds.add(new Image("assets/foreground/2.png"));
        foregrounds.add(new Image("assets/foreground/3.png"));
        foregrounds.add(new Image("assets/foreground/4.png"));
        foregrounds.add(new Image("assets/foreground/5.png"));
        foregrounds.add(new Image("assets/foreground/6.png"));

        currentBackground = backgrounds.get(0); // default background
        currentCrosshair = crosshairs.get(0); // default crosshair
        backgroundIndex = 0;
        crosshairIndex = 0;

        createGameTextLabel();
        createImageViews();

        gameScene.setCursor(Cursor.NONE);
        gameScene.setOnMouseEntered(this::handleMouseEntered);
        gameScene.setOnMouseExited(this::handleMouseExited);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        };
    }

    public void startGame() {
        drawBackground();
        drawForeground();
        drawCrosshair();
        createGameTextLabel();
        attachListeners();
        gameLoop.start();
        gameStage.show();
    }

    private void createGameTextLabel() {
        titleLabel = new Label("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");
        titleLabel.setFont(Font.font("Arial", 25/3*ViewManager.Scale));
        titleLabel.setStyle("-fx-text-fill: yellow");
        titleLabel.setWrapText(true);

        StackPane titlePane = new StackPane(titleLabel);
        StackPane.setAlignment(titleLabel, Pos.CENTER);

        gamePane.getChildren().add(titlePane);
        AnchorPane.setTopAnchor(titlePane, 50.0/3*ViewManager.Scale);
        AnchorPane.setLeftAnchor(titlePane, 0.0/3*ViewManager.Scale);
        AnchorPane.setRightAnchor(titlePane, 0.0/3*ViewManager.Scale);
    }

    public void createImageViews() {
        backgroundImageView = new ImageView();
        foregroundImageView = new ImageView();
        crosshairImageView = new ImageView();
    }

    public static void removeImageViews() {
        gamePane.getChildren().removeAll(backgroundImageView, foregroundImageView, crosshairImageView);
    }

    /**
     * Creates and plays the intro music.
     * The music file is located in the "assets/effects/Intro.mp3" path relative to the class.
     * The volume of the music is set according to the ViewManager's volume property.
     * After playing the intro music for 6 seconds, it performs the following actions:
     * - Removes image views from the game pane.
     * - Draws the background.
     * - Creates a new instance of Level1 and adds its duck view to the game pane.
     * - Draws the foreground.
     * - Draws the crosshair.
     * - Removes the game text label.
     */
    private void createIntroMusic() {
        introMusic = new AudioClip(getClass().getResource("assets/effects/Intro.mp3").toString());
        introMusic.setVolume(ViewManager.Volume);
        introMusic.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(6)); // Intro müziği 6 saniye
        delay.setOnFinished(event -> {
            removeImageViews();
            drawBackground();
            Level1 level1 = new Level1();
            gamePane.getChildren().add(level1.getDuckView1());
            drawForeground();
            drawCrosshair();
            removeGameTextLabel();
        });
        delay.play();
    }

    private void attachListeners() {
        gameScene.setOnKeyPressed(this::handleKeyPressed);
    }

    /**
     * Handles key press events.
     * Checks the key code of the pressed key and performs specific actions based on the game view flag.
     * If the game view flag is true, the following actions are performed:
     * - If the LEFT arrow key is pressed, calls the switchBackground() method with -1 as the parameter.
     * - If the RIGHT arrow key is pressed, calls the switchBackground() method with 1 as the parameter.
     * - If the UP arrow key is pressed, calls the switchCrosshair() method with -1 as the parameter.
     * - If the DOWN arrow key is pressed, calls the switchCrosshair() method with 1 as the parameter.
     * - If the ENTER key is pressed, stops the background music, creates the intro music, and sets the game view flag to false.
     * - If the ESCAPE key is pressed, stops the background music, closes the game stage, creates a new instance of ViewManager,
     *   retrieves the main stage from ViewManager, sets the game view flag to true, and shows the main stage.
     *
     * @param event the KeyEvent object representing the key press event
     */
    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (gameViewFlag){
            if (keyCode == KeyCode.LEFT ) {
                switchBackground(-1);
            } else if (keyCode == KeyCode.RIGHT) {
                switchBackground(1);
            } else if (keyCode == KeyCode.UP) {
                switchCrosshair(-1);
            } else if (keyCode == KeyCode.DOWN) {
                switchCrosshair(1);
            } else if (keyCode == KeyCode.ENTER) {
                ViewManager.backgroundMusic.stop();
                createIntroMusic();
                gameViewFlag = false;
            } else if (keyCode == KeyCode.ESCAPE) {
                ViewManager.backgroundMusic.stop();
                gameStage.close();
                ViewManager manager = new ViewManager();
                Stage primaryStage = manager.getMainStage();
                GameViewManager.gameViewFlag = true;
                primaryStage.show();
            }
        }
    }

    /**
     * Handles the mouse entered event.
     * If the cursor is not visible, sets the cursor of the game scene to Cursor.NONE.
     *
     * @param event the MouseEvent object representing the mouse entered event
     */
    private void handleMouseEntered(MouseEvent event) {
        if (!isCursorVisible) {
            gameScene.setCursor(Cursor.NONE);
        }
    }

    /**
     * Handles the mouse exited event.
     * Sets the cursor of the game scene to Cursor.DEFAULT and updates the visibility of the cursor.
     *
     * @param event the MouseEvent object representing the mouse exited event
     */
    private void handleMouseExited(MouseEvent event) {
        gameScene.setCursor(Cursor.DEFAULT);
        isCursorVisible = false;
    }

    /**
     * Switches the background image by changing the background index based on the given offset.
     * If the offset is positive, it moves to the next background image.
     * If the offset is negative, it moves to the previous background image.
     * If the index goes beyond the bounds of the backgrounds list, it wraps around to the opposite end.
     * It updates the currentBackground and currentForeground variables accordingly.
     * Removes the previous background, foreground, crosshair, and game text label.
     * Draws the new background, foreground, game text label, and crosshair.
     *
     * @param offset the offset value to change the background image index
     */
    private void switchBackground(int offset) {
        backgroundIndex += offset;
        if (backgroundIndex < 0) {
            backgroundIndex = backgrounds.size() - 1;
        } else if (backgroundIndex >= backgrounds.size()) {
            backgroundIndex = 0;
        }
        currentBackground = backgrounds.get(backgroundIndex);
        currentForeground = foregrounds.get(backgroundIndex);
        removeBackground();
        removeForeground();
        removeCrosshair();
        removeGameTextLabel();
        drawBackground();
        drawForeground();
        createGameTextLabel();
        drawCrosshair();
    }

    /**
     * Switches the crosshair image by changing the crosshair index based on the given offset.
     * If the offset is positive, it moves to the next crosshair image.
     * If the offset is negative, it moves to the previous crosshair image.
     * If the index goes beyond the bounds of the crosshairs list, it wraps around to the opposite end.
     * It updates the currentCrosshair variable accordingly.
     * Removes the previous crosshair image and draws the new crosshair image.
     *
     * @param offset the offset value to change the crosshair image index
     */
    private void switchCrosshair(int offset) {
        crosshairIndex += offset;
        if (crosshairIndex < 0) {
            crosshairIndex = crosshairs.size() - 1;
        } else if (crosshairIndex >= crosshairs.size()) {
            crosshairIndex = 0;
        }
        currentCrosshair = crosshairs.get(crosshairIndex);
        removeCrosshair();
        drawCrosshair();
    }

    public static void drawBackground() {
        backgroundImageView.setImage(currentBackground);
        backgroundImageView.setFitWidth(width);
        backgroundImageView.setFitHeight(height);
        gamePane.getChildren().add(backgroundImageView);
    }

    public static void drawForeground() {
        foregroundImageView.setImage(currentForeground);
        foregroundImageView.setFitWidth(width);
        foregroundImageView.setFitHeight(height);
        gamePane.getChildren().add(foregroundImageView);
    }

    public static void removeBackground() {
        gamePane.getChildren().remove(backgroundImageView);
    }

    public static void removeForeground() {
        gamePane.getChildren().remove(foregroundImageView);
    }

    /**
     * Draws the crosshair on the game scene.
     * Sets the image of the crosshairImageView to the currentCrosshair image.
     * Adjusts the width and height of the crosshairImageView based on the currentCrosshair size and the ViewManager's scale.
     * Makes the crosshairImageView mouse transparent.
     * Positions the crosshairImageView at the center of the game scene to center the crosshair.
     * Adds the crosshairImageView to the game pane.
     */
    public static void drawCrosshair() {
        crosshairImageView.setImage(currentCrosshair);
        crosshairImageView.setFitWidth(currentCrosshair.getWidth()*3/3*ViewManager.Scale);
        crosshairImageView.setFitHeight(currentCrosshair.getHeight()*3/3*ViewManager.Scale);
        crosshairImageView.setMouseTransparent(true);

        // To center the crosshair we place it in the middle of the game scene
        double crosshairX = (gameScene.getWidth() - currentCrosshair.getWidth()*3/3*ViewManager.Scale) / 2;
        double crosshairY = (gameScene.getHeight() - currentCrosshair.getHeight()*3/3*ViewManager.Scale) / 2;
        crosshairImageView.setLayoutX(crosshairX);
        crosshairImageView.setLayoutY(crosshairY);

        gamePane.getChildren().add(crosshairImageView);
    }

    private void removeGameTextLabel() {
        gamePane.getChildren().remove(titleLabel);
    }

    public void removeCrosshair() {
        gamePane.getChildren().remove(crosshairImageView);
    }
}
