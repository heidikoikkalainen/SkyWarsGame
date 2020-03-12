package skyWarsGame;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class GridTest {

	@Test
	public void testGetEnemyShipsOnSquare() {
		Grid grid = new Grid();
		int xLoc = 1;
		int yLoc = 1;
		int shipsOnTestSquare = 3;
		
		//adds three enemy ships on test square
		for(int loop = 0; loop < shipsOnTestSquare; loop++) {
			BattleStar testShip = new BattleStar();
			grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).addEnemyShip(testShip);
		}
		
		int actual = grid.getEnemyShipsOnSquare(xLoc, yLoc).size();
		int expected = shipsOnTestSquare;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testIsMasterShipOn() {
		Grid grid = new Grid();
		MasterShip ms = new MasterShip();
		int xLoc = 1;
		int yLoc = 1;
		
		//adds MasterShip to test square
		grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).addMasterShip(ms);
		
		assertTrue(grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheMasterShip() == ms);
		
	}

	@Test
	public void testGetNeighboursFor() {
		Grid grid = new Grid();
		int xLoc = 0;
		int yLoc = 0;
		int testNeighbouringSquares = 3;
		ArrayList<Square> theNeighbouringSquares = new ArrayList<Square>();
		
		//adds the square on the left (if not the column number 0)
		if(xLoc != 0) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc-1).getTheSquares().get(yLoc));
		}
		//adds the square on the right (if not the column number 3)
		if(xLoc != grid.getROWS()-1) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc+1).getTheSquares().get(yLoc));
		}
		//adds the square above (if not the row number 0)
		if(yLoc != 0) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc-1));
		}
		//adds the square below (if not the row number 3)
		if(yLoc != grid.getCOLUMNS()-1) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc+1));
		}
		//adds the square diagonally towards top left
		if(xLoc != 0 && yLoc != 0) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc-1).getTheSquares().get(yLoc-1));
		}
		//adds the square diagonally towards top right
		if(xLoc != grid.getROWS()-1 && yLoc != 0) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc+1).getTheSquares().get(yLoc-1));
		}
		//adds the square diagonally towards bottom left
		if(xLoc != 0 && yLoc != grid.getCOLUMNS()-1) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc-1).getTheSquares().get(yLoc+1));
		}
		//adds the square diagonally towards bottom right
		if(xLoc != grid.getROWS()-1 && yLoc != grid.getCOLUMNS()-1) {
			theNeighbouringSquares.add(grid.getTheGrid().get(xLoc+1).getTheSquares().get(yLoc+1));
		}
		
		int actual = theNeighbouringSquares.size();
		int expected = testNeighbouringSquares;
		
		assertTrue(actual == expected);
			
	}

	@Test
	public void testMoveMasterShip() {
		Grid grid = new Grid();
		int xLoc = 1;
		int yLoc = 1;
		int newXLoc = 2;
		int newYLoc = 1;
		
		//gets the MasterShip and removes it from the current square 
		MasterShip ms = grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheMasterShip();
		grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).removeMasterShip();
		//adds the MasterShip on a new square
		grid.getTheGrid().get(newXLoc).getTheSquares().get(newYLoc).addMasterShip(ms);
		
		assertTrue(grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheMasterShip() == null && grid.getTheGrid().get(newXLoc).getTheSquares().get(newYLoc).getTheMasterShip() == ms);
		
	}

	@Test
	public void testMoveEnemy() {
		Grid grid = new Grid();
		BattleStar enemyShip = new BattleStar();
		int xLoc = 1;
		int yLoc = 1;
		int tempShipRow = 0;
		int tempShipColumn = 0;
		
		//adds an enemy ship on a square 
		grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).addEnemyShip(enemyShip);
		
		//goes through all the squares and finds the location of enemy ships
		for(Row tempRow: grid.getTheGrid()) {
			for(Square square: tempRow.getTheSquares()) {
				if(square.getTheEnemyShips().contains(enemyShip)) {
					tempShipColumn = square.getColumnNumber();
					tempShipRow = tempRow.getRowNumber();
				}
			}	
		}
		assertTrue(tempShipRow == xLoc && tempShipColumn == yLoc);
		
	}

	@Test
	public void testMoveEnemyShip() {
		Grid grid = new Grid();
		BattleCruiser enemyShip = new BattleCruiser();
		int xLoc = 1;
		int yLoc = 1;
		int newXLoc = 2;
		int newYLoc = 2;
		
		//adds an enemy ship on a square and removes it
		grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).addEnemyShip(enemyShip);
		grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).removeEnemyShip(enemyShip);
		//adds the enemy ship on a new square 
		grid.getTheGrid().get(newXLoc).getTheSquares().get(newYLoc).addEnemyShip(enemyShip);
		
		int oldSquareShips = 0;
		int newSquareShips = 1;
		
		assertTrue(grid.getTheGrid().get(xLoc).getTheSquares().get(yLoc).getTheEnemyShips().size() == oldSquareShips && grid.getTheGrid().get(newXLoc).getTheSquares().get(newYLoc).getTheEnemyShips().size() == newSquareShips);
		
	}
	
	@Test
	public void testSetTempMastershipColumn() {
		Grid grid = new Grid();
		int testNum = 2;
		
		grid.setTempMastershipColumn(testNum);
		
		int actual = grid.getTempMastershipColumn();
		int expected = testNum;
		
		assertTrue(actual == expected);
	}

	@Test
	public void testSetTempMastershipRow() {
		Grid grid = new Grid();
		int testNum = 3;
		
		grid.setTempMastershipRow(testNum);
		
		int actual = grid.getTempMastershipRow();
		int expected = testNum;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetTempEnemyShipColumn() {
		Grid grid = new Grid();
		int testNum = 1;
		
		grid.setTempEnemyShipColumn(testNum);
		
		int actual = grid.getTempEnemyShipColumn();
		int expected = testNum;
		
		assertTrue(actual == expected);
		
	}

	@Test
	public void testSetTempEnemyShipRow() {
		Grid grid = new Grid();
		int testNum = 0;
		
		grid.setTempEnemyShipRow(testNum);
		
		int actual = grid.getTempEnemyShipRow();
		int expected = testNum;
		
		assertTrue(actual == expected);
		
	}

}
