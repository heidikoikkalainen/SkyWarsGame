package skyWarsGame;

public interface Observable {
	
	public void registerObserver(SkyWarsGUI gui);
	public void removeObserver(SkyWarsGUI gui);
	public void notifyObservers();

}
