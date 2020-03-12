package skyWarsGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class GameState implements Observable {

	private Grid grid;
	private Random rand;
	private EnemyShipFactory enemyShipFactory = new EnemyShipFactory();
	private int gameMode;
	private int defensiveMode = 0;
	private int offensiveMode = 1;
	private int defensiveModeEnemies = 2;
	private int offensiveModeEnemies = 3;
	private String gameModeMessage;
	private boolean gameOver;
	private ArrayList<SkyWarsGUI> gameModeObservers = new ArrayList<SkyWarsGUI>();
	private final String FILE_NAME = "saveGame.ser";

	public GameState() {}

	public void initialiseNewGame() {

		this.grid = new Grid();
		setGameOver(false);
		setGameMode(defensiveMode);

		rand = new Random(System.currentTimeMillis());

		//places MasterShip on a random square
		this.grid.setTempMastershipRow(rand.nextInt(this.grid.getCOLUMNS()));
		this.grid.setTempMastershipColumn(rand.nextInt(this.grid.getROWS()));
		this.grid.getTheGrid().get(this.grid.getTempMastershipRow()).getTheSquares().get(this.grid.getTempMastershipColumn()).addMasterShip(new MasterShip());

	}

	public void makeAMove() {

		moveMasterShipOnGrid();
		grid.printGrid();
		moveEnemyShipsOnGrid();
		grid.printGrid();
		checkSquares();

	}

	public void moveMasterShipOnGrid() {

		//gets the current location of MS
		int xLoc = grid.getTempMastershipRow();
		int yLoc = grid.getTempMastershipColumn();

		grid.moveMasterShip(xLoc, yLoc);
		createEnemyShip();

	}

	//creates a new enemy ship (1 in 3 chance)
	public void createEnemyShip() {
		int randomEnemyShipGenerator;
		int chanceOfEnemy = 3;
		int enemyStartRow = 0;
		int enemyStartColumn = 0;

		rand = new Random(System.currentTimeMillis());

		randomEnemyShipGenerator = rand.nextInt(chanceOfEnemy);
		if(randomEnemyShipGenerator == 1) {
			//sets the initial enemy ship location to 0,0 - all enemy ships enter the sky from the top left corner
			grid.setTempEnemyShipRow(enemyStartRow);
			grid.setTempEnemyShipColumn(enemyStartColumn);
			//generates a random enemy ship
			EnemyShip enemyShip = enemyShipFactory.getEnemyShip();
			grid.getTheGrid().get(grid.getTempEnemyShipRow()).getTheSquares().get(grid.getTempEnemyShipColumn()).addEnemyShip(enemyShip);
			grid.getEnemyShips().add(enemyShip);
		}

	}

	public void moveEnemyShipsOnGrid() {

		//if there are any enemy ships on the grid, moves them one by one
		if(grid.getEnemyShips().size() > 0) {
			for(EnemyShip enemyShip: grid.getEnemyShips()) {
				grid.moveEnemy(enemyShip);
			}
		}

	}

	public void checkSquares() {

		//gets the current location of MS
		int xLoc = grid.getTempMastershipRow();
		int yLoc = grid.getTempMastershipColumn();

		//ships cannot be destroyed on square 0,0 - one directional intergalactic hole
		if(xLoc == 0 && yLoc == 0) {
			return;
		} else {
			//checks enemy ships on the square
			ArrayList<EnemyShip> enemyShipsOnSquare = grid.getEnemyShipsOnSquare(xLoc, yLoc);
			System.out.println("Enemies on square: " + enemyShipsOnSquare.size());

			//defensive mode
			if(this.gameMode == defensiveMode) {
				//if 1 enemy ship on the square, the enemy ship gets destroyed and the game continues
				if(enemyShipsOnSquare.size() > 0 && enemyShipsOnSquare.size() < defensiveModeEnemies) {
					grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheEnemyShips().removeAll(enemyShipsOnSquare);
					grid.getEnemyShips().removeAll(enemyShipsOnSquare);
					System.out.println("Enemy ship destroyed");
				}
				//defensive mode - if 2 or more enemy ships on the square, MasterShip gets destroyed - GAME OVER!
				if(enemyShipsOnSquare.size() >= defensiveModeEnemies) {
					grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).removeMasterShip();
					gameOver = true;
					System.out.println("MasterShip destroyed - GAME OVER!");
				}

				//offensive mode
			} else {
				//if 1 or 2 enemy ships on the square, the enemy ships get destroyed and the game continues
				if(enemyShipsOnSquare.size() > 0 && enemyShipsOnSquare.size() < offensiveModeEnemies) {
					grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheEnemyShips().removeAll(enemyShipsOnSquare);
					grid.getEnemyShips().removeAll(enemyShipsOnSquare);
					System.out.println("Enemy ship(s) destroyed");
				}
				//offensive mode - if 3 or more enemy ships on the square, MasterShip gets destroyed - GAME OVER!
				if(enemyShipsOnSquare.size() >= offensiveModeEnemies) {
					grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).removeMasterShip();
					gameOver = true;
					System.out.println("MasterShip destroyed - GAME OVER!");
				}
			}
		}

	}

	public void changeMasterShipMode() {

		if(this.gameMode == defensiveMode) {
			setGameMode(offensiveMode);
			System.out.println("Offensive");
			setGameModeMessage("MasterShip mode: OFFENSIVE");
			notifyObservers();
		} else {
			setGameMode(defensiveMode);
			System.out.println("Defensive");
			setGameModeMessage("MasterShip mode: DEFENSIVE");
			notifyObservers();
		}

	}

	@Override
	public void registerObserver(SkyWarsGUI gui) {
		this.gameModeObservers.add(gui);

	}

	@Override
	public void removeObserver(SkyWarsGUI gui) {
		this.gameModeObservers.remove(gui);

	}

	@Override
	public void notifyObservers() {

		for(SkyWarsGUI tempGUI : this.gameModeObservers) {
			tempGUI.update(this.gameModeMessage);
		}

	}

	public void saveGame() {
		grid.setGameMode(this.gameMode);
		grid.setGameModeMessage(this.gameModeMessage);
		
		//create output stream
		try {
			FileOutputStream fos = new FileOutputStream(FILE_NAME);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(this.grid);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Game saved");

	}

	public void loadGame() {
		//check if a saved game can be found
		boolean checkSavedGame = new File(FILE_NAME).exists();

		if(checkSavedGame == true) {
			//create input stream
			try {
				FileInputStream fis = new FileInputStream(FILE_NAME);
				ObjectInputStream ois = new ObjectInputStream(fis);

				grid = (Grid) ois.readObject();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "File does not exist! Start a new game from File menu", "File Not Found", JOptionPane.INFORMATION_MESSAGE);
		}
		this.gameMode = grid.getGameMode();
		this.gameModeMessage = grid.getGameModeMessage();
		setGameOver(false);

	}

	public Grid getGrid() {
		return this.grid;
	}

	public int getGameMode() {
		return this.gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public String getGameModeMessage() {
		return this.gameModeMessage;
	}

	public void setGameModeMessage(String gameModeMessage) {
		this.gameModeMessage = gameModeMessage;
	}

	public int getDefensiveMode() {
		return this.defensiveMode;
	}

	public void setDefensiveMode(int defensiveMode) {
		this.defensiveMode = defensiveMode;
	}

	public int getOffensiveMode() {
		return this.offensiveMode;
	}

	public void setOffensiveMode(int offensiveMode) {
		this.offensiveMode = offensiveMode;
	}

	public int getDefensiveModeEnemies() {
		return this.defensiveModeEnemies;
	}

	public void setDefensiveModeEnemies(int defensiveModeEnemies) {
		this.defensiveModeEnemies = defensiveModeEnemies;
	}

	public int getOffensiveModeEnemies() {
		return this.offensiveModeEnemies;
	}

	public void setOffensiveModeEnemies(int offensiveModeEnemies) {
		this.offensiveModeEnemies = offensiveModeEnemies;
	}

}
