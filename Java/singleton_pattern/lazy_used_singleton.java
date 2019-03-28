package Singleton_pattern;

public class lazy_used_singleton {

	private static lazy_used_singleton instance;
	private lazy_used_singleton() {
		System.out.println("Created!");
	}
	
	public static lazy_used_singleton getInstance() {
		if (instance == null) {
			instance = new lazy_used_singleton();
			System.out.println("getInstance!");
		}
		return instance;
		
	}
}
