package Singleton_pattern;

public class thread_safe_singleton {

	private static thread_safe_singleton instance;
	private thread_safe_singleton() {
		System.out.println("Created!");
	}
	
	public static synchronized thread_safe_singleton getInstance() {
		if (instance == null) {
			instance = new thread_safe_singleton();
			System.out.println("getInstance!");
		}
		return instance;
		
	}
}
