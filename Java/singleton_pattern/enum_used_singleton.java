package Singleton_pattern;

public enum enum_used_singleton {
	INSTANCE;
	static String test = "";
	public static enum_used_singleton getInstance() {
		test = "test";
		System.out.println("getInsatance!");
		return INSTANCE;
	}
	
}
