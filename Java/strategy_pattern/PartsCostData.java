package strategy_pattern;

import java.util.List;
import java.util.ArrayList;

public class PartsCostData implements Subject{

	private ArrayList<Observer> observerList;
	
	private double price;
	
	public PartsCostData(double price) {
		this.price = price;
		observerList = new ArrayList<Observer>();
	}
	
	public void setPrice(double price) {
		this.price = price;
		notifyObservers();
	}
	
	public void display() {
		System.out.println("Price : "+price+ "Won");
	}
	
	@Override
	public void registerObserver(Observer OB) {
		observerList.add(OB);
	}

	@Override
	public void deleteObserver(Observer OB) {
		int i = observerList.indexOf(OB);
		if (i >= 0) {
			observerList.remove(i);
		}
	}

	@Override
	public void notifyObservers() {
		for(Observer ob : observerList) {
			ob.update(price);
		}
		System.out.println("================");
	}
	
}
