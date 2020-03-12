package skyWarsGame;

import static org.junit.Assert.*;

import org.junit.Test;

public class SquareTest {

	@Test
	public void testAddMasterShip() {
		int testNum = 1;
		Square square = new Square(testNum);
		MasterShip ms = new MasterShip();
		
		square.addMasterShip(ms);
		
		assertTrue(square.getTheMasterShip() == ms);
		
	}

	@Test
	public void testRemoveMasterShip() {
		int testNum = 1;
		Square square = new Square(testNum);
		MasterShip ms = new MasterShip();
		
		square.addMasterShip(ms);
		square.removeMasterShip();
		
		assertTrue(square.getTheMasterShip() == null);
		
	}

	@Test
	public void testAddEnemyShip() {
		int testNum = 0;
		Square square = new Square(testNum);
		BattleCruiser enemyShip = new BattleCruiser();
		
		square.addEnemyShip(enemyShip);
		
		assertTrue(square.getTheEnemyShips().contains(enemyShip));
		
	}

	@Test
	public void testRemoveEnemyShip() {
		int testNum = 3;
		Square square = new Square(testNum);
		BattleShooter enemyShip = new BattleShooter();
		
		square.addEnemyShip(enemyShip);
		square.removeEnemyShip(enemyShip);
		
		assertTrue(square.getTheEnemyShips().contains(enemyShip) == false);
		
	}

	@Test
	public void testSetColumnNumber() {
		Square square = new Square();
		int testNum = 2;
		
		square.setColumnNumber(testNum);
		
		int actual = square.getColumnNumber();
		int expected = testNum;
		
		assertTrue(actual == expected);
		
	}

}
