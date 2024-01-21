import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Level1 {
    private ImageView duckView1;
    private Duck duck;
    public static AudioClip gunshotEffect;
    public static AudioClip duckFallsEffect;
    public static AudioClip gameOverEffect;
    public static AudioClip levelCompletedEffect;
    private Label bulletCountLabel;
    private Label levelLabel;
    private static final int DUCK_WIDTH = 100/3*ViewManager.Scale;
    private static final int DUCK_HEIGHT = 100/3*ViewManager.Scale;
    private int bulletsLeft = 3;

    public Level1() {
        createBulletCountLabel();
        createLevelLabel();
        attachListeners2();

        Duck duck1 = new Duck();
        duck = duck1;
        duckView1 = duck1.getDuckView();
    }

    public ImageView getDuckView1() {
        return duckView1;
    }

    public void attachListeners2(){
        GameViewManager.gameScene.setOnMouseMoved(this::handleMouseMoved);
        GameViewManager.gameScene.setOnMouseClicked(this::handleMouseClicked);
    }

    /**
     * Handles the mouse click event in the game.
     * Retrieves the x and y coordinates of the mouse click from the MouseEvent.
     * Checks if the mouse click position is on the duck using the isHitDuck() method.
     * If the duck is hit, it updates the bullet count label, creates the gun shoot effect, marks the duck as hit,
     * and shoots the duck using the shootDuck() method.
     * Decreases the bullet count regardless of whether the duck is hit or not.
     *
     * @param event the MouseEvent representing the mouse click event
     */
    private void handleMouseClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        // Control to shoot the duck with the Crosshair
        if (isHitDuck(mouseX, mouseY)) {
            bulletCountLabel.setText("Ammo Left: " + --bulletsLeft);
            createGunShootEffect();
            Duck.isduckHit = true;
            shootDuck();
        }
        decreaseBulletCount();
    }

    /**
     * Checks if the given mouse click position is within the bounds of the duck.
     * Calculates the center coordinates of the duck using duckX and duckY, and the dimensions of the duck (DUCK_WIDTH and DUCK_HEIGHT).
     * Calculates the distances between the mouse click position and the duck center along the X and Y axes.
     * Returns true if the distance along the X axis is less than or equal to half of the duck's width (DUCK_WIDTH / 2)
     * and the distance along the Y axis is less than or equal to half of the duck's height (DUCK_HEIGHT / 2),
     * indicating that the mouse click is within the bounds of the duck.
     *
     * @param mouseX the x-coordinate of the mouse click position
     * @param mouseY the y-coordinate of the mouse click position
     * @return true if the mouse click is within the bounds of the duck, false otherwise
     */
    private boolean isHitDuck(double mouseX, double mouseY) {
        double duckCenterX = duck.getDuckX() + DUCK_WIDTH / 2;
        double duckCenterY = duck.getDuckY() + DUCK_HEIGHT / 2;

        double distanceX = Math.abs(mouseX - duckCenterX);
        double distanceY = Math.abs(mouseY - duckCenterY);

        return distanceX <= DUCK_WIDTH / 2 && distanceY <= DUCK_HEIGHT / 2;
    }

    /**
     * Performs the necessary actions when the duck is shot.
     * Creates the duck falls effect and updates the duckView1 image to the duck shotting image.
     * Defines a translation transition for the duckView1 to show the shotting animation for 1 second.
     * Updates the duckView1 image to the duck falling image when the shotting transition is finished.
     * Defines a translation transition for the duckView1 to simulate the duck falling animation by moving it 100/3*ViewManager.Scale pixels down.
     * Hides the duckView1 when the falling transition is finished.
     * Handles the game finish, and creates the level completed effect.
     */
    private void shootDuck() {
        // Actions to be taken when duck is hit
        createDuckFallsEffect();
        duckView1.setImage(duck.duckShotting);

        // Define a transition time for the duckShotting image to show for 1 second
        TranslateTransition shottingTransition = new TranslateTransition(Duration.seconds(1), duckView1);
        shottingTransition.setByY(0);
        shottingTransition.play();

        shottingTransition.setOnFinished(event -> {
            duckView1.setImage(duck.duckFalling);
            TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), duckView1);
            fallTransition.setByY(100/3*ViewManager.Scale); // 100px drop-down animation (when scale 3)
            fallTransition.setOnFinished(fallEvent -> {
                duckView1.setVisible(false); // Make the duck picture invisible
                handleGameFinish();
                createLevelCompletedEffect();
            });
            fallTransition.play();
        });
    }


    /**
     * Performs the necessary actions when the game finishes.
     * Creates a game finish label with the "You Win! Press Enter to Play Next Level" message.
     * Sets the font, text fill, and alignment of the game finish label.
     * Positions the game finish label at the center of the screen using an AnchorPane.
     * Adds the game finish label to the gamePane.
     * Adds an event listener to the gameScene that listens for the Enter key press to start the next level.
     * When the Enter key is pressed, removes the game finish label, level label, bullet count label from the gamePane.
     * Resets the duckHit flag to false.
     * Removes the image views, draws the background, and adds the duckView2 for Level2.
     * Draws the foreground and the crosshair.
     * Stops the level completed effect.
     */
    private void handleGameFinish() {
        // Actions to be taken when the game is over
        Label gameFinishLabel = new Label("You Win!\nPress Enter to Play Next Level");
        gameFinishLabel.setFont(Font.font("Arial", 30/3*ViewManager.Scale));
        gameFinishLabel.setStyle("-fx-text-fill: yellow");
        gameFinishLabel.setAlignment(Pos.CENTER);

        // Place the endgame tag in the middle of the screen
        AnchorPane centerPane = new AnchorPane(gameFinishLabel);
        AnchorPane.setTopAnchor(gameFinishLabel, 0.0);
        AnchorPane.setRightAnchor(gameFinishLabel, 0.0);
        AnchorPane.setBottomAnchor(gameFinishLabel, 0.0);
        AnchorPane.setLeftAnchor(gameFinishLabel, 0.0);
        GameViewManager.gamePane.getChildren().add(centerPane);

        // Add the endgame tag to the game board
        GameViewManager.gamePane.getChildren().add(gameFinishLabel);

        // Add event listener to start next level when enter key is pressed
        GameViewManager.gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GameViewManager.gamePane.getChildren().remove(gameFinishLabel); // Remove endgame tag
                GameViewManager.gamePane.getChildren().remove(levelLabel);
                GameViewManager.gamePane.getChildren().remove(bulletCountLabel);
                Duck.isduckHit = false;
                GameViewManager.removeImageViews();
                GameViewManager.drawBackground();
                Level2 level2 = new Level2();
                GameViewManager.gamePane.getChildren().add(level2.getDuckView2());
                GameViewManager.drawForeground();
                GameViewManager.drawCrosshair();
                levelCompletedEffect.stop();
            }
        });
    }

    /**
     * Performs the necessary actions when the game is over.
     * Creates a game finish label with the "Game Over! Press Enter to Play Again, Press ESC to Exit" message.
     * Sets the font, text fill, and alignment of the game finish label.
     * Positions the game finish label at the center of the screen using an AnchorPane.
     * Adds the game finish label to the gamePane.
     * Adds an event listener to the gameScene that listens for the Enter key press to play again or ESC key press to exit the game.
     * When the Enter key is pressed, removes the game finish label, level label, bullet count label from the gamePane.
     * Stops the game over effect.
     * Resets the duckHit flag to false.
     * Removes the image views, draws the background, and adds the duckView1 for Level1.
     * Draws the foreground and the crosshair.
     * When the ESC key is pressed, stops the game over effect, closes the gameStage, and returns to the main menu.
     */
    private void handleGameOverFinish() {
        // Actions to be taken when the game is over
        Label gameFinishLabel = new Label("Game Over!\nPress Enter to Play Again\nPress ESC to Exit");
        gameFinishLabel.setFont(Font.font("Arial", 30/3*ViewManager.Scale));
        gameFinishLabel.setStyle("-fx-text-fill: yellow");
        gameFinishLabel.setAlignment(Pos.CENTER);

        // Place the endgame tag in the middle of the screen
        AnchorPane centerPane = new AnchorPane(gameFinishLabel);
        AnchorPane.setTopAnchor(gameFinishLabel, 0.0);
        AnchorPane.setRightAnchor(gameFinishLabel, 0.0);
        AnchorPane.setBottomAnchor(gameFinishLabel, 0.0);
        AnchorPane.setLeftAnchor(gameFinishLabel, 0.0);
        GameViewManager.gamePane.getChildren().add(centerPane);

        // Add the endgame tag to the game board
        GameViewManager.gamePane.getChildren().add(gameFinishLabel);

        // Add event listener to start first level when enter key is pressed
        GameViewManager.gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GameViewManager.gamePane.getChildren().remove(gameFinishLabel); // Oyun bitirme etiketini kaldır
                GameViewManager.gamePane.getChildren().remove(levelLabel);
                GameViewManager.gamePane.getChildren().remove(bulletCountLabel);
                gameOverEffect.stop();
                Duck.isduckHit = false;
                GameViewManager.removeImageViews();
                GameViewManager.drawBackground();
                Level1 level1 = new Level1();
                GameViewManager.gamePane.getChildren().add(level1.getDuckView1());
                GameViewManager.drawForeground();
                GameViewManager.drawCrosshair();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                gameOverEffect.stop();
                Duck.isduckHit = false;
                GameViewManager.gameStage.close();
                ViewManager manager = new ViewManager();
                Stage primaryStage = manager.getMainStage();
                GameViewManager.gameViewFlag = true;
                primaryStage.show();
            }
        });
    }
    /**
     * Handles the mouse movement event.
     * Updates the center position of the crosshair based on the mouse position.
     * Calculates the centerX and centerY values by subtracting half of the crosshair's width and height respectively.
     * Calls the drawCrosshair() method to redraw the crosshair at the updated position.
     *
     * @param event The MouseEvent object containing information about the mouse movement.
     */
    private void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double centerX ;
        double centerY ;

        // Update the center of the crosshair according to the mouse position
        centerX = mouseX - GameViewManager.currentCrosshair.getWidth()*3/3*ViewManager.Scale / 2;
        centerY = mouseY - GameViewManager.currentCrosshair.getHeight()*3/3*ViewManager.Scale / 2;

        drawCrosshair(centerX, centerY);
    }

    /**
     * Draws the crosshair at the specified center position.
     * Creates a new ImageView with the current crosshair image, sets its dimensions, and positions it at the specified center coordinates.
     * Removes the previous crosshair image from the game pane.
     * Adds the new crosshair image to the game pane.
     *
     * @param centerX The x-coordinate of the center position of the crosshair.
     * @param centerY The y-coordinate of the center position of the crosshair.
     */
    private void drawCrosshair(double centerX, double centerY) {
        ImageView crosshairImageView = new ImageView(GameViewManager.currentCrosshair);
        crosshairImageView.setFitWidth(GameViewManager.currentCrosshair.getWidth()*3/3*ViewManager.Scale);
        crosshairImageView.setFitHeight(GameViewManager.currentCrosshair.getHeight()*3/3*ViewManager.Scale);
        crosshairImageView.setLayoutX(centerX);
        crosshairImageView.setLayoutY(centerY);

        // Remove old crosshair image
        GameViewManager.gamePane.getChildren().removeIf(node -> node instanceof ImageView && ((ImageView) node).getImage() == GameViewManager.currentCrosshair);

        // Add new crosshair image
        GameViewManager.gamePane.getChildren().add(crosshairImageView);
    }
    private void createBulletCountLabel() {
        bulletCountLabel = new Label("Ammo Left: " + bulletsLeft);
        bulletCountLabel.setFont(Font.font("Arial", 20/3*ViewManager.Scale));
        bulletCountLabel.setStyle("-fx-text-fill: yellow");

        AnchorPane.setTopAnchor(bulletCountLabel, 10.0/3*ViewManager.Scale);
        AnchorPane.setRightAnchor(bulletCountLabel, 10.0/3*ViewManager.Scale);

        GameViewManager.gamePane.getChildren().add(bulletCountLabel);
    }

    private void decreaseBulletCount() {
        bulletsLeft--;
        removeBulletCount();
        if (bulletsLeft >= 0 && Duck.isduckHit == false) {
            bulletCountLabel.setText("Ammo Left: " + bulletsLeft);
            createGunShootEffect();
        }

        if (bulletsLeft == 0 && Duck.isduckHit == false) {
            createGameOverEffect();
            handleGameOverFinish();
        }
    }

    private void removeBulletCount() {
        StackPane titlePane = null;
        for (Node node : GameViewManager.gamePane.getChildren()) {
            if (node instanceof StackPane && ((StackPane) node).getChildren().contains(bulletCountLabel)) {
                titlePane = (StackPane) node;
                break;
            }
        }
        if (titlePane != null) {
            GameViewManager.gamePane.getChildren().remove(bulletCountLabel);
        }
    }

    private void createLevelLabel() {
        levelLabel = new Label("Level: 1");
        levelLabel.setFont(Font.font("Arial", 20/3*ViewManager.Scale));
        levelLabel.setStyle("-fx-text-fill: yellow");

        AnchorPane.setTopAnchor(levelLabel, 10.0/3*ViewManager.Scale);
        AnchorPane.setLeftAnchor(levelLabel, (ViewManager.gameWidth - levelLabel.getWidth()) / 2); // To center horizontally

        GameViewManager.gamePane.getChildren().add(levelLabel);
    }


    private void createGunShootEffect(){
        gunshotEffect = new AudioClip(getClass().getResource("assets/effects/Gunshot.mp3").toString());
        gunshotEffect.setVolume(ViewManager.Volume);
        gunshotEffect.play();
    }

    private void createDuckFallsEffect(){
        duckFallsEffect = new AudioClip(getClass().getResource("assets/effects/DuckFalls.mp3").toString());
        duckFallsEffect.setVolume(ViewManager.Volume);
        duckFallsEffect.play();
    }

    private void createGameOverEffect(){
        gameOverEffect = new AudioClip(getClass().getResource("assets/effects/GameOver.mp3").toString());
        gameOverEffect.setVolume(ViewManager.Volume);
        gameOverEffect.play();
    }

    private void createLevelCompletedEffect(){
        levelCompletedEffect = new AudioClip(getClass().getResource("assets/effects/LevelCompleted.mp3").toString());
        levelCompletedEffect.setVolume(ViewManager.Volume);
        levelCompletedEffect.play();
    }

}


