package observer_pattern;

import java.util.List;
import java.util.ArrayList;

public class PartsCostData implements Subject{

	private ArrayList<Observer> observerList;
	
	private String name;
	private double price;
	
	public PartsCostData(String name, double price) {
		this.name = name;
		this.price = price;
		observerList = new ArrayList<Observer>();
	}
	
	public void setname(String name) {
		this.name = name;
		notifyObservers();
	}
	
	public void setPrice(double price) {
		this.price = price;
		notifyObservers();
	}
	
	public void display() {
		System.out.println("Name : "+name+", price : "+price);
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
			ob.update(price,name);
		}
		System.out.println("================");
	}
	
}
