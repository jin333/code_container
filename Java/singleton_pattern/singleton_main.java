package Singleton_pattern;

public class singleton_main {

	public static void main(String [] args) {
		static_used_singleton.getInstance();
		System.out.println("=================");
		static_block_used_singleton.getInstance();
		System.out.println("=================");
		lazy_used_singleton.getInstance();
		System.out.println("=================");
		thread_safe_singleton.getInstance();
		System.out.println("=================");
		inner_class_singleton.getInstance();
		System.out.println("=================");
		enum_used_singleton.getInstance();
		
	}
}
