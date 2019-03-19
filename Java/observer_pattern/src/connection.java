package observer_pattern;

public class connection {
	
	public static void main(String [] args) {
		PartsCostData MPCD = new PartsCostData("Part1", 333.0);
		Product P1 = new Product(MPCD, "Product1", 10, "P1");
		Product P2 = new Product(MPCD, "Product2", 2, "P2");
		Product P3 = new Product(MPCD, "Product3", 5, "P3");
		
		MPCD.display();
		P1.display();
		P2.display();
		P3.display();
		System.out.println("================");

		MPCD.setPrice(200.0);
		MPCD.setname("Part1-v2");
		MPCD.setPrice(258.7);
		
		return ;
	}
}
