
public class Init extends UserlandProcess{
	public int pid = this.getPid();
	/**
	 * Implements from Process, Creates all necessary processes and exits
	 */
//	public void main() {
//		try {
//			switch(this.getTimesStarted()) {
//			case 1:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 2:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 3:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 4:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 5:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 6:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 7:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 8:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 9:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 10:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 11:
//				OS.CreateProcess(new AllocateAllMemory(), PCB.Priority.REAL_TIME);
//				break;
//			case 12:
//				OS.exit();
//			}
//		}
//		catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
	public void main() {
		try {
			switch(this.getTimesStarted()) {
			case 1:
				OS.CreateProcess(new Ping(), PCB.Priority.REAL_TIME);
				break;
			case 2:
				OS.CreateProcess(new Pong(), PCB.Priority.REAL_TIME);
				break;
			case 3:
				OS.exit();
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
