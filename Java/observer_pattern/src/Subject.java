package observer_pattern;

public interface Subject {
	public void registerObserver(Observer OB);
	public void deleteObserver(Observer OB);
	public void notifyObservers();
}
