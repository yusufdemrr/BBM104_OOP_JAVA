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


public class Level4 {
    private ImageView duckView4;
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

    public Level4() {
        createBulletCountLabel();
        createLevelLabel();
        attachListeners2();

        Duck duck4 = new Duck();
        duck = duck4;
        duck.setDUCK_SPEED(8);
        duckView4 = duck4.getDuckView();
    }

    public ImageView getDuckView4() {
        return duckView4;
    }

    public void attachListeners2(){
        GameViewManager.gameScene.setOnMouseMoved(this::handleMouseMoved);
        GameViewManager.gameScene.setOnMouseClicked(this::handleMouseClicked);
    }

    private void handleMouseClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        // Control to shoot the duck with the Crosshair
        if (isHitDuck(mouseX, mouseY)) {
            bulletCountLabel.setText("Ammo Left: " + --bulletsLeft);
            duck.isduckHit = true;
            createGunShootEffect();
            shootDuck();
        }
        decreaseBulletCount();
    }

    private boolean isHitDuck(double mouseX, double mouseY) {
        double duckCenterX = duck.getDuckX() + DUCK_WIDTH / 2;
        double duckCenterY = duck.getDuckY() + DUCK_HEIGHT / 2;

        double distanceX = Math.abs(mouseX - duckCenterX);
        double distanceY = Math.abs(mouseY - duckCenterY);

        return distanceX <= DUCK_WIDTH / 2 && distanceY <= DUCK_HEIGHT / 2;
    }

    private void shootDuck() {
        // Duck vurulduğunda yapılacak işlemler
        createDuckFallsEffect();
        duckView4.setImage(duck.duckShotting);

        // duckShotting resminin 1 saniye gösterilmesi için bir geçiş süresi tanımlanıyor
        TranslateTransition shottingTransition = new TranslateTransition(Duration.seconds(1), duckView4);
        shottingTransition.setByY(0);
        shottingTransition.play();

        shottingTransition.setOnFinished(event -> {
            duckView4.setImage(duck.duckFalling);
            TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), duckView4);
            fallTransition.setByY(100/3*ViewManager.Scale); // 100 piksel aşağı düşme animasyonu
            fallTransition.setOnFinished(fallEvent -> {
                duckView4.setVisible(false); // Ördek resmini görünmez yap
                handleGameFinish();
                createLevelCompletedEffect();
            });
            fallTransition.play();
        });
    }


    private void handleGameFinish() {
        // Oyun bittiğinde yapılacak işlemler
        Label gameFinishLabel = new Label("You Win!\nPress Enter to Play Next Level");
        gameFinishLabel.setFont(Font.font("Arial", 30/3*ViewManager.Scale));
        gameFinishLabel.setStyle("-fx-text-fill: yellow");
        gameFinishLabel.setAlignment(Pos.CENTER);

        // Oyun bitirme etiketini ekranın ortasına yerleştir
        AnchorPane centerPane = new AnchorPane(gameFinishLabel);
        AnchorPane.setTopAnchor(gameFinishLabel, 0.0);
        AnchorPane.setRightAnchor(gameFinishLabel, 0.0);
        AnchorPane.setBottomAnchor(gameFinishLabel, 0.0);
        AnchorPane.setLeftAnchor(gameFinishLabel, 0.0);
        GameViewManager.gamePane.getChildren().add(centerPane);

        // Oyun bitirme etiketini oyun pane'ine ekle
        GameViewManager.gamePane.getChildren().add(gameFinishLabel);

        // Enter tuşuna basıldığında bir sonraki seviyeyi başlatmak için olay dinleyicisi ekle
        GameViewManager.gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GameViewManager.gamePane.getChildren().remove(gameFinishLabel); // Oyun bitirme etiketini kaldır
                GameViewManager.gamePane.getChildren().remove(levelLabel);
                GameViewManager.gamePane.getChildren().remove(bulletCountLabel);
                Duck.isduckHit = false;
                GameViewManager.removeImageViews();
                GameViewManager.drawBackground();
                Level5 level5 = new Level5();
                GameViewManager.gamePane.getChildren().add(level5.getDuckView5());
                GameViewManager.drawForeground();
                GameViewManager.drawCrosshair();
                levelCompletedEffect.stop();
            }
        });
    }
    private void handleGameOverFinish() {
        // Oyun bittiğinde yapılacak işlemler
        Label gameFinishLabel = new Label("Game Over!\nPress Enter to Play Again\nPress ESC to Exit");
        gameFinishLabel.setFont(Font.font("Arial", 30/3*ViewManager.Scale));
        gameFinishLabel.setStyle("-fx-text-fill: yellow");
        gameFinishLabel.setAlignment(Pos.CENTER);

        // Oyun bitirme etiketini ekranın ortasına yerleştir
        AnchorPane centerPane = new AnchorPane(gameFinishLabel);
        AnchorPane.setTopAnchor(gameFinishLabel, 0.0);
        AnchorPane.setRightAnchor(gameFinishLabel, 0.0);
        AnchorPane.setBottomAnchor(gameFinishLabel, 0.0);
        AnchorPane.setLeftAnchor(gameFinishLabel, 0.0);
        GameViewManager.gamePane.getChildren().add(centerPane);

        // Oyun bitirme etiketini oyun pane'ine ekle
        GameViewManager.gamePane.getChildren().add(gameFinishLabel);

        // Enter tuşuna basıldığında bir sonraki seviyeyi başlatmak için olay dinleyicisi ekle
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
    private void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double centerX ;
        double centerY ;

        // Crosshair'in merkezini mouse'un pozisyonuna göre güncelle
        centerX = mouseX - GameViewManager.currentCrosshair.getWidth()*3/3*ViewManager.Scale / 2;
        centerY = mouseY - GameViewManager.currentCrosshair.getHeight()*3/3*ViewManager.Scale / 2;

        drawCrosshair(centerX, centerY);
    }

    private void drawCrosshair(double centerX, double centerY) {
        ImageView crosshairImageView = new ImageView(GameViewManager.currentCrosshair);
        crosshairImageView.setFitWidth(GameViewManager.currentCrosshair.getWidth()*3/3*ViewManager.Scale);
        crosshairImageView.setFitHeight(GameViewManager.currentCrosshair.getHeight()*3/3*ViewManager.Scale);
        crosshairImageView.setLayoutX(centerX);
        crosshairImageView.setLayoutY(centerY);

        // Eski crosshair görüntüsünü kaldır
        GameViewManager.gamePane.getChildren().removeIf(node -> node instanceof ImageView && ((ImageView) node).getImage() == GameViewManager.currentCrosshair);

        // Yeni crosshair görüntüsünü ekle
        GameViewManager.gamePane.getChildren().add(crosshairImageView);
    }
    private void createBulletCountLabel() {
        bulletCountLabel = new Label("Ammo Left: " + bulletsLeft);
        bulletCountLabel.setFont(Font.font("Arial", 20/3*ViewManager.Scale));
        bulletCountLabel.setStyle("-fx-text-fill: yellow");

        AnchorPane.setTopAnchor(bulletCountLabel, 10.0/3*ViewManager.Scale); // Yükseklik ayarlanabilir
        AnchorPane.setRightAnchor(bulletCountLabel, 10.0/3*ViewManager.Scale); // Uzaklık ayarlanabilir

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
        levelLabel = new Label("Level: 4");
        levelLabel.setFont(Font.font("Arial", 20/3*ViewManager.Scale));
        levelLabel.setStyle("-fx-text-fill: yellow");

        AnchorPane.setTopAnchor(levelLabel, 10.0/3*ViewManager.Scale); // Yükseklik ayarlanabilir
        AnchorPane.setLeftAnchor(levelLabel, (ViewManager.gameWidth - levelLabel.getWidth()) / 2); // Yatayda ortalamak için

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


