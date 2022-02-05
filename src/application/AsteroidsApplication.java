package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application{
    
    public static int WIDTH = 600;
    public static int HEIGHT = 400;
    
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        pane.setStyle("-fx-background-color: black");
        
        // loading custom font
        Font retroFont = Font.loadFont("file:resources/fonts/windows_command_prompt.ttf", 15);
        
        // points
        Text text = new Text(10, 20, "POINTS: 0");
        text.setFont(retroFont);
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);
        AtomicInteger points = new AtomicInteger();
        
        // game over text
        Text gameOver = new Text("");
        gameOver.setStyle("-fx-font-size: 25px;");
        
        // creating ship
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        
        // creating asteroids
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        
        // list of projectiles
        List<Projectile> projectiles = new ArrayList<>();

        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        
        Scene scene = new Scene(pane);
        
        // key input map
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });
        
        scene.setOnKeyReleased(event -> {
           pressedKeys.put(event.getCode(), Boolean.FALSE); 
        });
        
        // character movements
        new AnimationTimer() {
            
            @Override
            public void handle(long now) {

                // ship movement
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                    ship.deccelerate();
                }
                
                // projectile movement
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 4) {
                    // we shoot
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(4));

                    pane.getChildren().add(projectile.getCharacter());
                    
                    
                }
                
                // projectile colliding with asteroid
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
            
                    //points for hitting projectiles
                    if(!projectile.isAlive()) {
                        text.setText("POINTS: " + points.addAndGet(1000));
                    }
                });
                
                // projectile and asteroid removal after colliding
                projectiles.stream()
                    .filter(projectile -> !projectile.isAlive())
                    .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                    .filter(projectile -> !projectile.isAlive())
                    .collect(Collectors.toList()));

                asteroids.stream()
                    .filter(asteroid -> !asteroid.isAlive())
                    .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                    .filter(asteroid -> !asteroid.isAlive())
                    .collect(Collectors.toList()));
                
                //projectile removal if off screen
                projectiles.stream()
                    .filter(projectile -> !projectile.isActive())
                    .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                    .filter(projectile -> !projectile.isActive())
                    .collect(Collectors.toList()));
                
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
                
                // asteroid and ship collision stops the game
                asteroids.forEach(asteroid -> {
                   if (ship.collide(asteroid)) {
                       stop();
                       
                       StackPane stackPane = new StackPane();
                       stackPane.setPrefSize(WIDTH, HEIGHT);
                       stackPane.setStyle("-fx-background-color: black");
                       
                       gameOver.setText("GAME OVER \nPOINTS: " + points.get());
                       Font retroFont = Font.loadFont("file:resources/fonts/windows_command_prompt.ttf", 100);
                       gameOver.setFont(retroFont);
                       gameOver.setFill(Color.WHITE);
                       
                       stackPane.getChildren().add(gameOver);
                       StackPane.setAlignment(gameOver, Pos.CENTER);
                       
                       Scene endScene = new Scene(stackPane);
                       
                       stage.setScene(endScene);
                   } 
                });
                
                // asteroid spawner
                if(Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
            }
        }.start();
        
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
