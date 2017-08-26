package de.clzserver.homebox.budgetcalc.ods;

import java.util.ArrayList;

import de.clzserver.homebox.budgetcalc.interfaces.IBudget;
import de.clzserver.homebox.budgetcalc.interfaces.Budget_API;
import de.clzserver.homebox.budgetcalc.interfaces.MonthEnum;
import de.clzserver.homebox.config.Config;

public class ODS_API implements Budget_API {

	@Override
	public boolean islocked() {
		return new Locker().islocked();
	}

	@Override
	public String getCurrentUser() {
		return new Locker().getCurrentUser();
	}

	@Override
	public IBudget[] getMonth(MonthEnum m_enum, int year) {
		Config cfg = Config.getInstance();

		String path = cfg.getValue(Config.CALCODS_PATH_KEY);
		path+=cfg.getValue(Config.CALCODS_NAME_KEY);

		CalcTable calc = CalcTable.getInstance(path);
		
		ArrayList<IBudget> list = calc.readout_Table(year+"_"+m_enum);
		IBudget[] erg = new IBudget[list.size()];
		return list.toArray(erg);
	}

	@Override
	public void writeBudget(IBudget calc) {
		Config cfg = Config.getInstance();

		String path = cfg.getValue(Config.CALCODS_PATH_KEY);
		path+=cfg.getValue(Config.CALCODS_NAME_KEY);

		CalcTable calctable = CalcTable.getInstance(path);
		
		calctable.write_row(getTableName(calc), calc.getVerwendung(), calc.getBetrag(), calc.getDatumString(), calc.getUser());
	}
	
	private String getTableName(IBudget buddi) {
		String erg = "";
		
		String[] parts = buddi.getDatumString().split("\\.");
		
		erg += "20";
		erg += parts[2];
		erg += "_";
		
		int index = Integer.parseInt(parts[1])-1;
		MonthEnum[] mes = MonthEnum.values();
		
		erg += mes[index];
		
		return erg;
	}


}
