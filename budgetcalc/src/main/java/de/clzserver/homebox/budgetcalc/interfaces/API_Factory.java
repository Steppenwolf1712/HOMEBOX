package de.clzserver.homebox.budgetcalc.interfaces;

import de.clzserver.homebox.budgetcalc.ods.ODS_API;
import de.clzserver.homebox.budgetcalc.sql.SQL_API;

public class API_Factory {

	
	private static API_Factory single = null;
	
	private API_Factory() {
		
	}
	
	public static API_Factory getInstance() {
		if (single == null) 
			single = new API_Factory();
		return single;
	}
	
	public Budget_API create_ODS_API() {
		return new ODS_API();
	}

	public Budget_API create_SQL_API() {
		return new SQL_API();
	}
}
