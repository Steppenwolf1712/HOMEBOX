package de.clzserver.homebox.config;

public class HBPrinter {

	private static HBPrinter single = null;
	
	private HBPrinter() {
		
	}
	
	public static HBPrinter getInstance() {
		if (single == null)
			single = new HBPrinter();
		return single;
	}
	
	public void printMSG(Class caller, String msg) {
		System.out.println(caller.getName()+": "+msg);
	}
	
	public void printError(Class caller, String msg, Exception ex) {
//		Object temp = caller.getClass();
//		if (temp.equals(Object.class.getClass()))
//			temp = caller.;
		System.err.println(caller.getName()+": "+msg+"\n"+ex.toString());
	}
}
