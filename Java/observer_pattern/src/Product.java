package observer_pattern;

class Product implements Observer{
	private String name;
	private String description;
	private String partName;
	private double cost;
	private int partAmount;
	
	private Subject PCD; 
	
	public Product(PartsCostData cd) {
		this.PCD = cd;
	} 
	
	public Product(Subject PCD, String name, int partAmount, String description) {
		this.name = name;
		this.partAmount = partAmount;
		this.description = description;
		this.PCD = PCD;
		PCD.registerObserver(this);
	}
	
	public void display() {
		System.out.print("Part_Name : "+partName+", cost : "+this.cost);
		System.out.println(", Name : "+this.name+", Amount : "+this.partAmount+", total_price"+this.partAmount*this.cost);
	}

	@Override
	public void update(double cost,String partName) {
		this.cost = cost;
		this.partName = partName;
		display();
	}
	
	
}
