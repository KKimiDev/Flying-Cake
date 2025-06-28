package game;

import java.util.concurrent.TimeUnit;

public class FibKimi {
	public static void main(String[] args) {
		fibTree(4, "> ");
		
		long nano1 = System.nanoTime();
		System.out.println(fib(30));
		System.out.println(System.nanoTime() - nano1);
		nano1 = System.nanoTime();
		System.out.println(fib_it(30));
		System.out.println(System.nanoTime() - nano1);
	}
	
	public static int fib(int n) {
		if (n < 2) {
			return n;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	public static int fibTree(int n, String indent) {
		int res = n;
		
		indent += "  ";
		
		if(n < 0) {
			return -1;
		} else if (n > 1) {
			res = fibTree(n-1, indent) + fibTree(n-2, indent);
		}
		
		System.out.println(indent + res);
		return res;
	}
	
	public static int fib_it(int n) {
		 if (n <= 0) { return 0; }
		 else if (n == 1) { return 1; }
		 else {
			int a = 0;
			int b1 = 1;
			for (int i = 2; i <= n; i++) {
				int b2 = a + b1;
				a = b1;
				b1 = b2;
		 	}
		 	return b1;
		 }
	}
}
