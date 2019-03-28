package Singleton_pattern;

public class static_used_singleton {

	private static final static_used_singleton instance = new static_used_singleton();
	
	private static_used_singleton() {
		System.out.println("Created!  ");
	}
	
	public static static_used_singleton getInstance() {
		System.out.println("getinstance!  ");
		return instance;
		
	}
}
