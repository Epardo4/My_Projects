
public class Main {	
	/**
	 * The main function, starts the operating system
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			OS.StartUp(new Init());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
