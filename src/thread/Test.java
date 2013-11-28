package thread;

public class Test {
	
	private static boolean ready  = false;
	private static int number ;
	
	static class TestT implements Runnable{

		@Override
		public void run() {
			System.out.println("=========");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(!ready){
				Thread.yield();
				System.out.println(number);
			}
			
		}
		
	}
	
	
	
	public static void main(String[] args) {
		new Thread(new TestT()).start();
		ready = false;
		number = 25;
	}
}
