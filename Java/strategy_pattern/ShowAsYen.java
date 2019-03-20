package strategy_pattern;

public class ShowAsYen implements Observer, DisplayElement{

	private double cost;
	private double exchangeRate;
	private Subject subject;
	
	public ShowAsYen(Subject subject){
		this.subject = subject;
	}
	
	public ShowAsYen(Subject subject,  double exchangeRate){
		this.cost = 0;
		this.exchangeRate = exchangeRate;
		this.subject = subject;
		this.subject.registerObserver(this);
	}

	@Override
	public void update(double cost) {
		this.cost = cost;
		this.exchangeRate = exchangeRate;
		display();
	}
	
	@Override
	public void display() {
		System.out.println("The Price is : "+this.cost/exchangeRate+" Yen");
		
	}

}
