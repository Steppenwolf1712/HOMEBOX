package de.clzserver.homebox.budgetcalc.ods.interfaces;

public interface IBudget {

	public String getVerwendung();
	
	public float getBetrag();
	public String getBetragString();
	
	public long getDatum();
	public String getDatumString();
	
	public String getUser();
	
}
