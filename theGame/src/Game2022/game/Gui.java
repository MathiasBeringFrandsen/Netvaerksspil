package Game2022.game;

import java.io.IOException;
import java.util.*;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class Gui extends Application {

    public static final int size = 30;
    public static final int scene_height = size * 20 + 50;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;
    public static Image fire_up, fire_down, fire_left, fire_right, fire_vertical, fire_horizontal, fire_wallNorth, fire_wallSouth, fire_wallWest, fire_wallEast;
    public static Image darken_floor, darken_wall4;


    private static Label[][] fields;
    private TextArea scoreList;


    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            GridPane boardGrid = new GridPane();

            image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);

            hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fire_up = new Image(getClass().getResourceAsStream("Image/fireUp.png"), size, size, false, false);
            fire_down = new Image(getClass().getResourceAsStream("Image/fireDown.png"), size, size, false, false);
            fire_left = new Image(getClass().getResourceAsStream("Image/fireLeft.png"), size, size, false, false);
            fire_right = new Image(getClass().getResourceAsStream("Image/fireRight.png"), size, size, false, false);

            fire_vertical = new Image(getClass().getResourceAsStream("Image/fireVertical.png"), size, size, false, false);
            fire_horizontal = new Image(getClass().getResourceAsStream("Image/fireHorizontal.png"), size, size, false, false);

            fire_wallNorth = new Image(getClass().getResourceAsStream("Image/fireWallNorth.png"), size, size, false, false);
            fire_wallSouth = new Image(getClass().getResourceAsStream("Image/fireWallSouth.png"), size, size, false, false);
            fire_wallWest = new Image(getClass().getResourceAsStream("Image/fireWallWest.png"), size, size, false, false);
            fire_wallEast = new Image(getClass().getResourceAsStream("Image/fireWallEast.png"), size, size, false, false);

            darken_floor = new Image(getClass().getResourceAsStream("Image/darkenFloor.png"), size, size, false, false);
            darken_wall4 = new Image(getClass().getResourceAsStream("Image/darkenWall4.png"), size, size, false, false);

            fields = new Label[20][20];
            for (int j = 0; j < 20; j++) {
                for (int i = 0; i < 20; i++) {
                    switch (Generel.board[j].charAt(i)) {
                        case 'w':
                            fields[i][j] = new Label("", new ImageView(image_wall));
                            break;
                        case ' ':
                            fields[i][j] = new Label("", new ImageView(image_floor));
                            break;
                        default:
                            throw new Exception("Illegal field value: " + Generel.board[j].charAt(i));
                    }
                    boardGrid.add(fields[i][j], i, j);
                }
            }
            scoreList.setEditable(false);


            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case UP:
                        playerMoved(0, -1, "up");
                        break;
                    case DOWN:
                        playerMoved(0, +1, "down");
                        break;
                    case LEFT:
                        playerMoved(-1, 0, "left");
                        break;
                    case RIGHT:
                        playerMoved(+1, 0, "right");
                        break;
                    case SPACE:
                        playerShoot();
                        break;
                    case ESCAPE:
                        System.exit(0);
                    default:
                        break;
                }
            });

            // Putting default players on screen
            for (int i = 0; i < GameLogic.players.size(); i++) {
                fields[GameLogic.players.get(i).getXpos()][GameLogic.players.get(i).getYpos()].setGraphic(new ImageView(hero_up));
            }
            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removePlayerOnScreen(pair oldpos) {
        Platform.runLater(() -> {
            fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(image_floor));
        });
    }

    public static void removeProjectileOnScreen(pair oldpos) {
        Platform.runLater(() -> {
            fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(image_floor));
        });
    }

    public static void placePlayerOnScreen(pair newpos, String direction) {
        Platform.runLater(() -> {
            int newx = newpos.getX();
            int newy = newpos.getY();
            if (direction.equals("right")) {
                fields[newx][newy].setGraphic(new ImageView(hero_right));
            }
            ;
            if (direction.equals("left")) {
                fields[newx][newy].setGraphic(new ImageView(hero_left));
            }
            ;
            if (direction.equals("up")) {
                fields[newx][newy].setGraphic(new ImageView(hero_up));
            }
            ;
            if (direction.equals("down")) {
                fields[newx][newy].setGraphic(new ImageView(hero_down));
            }
            ;
        });
    }

    public static void darkenSurroundings(pair newPos, int width) {
        ArrayList<Integer> lightspotsX = new ArrayList<>();
        ArrayList<Integer> lightspotsY = new ArrayList<>();
        for (int i = newPos.getX(); i < newPos.getX() + width; i++) {
            lightspotsX.add(i);
        }
        for (int i = newPos.getY(); i < newPos.getY() + width; i++) {
            lightspotsY.add(i);
        }

        for (int i = newPos.getX() - 1; i > newPos.getX() - width; i--) {
            lightspotsX.add(i);
        }
        for (int i = newPos.getY() - 1; i > newPos.getY() - width; i--) {
            lightspotsY.add(i);
        }

        System.out.println(lightspotsX);
        System.out.println(lightspotsY);

        Platform.runLater(() -> {
            for (int y = 0; y < 20; y++) {
                for (int x = 0; x < 20; x++) {
                    if (!(lightspotsX.contains(x) && lightspotsY.contains(y))) {
                        System.out.println(y + " " + x);
                        if (Generel.board[y].charAt(x) == ' ') {
                            fields[x][y].setGraphic(new ImageView(darken_floor));
                        } else {
                            fields[x][y].setGraphic(new ImageView(darken_wall4));
                        }
                    } else {
                        if (!(y == newPos.getY() && x == newPos.getX()) && GameLogic.getPlayerAt(x, y) == null) {
                            if (Generel.board[y].charAt(x) == ' ') {
                                fields[x][y].setGraphic(new ImageView(image_floor));
                            } else {
                                fields[x][y].setGraphic(new ImageView(image_wall));
                            }
                        }
                    }
                }
            }
        });
    }

    public static void placeProjectileOnScreen(Projectile projectile, String type) {
        Platform.runLater(() -> {
            ImageView graphic;
            String direction = projectile.getDirection();
            int newx = projectile.location.getX();
            int newy = projectile.location.getY();
            if (direction.equals("right")) {
                if (type.equals("start")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_right));
                } else if (type.equals("middle")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_horizontal));
                } else {
                    fields[newx][newy].setGraphic(new ImageView(fire_wallEast));
                }
            }

            if (direction.equals("left")) {
                if (type.equals("start")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_left));
                } else if (type.equals("middle")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_horizontal));
                } else {
                    fields[newx][newy].setGraphic(new ImageView(fire_wallWest));
                }
            }

            if (direction.equals("up")) {
                if (type.equals("start")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_up));
                } else if (type.equals("middle")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_vertical));
                } else {
                    fields[newx][newy].setGraphic(new ImageView(fire_wallNorth));
                }
            }

            if (direction.equals("down")) {
                if (type.equals("start")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_down));
                } else if (type.equals("middle")) {
                    fields[newx][newy].setGraphic(new ImageView(fire_vertical));
                } else {
                    fields[newx][newy].setGraphic(new ImageView(fire_wallSouth));
                }
            }
            ;
        });
    }

    public static void movePlayerOnScreen(pair oldpos, pair newpos, String direction) {
        removePlayerOnScreen(oldpos);
        placePlayerOnScreen(newpos, direction);
    }

    public void updateScoreTable() {
        Platform.runLater(() -> {
            scoreList.setText(getScoreList());
        });
    }

    public void playerMoved(int delta_x, int delta_y, String direction) {
        GameLogic.sendKoordinater(delta_x, delta_y, direction);
        updateScoreTable();
    }

    public void playerShoot() {
        System.out.println("Space Clicked!");
        GameLogic.sendProjectileToServer();
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : GameLogic.players) {
            b.append(p + "\r\n");
        }
        return b.toString();
    }


}

