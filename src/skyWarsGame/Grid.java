package skyWarsGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Grid implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList <Row> theGrid = new ArrayList <Row> ();
	private final int ROWS = 4;
	private final int COLUMNS = 4;
	private ArrayList<EnemyShip> enemyShips;
	private Random rand;
	private int tempMasterShipColumn;
	private int tempMasterShipRow;
	private int tempEnemyShipColumn;
	private int tempEnemyShipRow;
	private int gameMode;
	private String gameModeMessage;

	public Grid() {

		//initialises an empty grid
		for(int rowLoop = 0; rowLoop < ROWS; rowLoop++) {
			Row newRow = new Row(rowLoop);
			for(int columnLoop = 0; columnLoop < COLUMNS; columnLoop++) {
				Square square = new Square(columnLoop);
				newRow.getTheSquares().add(square);
			}
			theGrid.add(newRow);
		}
		enemyShips = new ArrayList<EnemyShip>();
	}

	public void printGrid() {

		System.out.println("==================");
		for(Row tempRow : this.theGrid) {
			System.out.print("|");
			for(Square tempSquare : tempRow.getTheSquares()) {
				MasterShip ms = tempSquare.getTheMasterShip();
				ArrayList<EnemyShip> enemyShips = tempSquare.getTheEnemyShips();
				String printChar = "   ";
				if(ms != null) {
					printChar = " " + ms.getPrintChar() + " ";
				} else if (enemyShips.size() == 1) {
					printChar = " " + enemyShips.get(0).getPrintChar() + " ";
				} else if (enemyShips.size() > 1) {
					printChar = " " + enemyShips.size() + "E";
				}
				System.out.print(printChar + "|");
			}
			System.out.println();
		}
		System.out.println("==================");
	}

	//returns an ArrayList of the ships on the square xLoc, yLoc (may be empty)
	public ArrayList<EnemyShip> getEnemyShipsOnSquare(int xLoc, int yLoc) {

		for(Row tempRow : this.theGrid) {
			if(tempRow.getRowNumber() == xLoc) {
				for(Square tempSquare : tempRow.getTheSquares()) {
					if(tempSquare.getColumnNumber() == yLoc) {
						return tempSquare.getTheEnemyShips();
					}
				}
			}
		}
		return new ArrayList<EnemyShip> ();

	}

	//returns true if the MasterShip is on the square xLoc, yLoc, otherwise returns false
	public boolean isMasterShipOn(int xLoc, int yLoc) {

		for(Row tempRow : this.theGrid) {
			if(tempRow.getRowNumber() == xLoc) {
				for(Square tempSquare : tempRow.getTheSquares()) {
					if(tempSquare.getColumnNumber() == yLoc) {
						if(tempSquare.getTheMasterShip() == null) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		return false;

	}

	//returns a collection of squares that a ship on xLoc, yLoc can legally move to
	public ArrayList<Square> getNeighboursFor(int xLoc, int yLoc) {

		ArrayList<Square> theNeighbouringSquares = new ArrayList<Square>();

		//finds the current square
		for(Row tempRow : this.theGrid) {
			if(tempRow.getRowNumber() == xLoc) {
				for(Square tempSquare : tempRow.getTheSquares()) {
					if(tempSquare.getColumnNumber() == yLoc) {
						//adds the square on the left (if not the column number 0)
						if(xLoc != 0) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc-1).getTheSquares().get(yLoc));
						}
						//adds the square on the right (if not the column number 3)
						if(xLoc != ROWS-1) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc+1).getTheSquares().get(yLoc));
						}
						//adds the square above (if not the row number 0)
						if(yLoc != 0) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc).getTheSquares().get(yLoc-1));
						}
						//adds the square below (if not the row number 3)
						if(yLoc != COLUMNS-1) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc).getTheSquares().get(yLoc+1));
						}
						//adds the square diagonally towards top left
						if(xLoc != 0 && yLoc != 0) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc-1).getTheSquares().get(yLoc-1));
						}
						//adds the square diagonally towards top right
						if(xLoc != ROWS-1 && yLoc != 0) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc+1).getTheSquares().get(yLoc-1));
						}
						//adds the square diagonally towards bottom left
						if(xLoc != 0 && yLoc != COLUMNS-1) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc-1).getTheSquares().get(yLoc+1));
						}
						//adds the square diagonally towards bottom right
						if(xLoc != ROWS-1 && yLoc != COLUMNS-1) {
							theNeighbouringSquares.add(this.theGrid.get(xLoc+1).getTheSquares().get(yLoc+1));
						}
					}
				}
			}
		}
		return theNeighbouringSquares;	

	}

	//moves MasterShip
	public void moveMasterShip(int xLoc, int yLoc) {

		ArrayList<Square> possibleMoves = getNeighboursFor(xLoc, yLoc);

		rand = new Random();

		MasterShip ms = this.theGrid.get(xLoc).getTheSquares().get(yLoc).getTheMasterShip();
		//removes MS from the current square
		this.theGrid.get(xLoc).getTheSquares().get(yLoc).removeMasterShip();

		//chooses a random neighbouring square and adds MS to it
		Square randomSquare = possibleMoves.get(rand.nextInt(possibleMoves.size()));
		randomSquare.addMasterShip(ms);

		//gets the location of the new square
		int theColumnNumber = randomSquare.getColumnNumber();
		int theRowNumber= -1;
		for(Row tempRow: this.theGrid) {
			if(tempRow.getTheSquares().contains(randomSquare)) {
				theRowNumber = tempRow.getRowNumber();
			}	
		}	

		//stores the location of the new square
		this.tempMasterShipColumn = theColumnNumber;
		this.tempMasterShipRow = theRowNumber;	

	}

	//finds the location of each enemy ship on grid
	public void moveEnemy(EnemyShip enemyShip) {

		int tempShipColumn = 0;
		int tempShipRow = 0;

		for(Row tempRow: this.theGrid) {
			for(Square tempSquare: tempRow.getTheSquares()) {
				if(tempSquare.getTheEnemyShips().contains(enemyShip)) {
					tempShipColumn = tempSquare.getColumnNumber();
					tempShipRow = tempRow.getRowNumber();
				}
			}	
		}	
		moveEnemyShip(enemyShip, tempShipRow, tempShipColumn);

	}

	//moves enemy ship
	public void moveEnemyShip(EnemyShip enemyShip, int xLoc, int yLoc) {

		ArrayList<Square> possibleMoves = getNeighboursFor(xLoc, yLoc);
		rand = new Random();

		//removes enemy ship from the current square
		this.theGrid.get(xLoc).getTheSquares().get(yLoc).removeEnemyShip(enemyShip);

		//chooses a random neighbouring square and adds enemy ship to it
		Square randomSquare = possibleMoves.get(rand.nextInt(possibleMoves.size()));
		randomSquare.addEnemyShip(enemyShip);

	}

	public ArrayList<Row> getTheGrid() {
		return this.theGrid;
	}

	public int getROWS() {
		return ROWS;
	}

	public int getCOLUMNS() {
		return COLUMNS;
	}

	public ArrayList<EnemyShip> getEnemyShips() {
		return this.enemyShips;
	}

	public int getTempMastershipColumn() {
		return this.tempMasterShipColumn;
	}

	public void setTempMastershipColumn(int tempMastershipColumn) {
		this.tempMasterShipColumn = tempMastershipColumn;
	}

	public int getTempMastershipRow() {
		return this.tempMasterShipRow;
	}

	public void setTempMastershipRow(int tempMastershipRow) {
		this.tempMasterShipRow = tempMastershipRow;
	}

	public int getTempEnemyShipColumn() {
		return this.tempEnemyShipColumn;
	}

	public void setTempEnemyShipColumn(int tempEnemyShipColumn) {
		this.tempEnemyShipColumn = tempEnemyShipColumn;
	}

	public int getTempEnemyShipRow() {
		return this.tempEnemyShipRow;
	}

	public void setTempEnemyShipRow(int tempEnemyShipRow) {
		this.tempEnemyShipRow = tempEnemyShipRow;
	}
	
	public int getGameMode() {
		return this.gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}
	
	public String getGameModeMessage() {
		return this.gameModeMessage;
	}

	public void setGameModeMessage(String gameModeMessage) {
		this.gameModeMessage = gameModeMessage;
	}

}
