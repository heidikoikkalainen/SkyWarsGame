package skyWarsGame;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SkyWarsGUI extends JFrame implements ActionListener, GameModeObserver {

	private GameState game;

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem mntmStartNewGame;
	private JMenuItem mntmSaveGame;
	private JMenuItem mntmLoadSavedGame;
	private JMenuItem mntmExit;
	private int input;
	private JPanel textPanel;
	private JLabel textLabel;
	private JPanel displayPanel;
	private JPanel controlPanel;
	private JButton moveButton;
	private JButton gameModeButton;
	private JLabel skyWarsLabel;
	private JLabel gameOverLabel;
	private JLabel[][] labelGrid;

	ImageIcon skyWarsLogo = new ImageIcon(this.getClass().getResource("/sky_wars.png"));
	ImageIcon gameOverLogo = new ImageIcon(this.getClass().getResource("/game_over.png"));
	ImageIcon mastershipIcon = new ImageIcon(this.getClass().getResource("/mastership.png"));
	ImageIcon spaceIcon = new ImageIcon(this.getClass().getResource("/space.png"));
	ImageIcon oneEnemyIcon = new ImageIcon(this.getClass().getResource("/one_enemy.png"));
	ImageIcon twoEnemiesIcon = new ImageIcon(this.getClass().getResource("/two_enemies.png"));
	ImageIcon threeEnemiesIcon = new ImageIcon(this.getClass().getResource("/three_enemies.png"));
	ImageIcon fourEnemiesIcon = new ImageIcon(this.getClass().getResource("/four_enemies.png"));

	private boolean hasGameEnded;
	private String gameModeUpdate;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SkyWarsGUI frame = new SkyWarsGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SkyWarsGUI() {
		game = new GameState();
		game.registerObserver(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 639);

		//creates a menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		mntmStartNewGame = new JMenuItem("Start New Game");
		mntmStartNewGame.addActionListener(this);
		fileMenu.add(mntmStartNewGame);

		mntmSaveGame = new JMenuItem("Save Game");
		mntmSaveGame.addActionListener(this);
		fileMenu.add(mntmSaveGame);

		mntmLoadSavedGame = new JMenuItem("Load Saved Game");
		mntmLoadSavedGame.addActionListener(this);
		fileMenu.add(mntmLoadSavedGame);

		fileMenu.addSeparator();

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(this);
		fileMenu.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//creates a panel for displaying text
		textPanel = new JPanel();
		textPanel.setBounds(0, 0, 500, 34);
		contentPane.add(textPanel);
		textPanel.setLayout(null);

		textLabel = new JLabel("Welcome to Sky Wars! Start new game from File menu");
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setBackground(Color.LIGHT_GRAY);
		textLabel.setForeground(Color.BLACK);
		textLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		textLabel.setBounds(0, 0, 500, 34);
		textPanel.add(textLabel);

		//creates a panel for displaying images/grid
		displayPanel = new JPanel();
		displayPanel.setBounds(0, 34, 500, 500);
		contentPane.add(displayPanel);
		displayPanel.setLayout(null);

		skyWarsLabel = new JLabel("");
		skyWarsLabel.setIcon(skyWarsLogo);
		skyWarsLabel.setBounds(0, 0, 500, 500);
		displayPanel.add(skyWarsLabel);

		controlPanel = new JPanel();
		controlPanel.setBounds(0, 533, 500, 62);
		contentPane.add(controlPanel);
		controlPanel.setLayout(null);

		//creates a 'make a move' button
		moveButton = new JButton("MAKE A MOVE");
		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.makeAMove();
				updateGUI(game, game.isGameOver());
			}
		});
		moveButton.setFont(new Font("Calibri", Font.PLAIN, 14));
		moveButton.setBounds(64, 6, 164, 50);
		controlPanel.add(moveButton);
		moveButton.setEnabled(false);

		//creates a 'change game mode' button
		gameModeButton = new JButton("CHANGE GAME MODE");
		gameModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.changeMasterShipMode();
			}
		});
		gameModeButton.setFont(new Font("Calibri", Font.PLAIN, 14));
		gameModeButton.setBounds(270, 6, 164, 50);
		controlPanel.add(gameModeButton);
		gameModeButton.setEnabled(false);

	}//end constructor

	//adds functions to menu items
	@Override
	public void actionPerformed(ActionEvent e) {

		//Exit
		if (e.getSource().equals(mntmExit)) {
			System.exit(0);
		//Start New Game
		} else if (e.getSource().equals(mntmStartNewGame)) {
			game.initialiseNewGame();
			textLabel.setText("MasterShip mode: DEFENSIVE");
			//sets up grid on the display panel
			displayPanel.setLayout(new GridLayout(game.getGrid().getROWS(), game.getGrid().getCOLUMNS()));
			labelGrid = new JLabel[game.getGrid().getROWS()][game.getGrid().getCOLUMNS()];
			updateGrid(game.getGrid());
			moveButton.setEnabled(true);
			gameModeButton.setEnabled(true);
		//Save Game
		} else if (e.getSource().equals(mntmSaveGame)) {
			game.saveGame();
			moveButton.setEnabled(false);
			gameModeButton.setEnabled(false);
			input = JOptionPane.showConfirmDialog(null, "Game saved! Would you like to continue playing?", "Game Saved", JOptionPane.YES_NO_OPTION);
			if(input == JOptionPane.YES_OPTION) {
				moveButton.setEnabled(true);
				gameModeButton.setEnabled(true);
				return;
			} else {
				System.exit(0);
			}
		//Load Saved Game
		} else if (e.getSource().equals(mntmLoadSavedGame)) {
			game.loadGame();
			textLabel.setText("Welcome back! " + game.getGameModeMessage());
			displayPanel.setLayout(new GridLayout(game.getGrid().getROWS(), game.getGrid().getCOLUMNS()));
			labelGrid = new JLabel[game.getGrid().getROWS()][game.getGrid().getCOLUMNS()];
			updateGrid(game.getGrid());
			moveButton.setEnabled(true);
			gameModeButton.setEnabled(true);
		}

	}

	//updates the GUI during the game
	public void updateGUI(GameState model, boolean gameOver) {
		this.game = model;
		this.hasGameEnded = gameOver;

		if(this.hasGameEnded == true) {
			textLabel.setText("MasterShip destroyed - GAME OVER!");
			displayPanel.removeAll();
			gameOverLabel = new JLabel("");
			gameOverLabel.setIcon(gameOverLogo);
			gameOverLabel.setPreferredSize(new Dimension(500, 500));
			gameOverLabel.setLocation(0, 0);
			displayPanel.setLayout(new FlowLayout());
			displayPanel.add(gameOverLabel);
			displayPanel.revalidate();
			displayPanel.repaint();
			moveButton.setEnabled(false);
			gameModeButton.setEnabled(false);
		} else {
			updateGrid(this.game.getGrid());
		}

	}

	//updates the display panel/grid during the game
	public void updateGrid(Grid grid) {

		displayPanel.removeAll();

		for(int rowLoop = 0; rowLoop < grid.getROWS(); rowLoop++) {
			for(int columnLoop = 0; columnLoop < grid.getCOLUMNS(); columnLoop++) {
				//displays MasterShip icon on the grid
				if(grid.isMasterShipOn(rowLoop, columnLoop) == true) {
					JLabel mastershipLabel  = new JLabel("");
					mastershipLabel.setIcon(mastershipIcon);
					mastershipLabel.setPreferredSize(new Dimension(125, 125));
					this.labelGrid[rowLoop][columnLoop] = mastershipLabel;
					this.displayPanel.add(mastershipLabel);
				} else {
					//gets enemy ships on square
					ArrayList<EnemyShip> enemyShipsOnGrid = grid.getEnemyShipsOnSquare(rowLoop, columnLoop);
					//if no enemy ships, displays background image
					if(enemyShipsOnGrid.size() == 0) {
						JLabel backgroundLabel = new JLabel("");
						backgroundLabel.setIcon(spaceIcon);
						backgroundLabel.setPreferredSize(new Dimension(125, 125));
						this.labelGrid[rowLoop][columnLoop] = backgroundLabel;
						this.displayPanel.add(backgroundLabel);
					}
					//displays 1 enemy ship
					if(enemyShipsOnGrid.size() == 1) {
						JLabel oneEnemyLabel = new JLabel("");
						oneEnemyLabel.setIcon(oneEnemyIcon);
						oneEnemyLabel.setPreferredSize(new Dimension(125, 125));
						this.labelGrid[rowLoop][columnLoop] = oneEnemyLabel;
						this.displayPanel.add(oneEnemyLabel);
					}
					if(enemyShipsOnGrid.size() > 1) {
						switch(enemyShipsOnGrid.size()) {
						//displays 2 enemy ships
						case 2:
							JLabel twoEnemiesLabel = new JLabel("");
							twoEnemiesLabel.setIcon(twoEnemiesIcon);
							twoEnemiesLabel.setPreferredSize(new Dimension(125, 125));
							this.labelGrid[rowLoop][columnLoop] = twoEnemiesLabel;
							this.displayPanel.add(twoEnemiesLabel);
							break;
						//displays 3 enemy ships
						case 3:
							JLabel threeEnemiesLabel = new JLabel("");
							threeEnemiesLabel.setIcon(threeEnemiesIcon);
							threeEnemiesLabel.setPreferredSize(new Dimension(125, 125));
							this.labelGrid[rowLoop][columnLoop] = threeEnemiesLabel;
							this.displayPanel.add(threeEnemiesLabel);
							break;
						//displays 4+ enemy ships
						default:
							JLabel fourEnemiesLabel = new JLabel("");
							fourEnemiesLabel.setIcon(fourEnemiesIcon);
							fourEnemiesLabel.setPreferredSize(new Dimension(125, 125));
							this.labelGrid[rowLoop][columnLoop] = fourEnemiesLabel;
							this.displayPanel.add(fourEnemiesLabel);
						}
					}
				}
			}
		}
		displayPanel.revalidate();
		displayPanel.repaint();

	}

	//updates the game mode message during the game
	public void update(String gameModeMessage) {
		this.gameModeUpdate = gameModeMessage;
		textLabel.setText(gameModeMessage);

	}

}//end class
