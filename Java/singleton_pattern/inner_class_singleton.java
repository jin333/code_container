package Singleton_pattern;

public class inner_class_singleton {

	
	private inner_class_singleton () {}
	private static class Singleton {
		private static final inner_class_singleton instance = new inner_class_singleton();
	}
	
	public static inner_class_singleton getInstance () {
		System.out.println("getinstance");
		return Singleton.instance;
	}
}
