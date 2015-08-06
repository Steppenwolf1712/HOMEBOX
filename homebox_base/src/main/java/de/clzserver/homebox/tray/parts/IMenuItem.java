package de.clzserver.homebox.tray.parts;

public interface IMenuItem extends IFunction{

	public String getName();
	public String getRef();
	public IMenuItem[] getSubmenu();
}
