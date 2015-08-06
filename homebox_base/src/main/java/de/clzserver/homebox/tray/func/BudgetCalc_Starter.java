package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.budgetcalc.gui.Gui;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class BudgetCalc_Starter implements IMenuItem{

	private static final String REF = "BUDGET_CALC";
	private static final String NAME = "Abrechnung";

	@Override
	public void start() {
		new Gui();
	}

	@Override
	public String getName() {
		return NAME ;
	}

	@Override
	public String getRef() {
		
		return REF ;
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
