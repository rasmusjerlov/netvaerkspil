import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
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
import javafx.scene.text.*;

public class GUI extends Application {

	public static final int size = 20; 
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;

	public static Player s;
	public static List<Player> players = new ArrayList<Player>();

	private Label[][] fields;
	private TextArea scoreList;
	
	private  String[] board = {    // 20x20
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

	private int clientId;

	
	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) throws IOException {
		try {

			Socket serverSocket = new Socket("10.10.138.2", 9999);
			DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			this.clientId = Integer.parseInt(inFromServer.readLine());
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

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

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
					default: throw new Exception("Illegal field value: " + board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);


			grid.add(mazeLabel,  0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);

			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:
                    try {
                        outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " +  s.getYpos() + " " + "up" + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
				case DOWN:
					try {
						outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " +  s.getYpos() + " " + "down" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case LEFT:
					try {
						outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " +  s.getYpos() + " " + "left" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case RIGHT:
					try {
						outToServer.writeBytes(s.getName() + " " + s.getXpos() + " " +  s.getYpos() + " " + "right" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				default: break;
				}
			});

            // Setting up standard players

			Player Rasmus = new Player("Rasmus",9,4,"up");
			players.add(Rasmus);
			fields[9][4].setGraphic(new ImageView(hero_up));

			Player Mikkel = new Player("Mikkel",14,15,"up");
			players.add(Mikkel);
			fields[14][15].setGraphic(new ImageView(hero_up));

			Player Anders = new Player("Anders",3,4,"up");
			players.add(Anders);
			fields[14][15].setGraphic(new ImageView(hero_up));

			System.out.println(players);
			s = players.get(clientId);
			System.out.println(s);
			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void playerMoved(List<Player> players, int id, int delta_x, int delta_y, String direction) {
			Player s = players.get(id);
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
			scoreList.setText(getScoreList());
	}


	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
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
			String message;
            while (true) {
				//message = inFromClient.readLine();
				String[] pos = inFromClient.readLine().split("\\s++");
				String direction = pos[3];
				System.out.println(direction);
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
				playerMoved(players, Integer.parseInt(pos[0]), deltaX, deltaY, direction);
				});
				//players.get(Integer.parseInt(pos[0])).setXpos(Integer.parseInt(pos[1]));
				//players.get(Integer.parseInt(pos[0])).setYpos(Integer.parseInt(pos[2]));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

	
}

