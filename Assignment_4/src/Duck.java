import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Duck {
    private ImageView duckView;
    private boolean movingRight;
    private static final int DUCK_WIDTH = 100/3*ViewManager.Scale;
    private static final int DUCK_HEIGHT = 100/3*ViewManager.Scale;
    private double duckX;
    private double duckY = ViewManager.gameHeight / 2 - DUCK_HEIGHT / 2;
    public static boolean isduckHit = false;
    private Image duckImage1;
    private Image duckImage2;
    private Image duckImage3;
    public static Image duckShotting;
    public static Image duckFalling;
    private int DUCK_SPEED = 1*ViewManager.Scale;
    private static final int FRAME_DURATION = 10; // Picture transition time (ms)
    private int bulletsLeft = 3;

    public Duck() {
        createDuckView();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateDuck();
            }
        };
        timer.start();
    }

    /**
     * Creates the duck view for the game.
     * Initializes the duckView as a new ImageView and sets its width and height to DUCK_WIDTH and DUCK_HEIGHT constants, respectively.
     * Loads the images for the duck animation, including the right-left movement frames (duck_black),
     * the shotting frame when the duck is shot, and the falling frame when the duck is falling (duck_black).
     * Sets the initial direction of the duck movement to movingRight (true).
     */
    private void createDuckView() {
        duckView = new ImageView();
        duckView.setFitWidth(DUCK_WIDTH);
        duckView.setFitHeight(DUCK_HEIGHT);

        // Right-left (duck_black)
        duckImage1 = new Image("assets/duck_black/4.png");
        duckImage2 = new Image("assets/duck_black/5.png");
        duckImage3 = new Image("assets/duck_black/6.png");
        // duck shot and falling (duck_black)
        duckShotting = new Image("assets/duck_black/7.png");
        duckFalling = new Image("assets/duck_black/8.png");

        movingRight = true;
    }


    public ImageView getDuckView() {
        return duckView;
    }

    /**
     * Updates the position and image of the duck in the game.
     * If the duck is not hit, it handles the duck movement based on the current direction (movingRight).
     * If movingRight is true, it moves the duck to the right by DUCK_SPEED.
     * If movingRight is false, it moves the duck to the left by DUCK_SPEED.
     * When the duck reaches the game boundaries, it changes the direction and updates the duckView scale accordingly.
     * Updates the position of the duckView based on duckX and duckY.
     * Updates the image of the duckView based on the current frameIndex and the duck images (duckImage1, duckImage2, duckImage3).
     */
    private void updateDuck() {
        if (!isduckHit) {
            if (movingRight) {
                duckX += DUCK_SPEED;
                if (duckX >= ViewManager.gameWidth - DUCK_WIDTH) {
                    duckX = ViewManager.gameWidth - DUCK_WIDTH;
                    movingRight = false;
                    duckView.setScaleX(-1); // reflection
                }
            } else {
                duckX -= DUCK_SPEED;
                if (duckX <= 0) {
                    duckX = 0;
                    movingRight = true;
                    duckView.setScaleX(1); // No need for reflection
                }
            }

            duckView.setX(duckX);
            duckView.setY(duckY);

            // Update duck image
            int frameIndex = (int) (duckX / DUCK_SPEED) / FRAME_DURATION;
            Image currentImage;

            if (frameIndex % 3 == 0) {
                currentImage = duckImage1;
            } else if (frameIndex % 3 == 1) {
                currentImage = duckImage2;
            } else {
                currentImage = duckImage3;
            }

            duckView.setImage(currentImage);
        }
    }

    public double getDuckX() {
        return duckX;
    }

    public void setDuckX(double duckX) {
        this.duckX = duckX;
    }

    public double getDuckY() {
        return duckY;
    }

    public void setDuckY(double duckY) {
        this.duckY = duckY;
    }

    public void setDUCK_SPEED(int DUCK_SPEED) {
        this.DUCK_SPEED = DUCK_SPEED*ViewManager.Scale/3;
    }
}



