package strategy_pattern;

public class ShowAsDollar implements Observer, DisplayElement{

	private double cost;
	private double exchangeRate;
	private Subject subject;
	
	public ShowAsDollar(Subject subject){
		this.subject = subject;
	}
	
	public ShowAsDollar(Subject subject, double exchangeRate){
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
		System.out.println("The Price is : "+this.cost/exchangeRate+" Dollar");
		
	}

}
