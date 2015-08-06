package de.clzserver.homebox.tray.parts;

class FKT_DEF {

	private IMenuItem fkt = null;
	private boolean placeholder = false;
	
	public FKT_DEF(IMenuItem fkt, boolean placeholder) {
		this.fkt = fkt;
		this.placeholder = placeholder;
	}
	
	public void do_funktion() {
		fkt.start();
	}
	
	public String getName() {
		return fkt.getName();
	}
	
	public String getRef() {
		return fkt.getRef();
	}
	
	
	public boolean is_placeholder() {
		return this.placeholder;
	}
	
	public boolean has_Submenu() {
		if (fkt.getSubmenu() == null || fkt.getSubmenu().length == 0)
			return false;
		return true;
	}
	
	public IMenuItem[] get_Submenu() {
		return fkt.getSubmenu();
	}
}
