package de.clzserver.homebox.budgetcalc.ods;

import de.clzserver.homebox.budgetcalc.ods.interfaces.IODS_API;

public class API_Factory {

	
	private static API_Factory single = null;
	
	private API_Factory() {
		
	}
	
	public static API_Factory getInstance() {
		if (single == null) 
			single = new API_Factory();
		return single;
	}
	
	public IODS_API createAPI() {
		return new ODS_API();
	}
}
