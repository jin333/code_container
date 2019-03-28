package Singleton_pattern;

public class static_block_used_singleton {

	private static static_block_used_singleton instance;
	
	private static_block_used_singleton() {
		System.out.println("Created! ");
	}
	
	static {
		try {
			System.out.println("instance create..");
			instance = new static_block_used_singleton();
		} catch (Exception e) {
			throw new RuntimeException("Exception creating StaticBlockInitalization instance.");
		}
	}
	
	public static static_block_used_singleton getInstance() {
		System.out.println("getinstance! ");
		return instance;
		
	}
}
