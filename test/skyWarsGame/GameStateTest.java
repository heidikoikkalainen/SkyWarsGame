package skyWarsGame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

public class GameStateTest {

	@Test
	public void testInitialiseNewGame() {
		Grid grid = new Grid();
		Random rand = new Random(System.currentTimeMillis());
		MasterShip ms = new MasterShip();
		int testShipRow = 0;
		int testShipColumn = 0;
		
		//places MasterShip on a random square
		grid.setTempMastershipRow(rand.nextInt(grid.getROWS()));
		grid.setTempMastershipColumn(rand.nextInt(grid.getCOLUMNS()));
		grid.getTheGrid().get(grid.getTempMastershipRow()).getTheSquares().get(grid.getTempMastershipColumn()).addMasterShip(ms);
		
		//finds the location of the MasterShip
		for(Row tempRow: grid.getTheGrid()) {
			for(Square square: tempRow.getTheSquares()) {
				if(square.getTheMasterShip() == ms) {
					testShipColumn = square.getColumnNumber();
					testShipRow = tempRow.getRowNumber();
				}
			}	
		}
		int actualXLoc = grid.getTempMastershipRow();
		int actualYLoc = grid.getTempMastershipColumn();
		
		assertTrue(actualXLoc == testShipRow && actualYLoc == testShipColumn);
		
	}

	@Test
	public void testCreateEnemyShip() {
		Grid grid = new Grid();
		EnemyShipFactory enemyShipFactory = new EnemyShipFactory();
		int enemyStartRow = 0;
		int enemyStartColumn = 0;
		int shipsOnTestSquare = 1;

		//generates a random enemy ship
		EnemyShip enemyShip = enemyShipFactory.getEnemyShip();
		grid.getTheGrid().get(enemyStartRow).getTheSquares().get(enemyStartColumn).addEnemyShip(enemyShip);
		grid.getEnemyShips().add(enemyShip);

		int actual = grid.getEnemyShipsOnSquare(enemyStartRow, enemyStartColumn).size();
		int expected = shipsOnTestSquare;

		assertTrue(actual == expected);

	}

	@Test
	public void testCheckSquares() {
		GameState game = new GameState();
		ArrayList<EnemyShip> testEnemyShips = new ArrayList<EnemyShip>();
		int gameMode = 1;
		int defensiveMode = 0;
		int defensiveModeEnemies = 2;
		int offensiveModeEnemies = 3;
		int xLoc = 1;
		int yLoc = 1;
		
		BattleStar e1 = new BattleStar();
		BattleCruiser e2 = new BattleCruiser();
		BattleShooter e3 = new BattleShooter();
		
		testEnemyShips.add(e1);
		testEnemyShips.add(e2);
		testEnemyShips.add(e3);
		
		if(xLoc != 0 && yLoc != 0) {
			//defensive mode
			if(gameMode == defensiveMode) {
				//if 1 enemy ship on the square, the enemy ship gets destroyed and the game continues
				if(testEnemyShips.size() > 0 && testEnemyShips.size() < defensiveModeEnemies) {
					game.getGrid().getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheEnemyShips().removeAll(testEnemyShips);
					game.getGrid().getEnemyShips().removeAll(testEnemyShips);
				}
				//defensive mode - if 2 or more enemy ships on the square, MasterShip gets destroyed - GAME OVER!
				if(testEnemyShips.size() >= defensiveModeEnemies) {
					game.setGameOver(true);
				}

			//offensive mode
			} else {
				//if 1 or 2 enemy ships on the square, the enemy ships get destroyed and the game continues
				if(testEnemyShips.size() > 0 && testEnemyShips.size() < offensiveModeEnemies) {
					game.getGrid().getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheEnemyShips().removeAll(testEnemyShips);
					game.getGrid().getEnemyShips().removeAll(testEnemyShips);
				}
				//offensive mode - if 3 or more enemy ships on the square, MasterShip gets destroyed - GAME OVER!
				if(testEnemyShips.size() >= offensiveModeEnemies) {
					game.setGameOver(true);
				}
			}
		}
		
		boolean actual = game.isGameOver();
		boolean expected = true;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testChangeMasterShipMode() {
		GameState game = new GameState();
		int defensiveMode = 0;
		int offensiveMode = 1;
		int testGameMode = 1;
		
		if(testGameMode == defensiveMode) {
			game.setGameMode(offensiveMode);
		} else {
			game.setGameMode(defensiveMode);
		}
		
		int actual = game.getGameMode();
		int expected = defensiveMode;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetGameMode() {
		GameState game = new GameState();
		int testMode = 1;
		
		game.setGameMode(testMode);
		
		int actual = game.getGameMode();
		int expected = testMode;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetGameOver() {
		GameState game = new GameState();
		boolean testGameOver = true;
		
		game.setGameOver(testGameOver);
		
		boolean actual = game.isGameOver();
		boolean expected = testGameOver;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetGameModeMessage() {
		GameState game = new GameState();
		String testMessage = "Offensive";
		
		game.setGameModeMessage(testMessage);
		
		String actual = game.getGameModeMessage();
		String expected = testMessage;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetDefensiveMode() {
		GameState game = new GameState();
		int defensiveMode = 0;
		
		game.setDefensiveMode(defensiveMode);
		
		int actual = game.getDefensiveMode();
		int expected = defensiveMode;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetOffensiveMode() {
		GameState game = new GameState();
		int offensiveMode = 1;
		
		game.setOffensiveMode(offensiveMode);
		
		int actual = game.getOffensiveMode();
		int expected = offensiveMode;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetDefensiveModeEnemies() {
		GameState game = new GameState();
		int defensiveModeEnemies = 2;
		
		game.setDefensiveModeEnemies(defensiveModeEnemies);
		
		int actual = game.getDefensiveModeEnemies();
		int expected = defensiveModeEnemies;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetOffensiveModeEnemies() {
		GameState game = new GameState();
		int offensiveModeEnemies = 3;
		
		game.setOffensiveModeEnemies(offensiveModeEnemies);
		
		int actual = game.getOffensiveModeEnemies();
		int expected = offensiveModeEnemies;
		
		assertTrue(actual == expected);
		
	}

}
