import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

    public static final int size = 20;
    public static final int scene_height = size * 20 + 100;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;

    public static Player s;
    public static List<Player> players = new ArrayList<Player>();

    private Label[][] fields;
    private TextArea scoreList;

    private String[] board = {    // 20x20
            "wwwwwwwwwwwwwwwwwwww",
            "w        ww        w",
            "w w  w  www w  w  ww",
            "w w  w   ww w  w  ww",
            "w  w               w",
            "w w w w w w w  w  ww",
            "w w     www w  w  ww",
            "w w     w w w  w  ww",
            "w   w w  w  w  w   w",
            "w     w  w  w  w   w",
            "w ww ww        w  ww",
            "w  w w    w    w  ww",
            "w        ww w  w  ww",
            "w         w w  w  ww",
            "w        w     w  ww",
            "w  w              ww",
            "w  w www  w w  ww ww",
            "w w      ww w     ww",
            "w   w   ww  w      w",
            "wwwwwwwwwwwwwwwwwwww"
    };



    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Opretter forbindelse til server
            Scanner scanner = new Scanner(System.in);
            Socket serverSocket = new Socket("localhost", 9999);
            DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            //Starter read tråd
            ReadThread readThread = new ReadThread(inFromServer, serverSocket);
            readThread.start();

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

            fields = new Label[20][20];
            for (int j = 0; j < 20; j++) {
                for (int i = 0; i < 20; i++) {
                    switch (board[j].charAt(i)) {
                        case 'w':
                            fields[i][j] = new Label("", new ImageView(image_wall));
                            break;
                        case ' ':
                            fields[i][j] = new Label("", new ImageView(image_floor));
                            break;
                        default:
                            throw new Exception("Illegal field value: " + board[j].charAt(i));
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
                        try {
                            outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " + s.getYpos() + " " + "up" + "\n"); // Sender beskeder til server
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case DOWN:
                        try {
                            outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " + s.getYpos() + " " + "down" + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case LEFT:
                        try {
                            outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " + s.getYpos() + " " + "left" + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case RIGHT:
                        try {
                            outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " + s.getYpos() + " " + "right" + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        break;
                }
            });

            // Setting up standard players

            System.out.println("Indtast dit navn: "); // Opretter spiller med det indtastede navn.
            String navn = scanner.nextLine();

            Player s1 = new Player(navn, 10, 4, "up");
            players.add(s1);
            fields[10][4].setGraphic(new ImageView(hero_up));

            s = s1; // Spilleren man styrer er s, ville være nemmere hvis det var p


            System.out.println(players);
            System.out.println(s);
            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playerMoved(String navn, int delta_x, int delta_y, String direction) {
        for (Player s : players) { // Løber spiller listen igennem for at finde spiller med matchende navn
            if (s.getName().equals(navn)) {
                s.direction = direction;
                int x = s.getXpos(), y = s.getYpos();

                if (board[y + delta_y].charAt(x + delta_x) == 'w') {
                    s.addPoints(-1);
                } else {
                    Player p = getPlayerAt(x + delta_x, y + delta_y);
                    if (p != null) {
                        s.addPoints(10);
                        p.addPoints(-10);
                    } else {
                        s.addPoints(1);

                        fields[x][y].setGraphic(new ImageView(image_floor));
                        x += delta_x;
                        y += delta_y;

                        if (direction.equals("right")) {
                            fields[x][y].setGraphic(new ImageView(hero_right));
                        }
                        ;
                        if (direction.equals("left")) {
                            fields[x][y].setGraphic(new ImageView(hero_left));
                        }
                        ;
                        if (direction.equals("up")) {
                            fields[x][y].setGraphic(new ImageView(hero_up));
                        }
                        ;
                        if (direction.equals("down")) {
                            fields[x][y].setGraphic(new ImageView(hero_down));
                        }
                        ;

                        s.setXpos(x);
                        s.setYpos(y);
                    }
                }
            }
        }

        scoreList.setText(getScoreList());
    }


    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : players) {
            b.append(p + "\r\n");
        }
        return b.toString();
    }

    public Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }


    class ReadThread extends Thread {
        private BufferedReader reader;
        private Socket connSocket;

        public ReadThread(BufferedReader reader, Socket connSocket) {
            this.reader = reader;
            this.connSocket = connSocket;
        }

        @Override
public void run() {
    try {
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
        while (true) {
            String[] pos = inFromClient.readLine().split("\\s++");
            String playerName = pos[0];
            int newX = Integer.parseInt(pos[1]);
            int newY = Integer.parseInt(pos[2]);
            String direction = pos[3];

            Player player = getPlayerByName(playerName); // Tjekker om en spiller er i listen og returnerer den. Se metoden
            if (player == null) { // Hvis spiller ikke findes, oprettes en ny.
                player = new Player(playerName, newX, newY, direction);
                players.add(player);
            } else {
                //System.out.println(player.getName()); // Debug for at se om det rigtige objekt vælges
            }

            Platform.runLater(() -> {
                int deltaY = 0;
                int deltaX = 0;
                if (direction.equals("down")) {
                    deltaY++;
                } else if (direction.equals("up")) {
                    deltaY--;
                } else if (direction.equals("left")) {
                    deltaX--;
                } else if (direction.equals("right")) {
                    deltaX++;
                }
                playerMoved(playerName, deltaX, deltaY, direction);
            });
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

        private Player getPlayerByName(String name) { // Metode for sig selv, da vi fik fejl da vi kørte noget lign. i run metoden
            for (Player player : players) {
                if (player.getName().equals(name)) {
                    return player;
                }
            }
            return null;
        }

    }
}


