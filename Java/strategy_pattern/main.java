package strategy_pattern;

public class main {
	
	public static void main(String [] args) {
		PartsCostData MPCD = new PartsCostData(1000.0);
		ShowAsWon won = new ShowAsWon(MPCD,1);
		ShowAsDollar dollar = new ShowAsDollar(MPCD,1200);
		ShowAsYen Yen = new ShowAsYen(MPCD,100);
		
		MPCD.setPrice(30000);
		MPCD.setPrice(59000);
		MPCD.setPrice(4000);
		return ;
	}
}
